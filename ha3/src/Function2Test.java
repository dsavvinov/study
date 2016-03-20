import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by dsavv on 20.03.2016.
 */
public class Function2Test {

    @Test
    public void testCompose() throws Exception {
        // casting of superclass
        Function2<String, Integer, Integer > f = (c, times) -> c.length() * times;
        Function1<Number,    String> g = arg -> arg.toString();
        Function2<String, Integer, String> composition = f.compose(g);
        Assert.assertEquals("16", composition.apply("haha", 4));
    }

    @Test
    public void testBind1() throws Exception {
        Function2<Number, Number, String> f = (arg1, arg2) -> arg1.toString() + " + " + arg2.toString();
        Assert.assertEquals("42 + 17", f.bind1(42).apply(17));
        Assert.assertEquals("42 + 36", f.bind1(42).apply(36));
    }

    @Test
    public void testBind2() throws Exception {
        Function2<Number, Number, String> f = (arg1, arg2) -> arg1.toString() + " + " + arg2.toString();
        Assert.assertEquals("17 + 42", f.bind2(42).apply(17));
        Assert.assertEquals("36 + 42", f.bind2(42).apply(36));
    }

    @Test
    public void testCurry() throws Exception {
        Function2<Number, Number, String> f = (arg1, arg2) -> arg1.toString() + " + " + arg2.toString();
        Function1<Number, Function1 <Number, String> > curried = f.curry();
        Assert.assertEquals(f.bind1(42).apply(17), curried.apply(42).apply(17));
    }
}