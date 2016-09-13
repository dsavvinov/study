import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class LazyMultithreadImpl<T> implements Lazy<T> {
    private T cachedValue = null;
    private Supplier<T> supplier;

    public LazyMultithreadImpl(Supplier<T> supp) {
        supplier = supp;
    }

    @Override
    synchronized public T get() {
        if (supplier != null) {
            cachedValue = supplier.get();
            supplier = null;
        }
        return cachedValue;
    }
}
