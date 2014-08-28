package org.stevens.changegen;

import java.util.ArrayList;
import java.util.List;

public class NoOpGenerator<TModel, TResult> implements ChangeGenerator<TModel, TResult>
{
    @Override
    public List<TResult> getChanges(TModel original, TModel current) {
        return new ArrayList<>();
    }

    @Override
    public boolean areEqual(TModel original, TModel current) {
        return false;
    }
}
