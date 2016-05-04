package ru.spbau.mit;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by dsavv on 01.05.2016.
 */
public interface LightFuture<R> {
    boolean isReady();
    R get() throws LightExecutionException;
    <T> LightFuture<T> thenApply(Function<R, T> f);
}
