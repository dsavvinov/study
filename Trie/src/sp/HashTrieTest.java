package sp;

import org.junit.Assert;
import sp.HashTrie;

/**
 * Created by dsavv on 23.02.2016.
 */
public class HashTrieTest {

    @org.junit.Test
    public void testRemove() throws Exception {
        HashTrie t = fillTrie();
        Assert.assertTrue(t.remove("Something"));
        Assert.assertFalse(t.remove("Something"));
        Assert.assertTrue(t.remove("Somq"));
        Assert.assertFalse(t.remove("Wrong"));
    }

    @org.junit.Test
    public void testAdd() throws Exception {
        HashTrie t = new HashTrie();
        Assert.assertTrue(t.add("Something"));
        Assert.assertTrue(t.add("Someone"));
        Assert.assertTrue(t.add("Somq"));
        Assert.assertTrue(t.add("#$%^&_)(*&"));
        Assert.assertTrue(t.add(""));
        Assert.assertFalse(t.add("Someone"));
        Assert.assertFalse(t.add("#$%^&_)(*&"));
    }

    @org.junit.Test
    public void testContains() throws Exception {
        HashTrie t = fillTrie();
        Assert.assertTrue(t.contains("Something"));
        Assert.assertTrue(t.contains("#$%^&_)(*&"));
        Assert.assertTrue(t.contains(""));
        Assert.assertFalse(t.contains("Wrong"));
    }

    @org.junit.Test
    public void testSize() throws Exception {
        HashTrie t = fillTrie();
        Assert.assertEquals(5, t.size());
        t.remove("Something");
        Assert.assertEquals(4, t.size());
        t.add("Another word");
        Assert.assertEquals(5, t.size());
    }

    @org.junit.Test
    public void testHowManyStartsWithPrefix() throws Exception {
        HashTrie t = fillTrie();
        Assert.assertEquals(5, t.howManyStartsWithPrefix(""));
        Assert.assertEquals(2, t.howManyStartsWithPrefix("Some"));
        Assert.assertEquals(3, t.howManyStartsWithPrefix("Som"));
        Assert.assertEquals(1, t.howManyStartsWithPrefix("#$"));
    }

    private HashTrie fillTrie() {
        HashTrie t = new HashTrie();
        t.add("Something");
        t.add("Someone");
        t.add("Somq");
        t.add("#$%^&_)(*&");
        t.add("");
        return t;
    }
}