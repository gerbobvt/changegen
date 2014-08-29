package org.stevens.changegen;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface ChangeBuilder<TModel, TResult>
{
    ChangeGenerator<TModel, TResult> getChangeGenerator();

    <TProp> ChangeBuilder<TModel, TResult> addSelectorProperty(Function<TModel, TProp> selector, BiFunction<TModel, TModel, TResult> resultFactory) throws IllegalArgumentException;

    <TProp, TKey> ChangeBuilder<TModel, TResult> addListProperty(Function<TModel, List<TProp>> listSelector,
                                                                 Function<TProp, TKey> keySelector,
                                                                 Function<TProp, TResult> addResultFactory,
                                                                 Function<TProp, TResult> removeResultFactory,
                                                                 ChangeGenerator<TProp, TResult> itemChangeGenerator) throws IllegalArgumentException;
    static <TModel, TResult> ChangeBuilder<TModel, TResult> getNewBuilder()
    {
        return new ChangeBuilderImpl<>();
    }
}
