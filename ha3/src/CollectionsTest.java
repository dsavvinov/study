import org.junit.Assert;

import java.util.ArrayList;
import java.util.Arrays;

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
        ArrayList<Integer> arr = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        ArrayList<Integer> res = Collections.map(arr, arg -> arg * arg);
        ArrayList<Integer> ans = new ArrayList<>(Arrays.asList(1, 4, 9, 16, 25));
        Assert.assertEquals(ans, res);

        // empty array
        arr = new ArrayList<>();
        res = Collections.map (arr, arg -> arg * arg);
        ans = new ArrayList<>();
        Assert.assertEquals(ans, res);
    }

    @org.junit.Test
    public void testFilter() throws Exception {
        // trivial test-case
        ArrayList<Integer> arr = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 7, 10));
        ArrayList<Integer> res = Collections.filter(arr, arg -> arg % 2 == 0);
        ArrayList<Integer> ans = new ArrayList<>(Arrays.asList(2, 4, 10));
        Assert.assertEquals(ans, res);

        /* filter all */
        res = Collections.filter(arr, Predicate.ALWAYS_FALSE);
        ans = new ArrayList<>();
        Assert.assertEquals(ans, res);

        /* filter nothing */
        res = Collections.filter(arr, Predicate.ALWAYS_TRUE);
        Assert.assertEquals(arr, res);

        /* filter empty collection */
        arr = new ArrayList<>();
        res = Collections.filter(arr, Predicate.ALWAYS_TRUE);
        ans = new ArrayList<>();
        Assert.assertEquals(ans, res);
    }

    @org.junit.Test
    public void testTakeWhile() throws Exception {
        ArrayList<Integer> arr = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 7, 10));
        ArrayList<Integer> res = Collections.takeWhile(arr, arg -> arg < 5);
        ArrayList<Integer> ans = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        Assert.assertEquals(ans, res);
    }

    @org.junit.Test
    public void testTakeUnless() throws Exception {
        ArrayList<Integer> arr = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 7, 10));
        ArrayList<Integer> res = Collections.takeUnless(arr, arg -> arg == 7);
        ArrayList<Integer> ans = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        Assert.assertEquals(ans, res);
    }

    @org.junit.Test
    public void testFoldr() throws Exception {
        /* trivial test-case */
        ArrayList<Integer> arr = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 7, 10));
        Function2<Integer, Integer, Integer> f = (arg, acc) -> acc + arg;
        Integer ini = 0;
        Integer res = Collections.foldr(f, ini, arr);
        Integer ans = 32;
        Assert.assertEquals(ans, res);

        /* correctness of associativity */
        arr = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        f   = (arg, acc) -> acc * 2 + arg;
        ini = 1;
        res = Collections.foldr(f, ini, arr);
        ans = 65;
        Assert.assertEquals(ans, res);

        /* type-check */
        ArrayList <String> arr2 = new ArrayList<>(Arrays.asList("one", "two", "three"));
        Function2<String, Integer, Integer> g = (str, cnt) -> str.length() + cnt;
        ini = 0;
        res = Collections.foldr(g, ini, arr2);
        ans = 11;
        Assert.assertEquals(ans, res);
    }

    @org.junit.Test
    public void testFoldl() throws Exception {
        /* trivial test-case */
        ArrayList<Integer> arr = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 7, 10));
        Function2<Integer, Integer, Integer> f = (acc, arg) -> acc + arg;
        Integer ini = 0;
        Integer res = Collections.foldl(f, ini, arr);
        Integer ans = 32;
        Assert.assertEquals(ans, res);

        /* correctness of associativity */
        arr = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        f   = (acc, arg) -> acc * 2 + arg;
        ini = 1;
        res = Collections.foldl(f, ini, arr);
        ans = 42;
        Assert.assertEquals(ans, res);

        /* type-check */
        ArrayList <String> arr2 = new ArrayList<>(Arrays.asList("one", "two", "three"));
        Function2<Integer, String, Integer> g = (cnt, str) -> str.length() + cnt;
        ini = 0;
        res = Collections.foldl(g, ini, arr2);
        ans = 11;
        Assert.assertEquals(ans, res);
    }
}