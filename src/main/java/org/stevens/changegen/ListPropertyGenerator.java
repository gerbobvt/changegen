package org.stevens.changegen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

class ListPropertyGenerator<TModel, TResult, TProp, TKey> implements ChangeGenerator<TModel, TResult>
{
    final Function<TModel, List<TProp>> listSelector;
    final Function<TProp, TKey> keySelector;
    final Function<TProp, TResult> addResultFactory;
    final Function<TProp, TResult> removeResultFactory;
    final ChangeGenerator<TProp, TResult> itemChangeGenerator;

    public ListPropertyGenerator(Function<TModel, List<TProp>> listSelector, Function<TProp, TKey> keySelector, Function<TProp, TResult> addResultFactory, Function<TProp, TResult> removeResultFactory, ChangeGenerator<TProp, TResult> itemChangeGenerator)
    {
        this.listSelector = listSelector;
        this.keySelector = keySelector;
        this.addResultFactory = addResultFactory;
        this.removeResultFactory = removeResultFactory;

        if (itemChangeGenerator == null)
        {
            this.itemChangeGenerator = new NoOpGenerator<>();
        }
        else
        {
            this.itemChangeGenerator = itemChangeGenerator;
        }
    }

    @Override
    public List<TResult> getChanges(TModel original, TModel current)
    {
        List<TProp> originalList = listSelector.apply(original);
        List<TProp> currentList = listSelector.apply(current);

        List<TResult> additions = getListAdditions(originalList, currentList);

        List<TResult> removals = getListRemovals(originalList, currentList);

        List<TResult> itemChanges = getListItemChanges(originalList, currentList);

        List<TResult> result = new ArrayList<>(additions.size() + removals.size() + itemChanges.size());

        result.addAll(additions);
        result.addAll(removals);
        result.addAll(itemChanges);

        return result;
    }

    private List<TResult> getListItemChanges(List<TProp> originalList, List<TProp> currentList) {
        List<TResult> itemChanges = new ArrayList<>();
        for (TProp originalItem : originalList)
        {
            Optional<TProp> currentItemOption = currentList.stream().filter(c -> compareItems(originalItem, c)).findFirst();
            if (currentItemOption.isPresent())
            {
                TProp currentItem = currentItemOption.get();
                if (!itemChangeGenerator.areEqual(originalItem, currentItem))
                {
                    itemChanges.addAll(itemChangeGenerator.getChanges(originalItem, currentItem));
                }
            }
        }
        return itemChanges;
    }

    private List<TResult> getListRemovals(List<TProp> originalList, List<TProp> currentList) {
        List<TResult> removals = new ArrayList<>();
        if (removeResultFactory != null)
        {
            for (TProp originalItem : originalList)
            {
                if (currentList.stream().noneMatch(currentItem -> compareItems(originalItem, currentItem)))
                {
                    removals.add(removeResultFactory.apply(originalItem));
                }
            }
        }
        return removals;
    }

    private List<TResult> getListAdditions(List<TProp> originalList, List<TProp> currentList) {
        List<TResult> additions = new ArrayList<>();
        if (addResultFactory != null)
        {
            for (TProp currentItem : currentList)
            {
                if (originalList.stream().noneMatch(originalItem -> compareItems(currentItem, originalItem)))
                {
                    additions.add(addResultFactory.apply(currentItem));
                }
            }
        }
        return additions;
    }

    @Override
    public boolean areEqual(TModel original, TModel current)
    {
        List<TProp> originalList = listSelector.apply(original);
        List<TProp> currentList = listSelector.apply(current);

        if (originalList.size() != currentList.size())
        {
            return false;
        }

        for (TProp originalItem : originalList)
        {
            Optional<TProp> currentItemOption = currentList.stream().filter(c -> compareItems(originalItem, c)).findFirst();
            if (!currentItemOption.isPresent())
            {
                return false;
            }
            else
            {
                TProp currentItem = currentItemOption.get();
                if (!itemChangeGenerator.areEqual(originalItem, currentItem))
                {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean compareItems(TProp original, TProp current)
    {
        return keySelector.apply(original).equals(keySelector.apply(current));
    }
}
