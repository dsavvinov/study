/**
 * Created by dsavv on 20.03.2016.
 */
public interface Predicate<A> extends Function1<A, Boolean> {
    Boolean apply(A arg);

    default <A2> Function2<A, A2, Boolean> or(Predicate <A2> other) {
        return (arg1, arg2) -> {
            if (this.apply(arg1)) {
                return Boolean.TRUE;
            }
            return other.apply(arg2);
        };
    }

    default <A2> Function2<A, A2, Boolean> and(Predicate <A2> other) {
        return (arg1, arg2) -> {
            if (!this.apply(arg1)) {
                return Boolean.FALSE;
            }
            return other.apply(arg2);
        };
    }

    default Predicate<A> not () {
        return arg -> ! this.apply(arg);
    }

    Predicate ALWAYS_TRUE = arg -> Boolean.TRUE;

    Predicate ALWAYS_FALSE = arg -> Boolean.FALSE;
}
