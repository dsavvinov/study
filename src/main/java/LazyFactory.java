import java.util.function.Supplier;

/**
 * Created by dsavv on 07.09.2016.
 */
public class LazyFactory {
    public static <T> Lazy<T> getSingleThreadLazy(Supplier<T> supplier) {
        return new LazySingleThreadImpl<T>(supplier);
    }

    public static <T> Lazy<T> getMultithreadLazy(final Supplier<T> supp) {
        return new LazyMultithreadImpl<T>(supp);
    }

    public static <T> Lazy<T> getLockfreeLazy(final Supplier<T> supp) {
        return new LazyLockfreeImpl<T>(supp);
    }
}
