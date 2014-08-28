package org.stevens.changegen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

class SelectorPropertyGenerator<TModel, TResult, TProp> implements ChangeGenerator<TModel, TResult>
{
    private Function<TModel, TProp> selector;
    private BiFunction<TModel, TModel, TResult> resultFactory;

    SelectorPropertyGenerator(Function<TModel, TProp> selector, BiFunction<TModel, TModel, TResult> resultFactory)
    {
        this.selector = selector;
        this.resultFactory = resultFactory;
    }

    @Override
    public List<TResult> getChanges(TModel original, TModel current)
    {
        if (areEqual(original, current))
        {
            return null;
        }
        List<TResult> results = new ArrayList<>();

        results.add(resultFactory.apply(original, current));

        return results;
    }

    @Override
    public boolean areEqual(TModel original, TModel current)
    {
        return selector.apply(original).equals(selector.apply(current));
    }
}
