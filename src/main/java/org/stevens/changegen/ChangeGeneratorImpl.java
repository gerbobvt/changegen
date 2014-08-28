package org.stevens.changegen;

import java.util.ArrayList;
import java.util.List;

class ChangeGeneratorImpl<TModel, TResult> implements ChangeGenerator<TModel, TResult>
{
    private List<ChangeGenerator<TModel, TResult>> changeGenerators;

    ChangeGeneratorImpl(final List<ChangeGenerator<TModel, TResult>> changeGenerators)
    {
        this.changeGenerators = changeGenerators;
    }

    @Override
    public List<TResult> getChanges(TModel original, TModel current)
    {
        List<TResult> results = new ArrayList<>();

        for(ChangeGenerator<TModel, TResult> changeGenerator : changeGenerators)
        {
            if (!changeGenerator.areEqual(original,current))
            {
                results.addAll(changeGenerator.getChanges(original, current));
            }
        }

        return results;
    }

    @Override
    public boolean areEqual(final TModel original, final TModel current)
    {
        return changeGenerators.stream().allMatch(changeGenerator -> changeGenerator.areEqual(original, current));
    }
}
