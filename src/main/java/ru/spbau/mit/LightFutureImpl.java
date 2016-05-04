package ru.spbau.mit;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by dsavv on 01.05.2016.
 */
public class LightFutureImpl<R> implements LightFuture<R> {

    LightFutureImpl(ThreadPoolImpl tp) {
        boundedThreadPool = tp;
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    @Override
    public R get() throws LightExecutionException {
        while (!ready) {
            synchronized (gotResultEvent) {
                try {
                    gotResultEvent.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!isFailed) {
            return result;
        }
        throw new LightExecutionException("Evaluation interrupted exception", causeOfFail);
    }

    public void updateResult(R r) {
        result = r;
        ready = true;
        synchronized (gotResultEvent) {
            gotResultEvent.notifyAll();
        }
    }

    public void markAsFailed(Throwable supplierException) {
        isFailed = true;
        ready = true;
        synchronized (gotResultEvent) {
            gotResultEvent.notifyAll();
        }
    }

    @Override
    public <T> LightFuture<T> thenApply(Function<R, T> f) throws LightExecutionException {
        return boundedThreadPool.submitDependent( () -> {
                    try {
                        R r = this.get();
                        return f.apply(r);
                    } catch (LightExecutionException e) {
                        throw new RuntimeException(e);
                    }
                },
                this
        );
    }

    private final Object gotResultEvent = new Object();
    private boolean isFailed = false;
    private Exception causeOfFail = null;
    private boolean ready = false;
    private R result;
    private final ThreadPoolImpl boundedThreadPool;
}
