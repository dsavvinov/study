package ru.spbau.mit.dsavvinov;

/**
 * Created by dsavv on 20.03.2016.
 */
public interface Predicate<A> extends Function1<A, Boolean> {

    default Predicate<A> or(Predicate <? super A> other) {
        return arg1 -> apply(arg1) || other.apply(arg1);

    }

    default Predicate<A> and(Predicate <? super A> other) {
        return arg1 -> apply(arg1) && other.apply(arg1);
    }

    default Predicate<A> not () {
        return arg -> ! apply(arg);
    }

    Predicate<Object> ALWAYS_TRUE = arg -> Boolean.TRUE;

    Predicate<Object> ALWAYS_FALSE = arg -> Boolean.FALSE;
}
