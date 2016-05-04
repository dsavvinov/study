package ru.spbau.mit;

import org.junit.Test;
import sun.nio.ch.ThreadPool;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.Assert.*;

/**
 * Created by dsavv on 01.05.2016.
 */
public class ThreadPoolImplTest {

    class SumTask implements Supplier<Long> {
        private final int n;

        SumTask(int n) {
            this.n = n;
        }

        @Override
        public Long get() {
            long res = 0;
            for (int i = 0; i <= n; i++) {
                res += i;
                try {
                    Thread.sleep(10);
                }
                catch (InterruptedException e) {
                    // we don't care if on some iteration some thread will sleep less than 10ms, so we ignore it
                }
            }
            return res;
        }
    }

    @Test
    public void testSimple() throws LightExecutionException{
        ThreadPoolImpl tp = new ThreadPoolImpl(2);
        LightFuture<Long> one = tp.submit(new SumTask(100));
        LightFuture<Long> two = tp.submit(new SumTask(500));
        LightFuture<Long> three = tp.submit(new SumTask(1));
        try { Thread.sleep(100); } catch (InterruptedException ignored) {}
        assertFalse(one.isReady());
        assertFalse(two.isReady());
        assertFalse(three.isReady());

        Long ans1 = one.get();
        assertTrue("Job1 is ready after get()", one.isReady());
        assertEquals(5050, (long) ans1);
        assertFalse("Job2 isn't ready after Job1.get()", two.isReady());
        assertFalse("Job3 isn't ready after Job1.get()", three.isReady());

        Long ans3 = three.get();
        assertTrue(one.isReady());
        assertTrue(three.isReady());
        assertEquals(1, (long) ans3);
        assertFalse(two.isReady());

        Long ans2 = two.get();
        assertTrue(one.isReady());
        assertTrue(two.isReady());
        assertTrue(three.isReady());
        assertEquals(125250, (long) ans2);
    }

    @Test(expected = LightExecutionException.class)
    public void testExceptionThrow() throws LightExecutionException {
        ThreadPoolImpl tp = new ThreadPoolImpl(1);
        LightFuture<Long> one = tp.submit((Supplier<Long>) () -> {
            throw new IndexOutOfBoundsException();
        });

        one.get();
        tp.shutdown();
    }

    @Test
    public void testHighLoad() throws LightExecutionException {
        ThreadPoolImpl tp = new ThreadPoolImpl(50);
        final int tasksAmount = 100;
        List<LightFuture<Long>> futures = new ArrayList<>();
        for (int i = 0; i < tasksAmount; i++) {
            final int cur = i;
            futures.add(tp.submit(new SumTask(50)));
        }

        for (int i = 0; i < tasksAmount; i++) {
            assertEquals(1275, (long) futures.get(i).get());
        }
    }

    @Test
    public void testAndThen() throws LightExecutionException {
        ThreadPoolImpl tp = new ThreadPoolImpl(2);
        final StringBuilder result = new StringBuilder();
        LightFuture<Void> one = tp.submit(() -> {
            try { Thread.sleep(200); } catch (InterruptedException ignored) { }
            result.append(1);
            return null;
        });

        LightFuture<Void> two = tp.submit(() -> {
            try { Thread.sleep(500); } catch (InterruptedException ignored) { }
            result.append(2);
            return null;
        });

        LightFuture<Void> three = one.thenApply(aVoid -> {
            result.append(3);
            return null;
        });

        LightFuture<Void> four = three.thenApply(aVoid -> {
            try { Thread.sleep(500); } catch (InterruptedException ignored) { }
            result.append(4);
            return null;
        });
        one.get();
        two.get();
        three.get();
        four.get();

        assertEquals("1324", result.toString());
    }

    @Test(expected = LightExecutionException.class)
    public void testAndThenThrow() throws LightExecutionException{
        ThreadPoolImpl tp = new ThreadPoolImpl(2);

        LightFuture<Integer> one = tp.submit(() -> {
            throw new IndexOutOfBoundsException();
        });

        LightFuture<String> two = one.thenApply(Object::toString);
        two.get();
    }
}