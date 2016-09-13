import java.util.Optional;
import java.util.function.Supplier;

public class LazySingleThreadImpl<T> implements Lazy<T> {
    private T cachedValue = null;
    private Supplier<T> supplier;

    public LazySingleThreadImpl(Supplier<T> supp) {
        supplier = supp;
    }

    @Override
    public T get() {
        if (supplier != null) {
            cachedValue = supplier.get();
            supplier = null;
        }
        return cachedValue;
    }
}
