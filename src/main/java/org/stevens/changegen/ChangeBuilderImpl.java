package org.stevens.changegen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ChangeBuilderImpl<TModel, TResult> implements ChangeBuilder<TModel, TResult>
{
    private List<ChangeGenerator<TModel, TResult>> changeGenerators = new ArrayList<>();

    @Override
    public ChangeGenerator<TModel, TResult> getChangeGenerator()
    {
        return new ChangeGeneratorImpl<>(changeGenerators);
    }

    @Override
    public <TProp> ChangeBuilder<TModel, TResult> AddSelectorProperty(Function<TModel, TProp> selector,
                                                                      BiFunction<TModel, TModel, TResult> resultFactory)
            throws IllegalArgumentException
    {
        if (selector == null)
        {
            throw new IllegalArgumentException("selector");
        }

        if (resultFactory == null)
        {
            throw new IllegalArgumentException("resultFactory");
        }

        changeGenerators.add(new SelectorPropertyGenerator<>(selector, resultFactory));

        return this;
    }

    @Override
    public <TProp, TKey> ChangeBuilder<TModel, TResult> AddListProperty(Function<TModel, List<TProp>> listSelector,
                                                                        Function<TProp, TKey> keySelector,
                                                                        Function<TProp, TResult> addResultFactory,
                                                                        Function<TProp, TResult> removeResultFactory,
                                                                        ChangeGenerator<TProp, TResult> itemChangeGenerator)
            throws IllegalArgumentException
    {
        if (listSelector == null)
        {
            throw new IllegalArgumentException("listSelector");
        }

        if (keySelector == null)
        {
            throw new IllegalArgumentException("keySelector");
        }

        changeGenerators.add(new ListPropertyGenerator<>(listSelector, keySelector, addResultFactory, removeResultFactory, itemChangeGenerator));
        return this;
    }
}
