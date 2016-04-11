package ru.spbau.mit.dsavvinov;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by dsavv on 20.03.2016.
 */
public class Function1Test {

    @Test
    public void testCompose() throws Exception {
        // trivial test-case
        Function1<Character, String > f           = arg -> arg.toString() + arg.toString();
        Function1<String,    Integer> g           = arg -> arg.length();
        Function1<Character, Integer> composition = f.compose(g);
        Assert.assertEquals((long) 2, (long) composition.apply('c'));
        Assert.assertEquals((long) 2, (long) composition.apply('z'));

        // correct casting of superclasses
        Function1<String, Integer> u = arg -> arg.length();
        Function1<Number, String> r = arg -> arg.toString();
        Function1 <String, String> comp = u.compose(r);
        assertEquals("4", comp.apply("test"));
        assertEquals("13", comp.apply("long sentence"));
        assertEquals("0", comp.apply(""));
    }
}