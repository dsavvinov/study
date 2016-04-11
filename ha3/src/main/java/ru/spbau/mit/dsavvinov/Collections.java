package ru.spbau.mit.dsavvinov;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.*;

/**
 * Created by dsavv on 20.03.2016.
 */
public class Collections {
    public static <T, R> ArrayList<R> map (Iterable <T> collection, Function1<? super T, R> f) {
        ArrayList<R> result = new ArrayList<R>();
        for (T item : collection) {
            result.add(f.apply(item));
        }
        return result;
    }

    public static <T> ArrayList<T> filter (Iterable <T> collection, Predicate<? super T> pred) {
        ArrayList<T> result = new ArrayList<>();
        for (T item : collection) {
            if (pred.apply(item)) {
                result.add(item);
            }
        }
        return result;
    }

    public static <T> ArrayList<T> takeWhile (Iterable <T> collection, Predicate<? super T> pred) {
        ArrayList<T> result = new ArrayList<>();
        for (T item : collection) {
            if (!pred.apply(item)) {
                return result;
            }
            result.add(item);
        }
        return result;
    }

    public static <T> ArrayList<T> takeUnless (Iterable <T> collection, Predicate<? super T> pred) {
        return takeWhile(collection, pred.not());
    }

    public static <A, B> B foldr (Function2<? super A, B, B> f, B acc, Iterable <A> collection) {
        Iterator<A> iterator = collection.iterator();
        return foldrHelper (f, acc, iterator);
    }
    public static <A, B> B foldl (Function2<B, ? super A, B> f, B acc, Iterable <A> collection) {
        for (A item : collection) {
            acc = f.apply(acc, item);
        }
        return acc;
    }

    private static <A, B> B foldrHelper (Function2 <? super A, B, B> f, B acc, Iterator <A> iterator) {
        if (!iterator.hasNext()) {
            return acc;
        }
        return f.apply(iterator.next(), foldrHelper (f, acc, iterator));
    }
}
