package org.stevens.changegen;

import java.util.List;

public interface ChangeGenerator<TModel, TResult>
{
    List<TResult> getChanges(TModel original, TModel current);
    boolean areEqual(TModel original, TModel current);
}
