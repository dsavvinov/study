package ru.spbau.mit.dsavvinov;

import org.junit.Assert;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by dsavv on 20.03.2016.
 */
public class CollectionsTest {

    @org.junit.Test
    public void testMap() throws Exception {
        /*  Здесь я проверяю один и тот же метод map, но несколькими различными тестовыми случаями.
            Нужно ли выносить куда-нибудь отдельно каждый тестовый случай, чтобы, допустим, когда у меня
            падает какой-то тестовый метод, я сразу понимал, какой именно тест-кейз отработал неверно?
         */
        /* some trivial array and function */
        assertEquals(
                Arrays.asList(1, 4, 9, 16, 25),
                Collections.map(
                        Arrays.asList(1, 2, 3, 4, 5),
                        arg -> arg * arg
                )
        );

        // empty array
        assertEquals(java.util.Collections.emptyList(), java.util.Collections.emptyList());

        // wildcard test
        assertEquals(
                Arrays.asList("1", "2", "3"),
                Collections.map(
                        Arrays.asList(1, 2, 3),
                        (Object arg) -> arg.toString()
                )
        );
    }

    @org.junit.Test
    public void testFilter() throws Exception {
        // trivial test-case
        assertEquals(
                Arrays.asList(2, 4, 10),
                Collections.filter(
                        Arrays.asList(1, 2, 3, 4, 5, 7, 10),
                        arg1 -> arg1 % 2 == 0)
        );

        /* filter all */
        assertEquals(
                java.util.Collections.emptyList(),
                Collections.filter(
                        Arrays.asList(1, 2, 3, 4, 5, 7, 10),
                        Predicate.ALWAYS_FALSE)
        );

        /* filter nothing */
        assertEquals(
                Arrays.asList(1, 2, 3, 4, 5, 7, 10),
                Collections.filter(
                        Arrays.asList(1, 2, 3, 4, 5, 7, 10),
                        Predicate.ALWAYS_TRUE)
        );

        /* filter empty collection */
        assertEquals(java.util.Collections.emptyList(), java.util.Collections.emptyList());

        // wildcard test
        assertEquals(
                Arrays.asList(2),
                Collections.filter(
                        Arrays.asList(1, 2, 3),
                        (Predicate<Object>) (Object arg) -> arg.toString().equals("2")
                )
        );
    }

    @org.junit.Test
    public void testTakeWhile() throws Exception {
        assertEquals(
                Arrays.asList(1, 2, 3, 4),
                Collections.takeWhile(
                        Arrays.asList(1, 2, 3, 4, 5, 7, 10),
                        arg -> arg < 5
                )
        );
    }

    @org.junit.Test
    public void testTakeUnless() throws Exception {
        assertEquals(
                Arrays.asList(1, 2, 3, 4, 5),
                Collections.takeUnless(
                        Arrays.asList(1, 2, 3, 4, 5, 7, 10),
                        arg -> arg == 7
                )
        );
    }

    @org.junit.Test
    public void testFoldr() throws Exception {
        /* trivial test-case */
        assertEquals(
                (Integer) 32,
                Collections.foldr(
                        (arg2, acc2) -> acc2 + arg2,
                        0,
                        Arrays.asList(1, 2, 3, 4, 5, 7, 10)
                )
        );

        /* correctness of associativity */
        assertEquals(
                (Integer) 65,
                Collections.foldr(
                        (arg1, acc1) -> acc1 * 2 + arg1,
                        1,
                        Arrays.asList(1, 2, 3, 4)
                )
        );

        /* type-check */
        assertEquals(
                (Integer) 11,
                Collections.foldr(
                        (str, cnt) -> str.length() + cnt,
                        0,
                        Arrays.asList("one", "two", "three")
                )
        );

        // wildcard test
        assertEquals(
                "4321",
                Collections.foldr(
                        (Object arg, String acc) -> acc + arg.toString(),
                        "",
                        Arrays.asList(1, 2, 3, 4)
                )
        );
    }

    @org.junit.Test
    public void testFoldl() throws Exception {
        /* trivial test-case */
        assertEquals(
                (Integer) 32,
                Collections.foldl(
                        (acc2, arg2) -> acc2 + arg2,
                        0,
                        Arrays.asList(1, 2, 3, 4, 5, 7, 10)
                )
        );

        /* correctness of associativity */
        assertEquals(
                (Integer) 42,
                Collections.foldl(
                        (acc1, arg1) -> acc1 * 2 + arg1,
                        1,
                        Arrays.asList(1, 2, 3, 4)
                )
        );

        /* type-check */
        assertEquals(
                (long) 11,
                (long) Collections.foldl(
                        (cnt, str) -> str.length() + cnt,
                        0,
                        Arrays.asList("one", "two", "three")
                )
        );

        // wildcard test
        assertEquals(
                "1234",
                Collections.foldl(
                        (String acc, Object arg) -> acc + arg.toString(),
                        "",
                        Arrays.asList(1, 2, 3, 4)
                )
        );
    }
}