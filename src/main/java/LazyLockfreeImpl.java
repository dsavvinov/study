import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.function.Supplier;

public class LazyLockfreeImpl<T> implements Lazy<T> {
    private static AtomicReferenceFieldUpdater<LazyLockfreeImpl, Object> valueUpdater =
            AtomicReferenceFieldUpdater.newUpdater(LazyLockfreeImpl.class, Object.class, "value");

    private volatile T value = null;
    private Supplier<T> supplier;

    public LazyLockfreeImpl(Supplier<T> supp) {
        supplier = supp;
    }

    @Override
    public T get() {
        Supplier<T> tmpSupplier = supplier;
        while (supplier != null) {
            if (valueUpdater.compareAndSet(this, null, tmpSupplier.get())) {
                supplier = null;
            }
        }
        return value;
    }
}
