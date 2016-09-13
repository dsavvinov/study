import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;

import static org.junit.Assert.*;

public class LazyFactoryTest {
    private class TestTask implements Supplier<String> {
        private String value;
        public volatile int calls = 0;

        public TestTask(String val) {
            value = val;
        }

        @Override
        public String get() {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) { }
            calls += 1;
            if (value != null) {
                return new String(value);
            }
            return null;
        }
    }

    // Returns amount of calls made to test object
    private void testLazy (Lazy<String> lazy, int threadPoolSize) throws ExecutionException {
        ExecutorService tp = Executors.newFixedThreadPool(threadPoolSize);
        List<Future<String>> futures = new ArrayList<>();

        for (int i = 0; i < 4 * threadPoolSize; i += 1) {
            futures.add(tp.submit(lazy::get));
        }

        String referenceValue = null;

        for (int i = 0; i < 4 * threadPoolSize; i += 1) {
            String currentValue = null;
            while (!futures.get(i).isDone()) { }
            try {
                currentValue = futures.get(i).get();
            }
            catch (InterruptedException ignored) { }    // Spurious wake-ups will be handled by the while-loop

            if (i == 0) {
                referenceValue = currentValue;
            }
            assertSame(referenceValue, currentValue);
        }
    }

    @Test
    public void testGetSingleThreadLazy() throws Exception {
        // explicit call of string constructor to prevent caching
        String referenceString = new String("Single");
        TestTask tt = new TestTask(referenceString);
        Lazy<String> lazySingle = LazyFactory.getSingleThreadLazy(tt);

        assertEquals(0, tt.calls);
        testLazy(lazySingle, /* threadPoolSize = */ 1);
        assertEquals(1, tt.calls);
    }

    @Test
    public void testGetMultithreadLazy() throws Exception {
        // explicit call of string constructor to prevent caching
        String referenceString = new String("Multi");
        TestTask tt = new TestTask(referenceString);
        Lazy lazyMulti = LazyFactory.getMultithreadLazy(tt);

        assertEquals(0, tt.calls);
        testLazy(lazyMulti, /* threadPoolSize = */ 50);
        assertEquals(1, tt.calls);
    }

    @Test
    public void testGetLockfreeLazy() throws Exception {
        // explicit call of string constructor to prevent caching
        String referenceString = new String("Lockfree");
        TestTask tt = new TestTask(referenceString);
        Lazy lazyLock = LazyFactory.getLockfreeLazy(tt);

        assertEquals(0, tt.calls);
        testLazy(lazyLock, /* threadPoolSize = */ 50);
        assertTrue("Get wasn't called even once", tt.calls > 0);
    }

    @Test
    public void testNullableLockfreeLazy () throws Exception {
        String referenceString = null;
        TestTask tt = new TestTask(referenceString);
        Lazy lazyLock = LazyFactory.getLockfreeLazy(tt);

        assertEquals(0, tt.calls);
        testLazy(lazyLock, /* threadPoolSize = */ 50);
        assertTrue("Get wasn't called even once", tt.calls > 0);
    }

    @Test
    public void testNullableMultithreadLazy () throws Exception {
        String referenceString = null;
        TestTask tt = new TestTask(referenceString);
        Lazy multiLock = LazyFactory.getMultithreadLazy(tt);

        assertEquals(0, tt.calls);
        testLazy(multiLock, /* threadPoolSize = */ 50);
        assertEquals(1, tt.calls);
    }

    @Test
    public void testNullableSinglethreadLazy() throws Exception {
        String referenceString = null;
        TestTask tt = new TestTask(referenceString);
        Lazy singleLock = LazyFactory.getSingleThreadLazy(tt);

        assertEquals(0, tt.calls);
        testLazy(singleLock, /* threadPoolSize = */ 1);
        assertEquals(1, tt.calls);
    }
}