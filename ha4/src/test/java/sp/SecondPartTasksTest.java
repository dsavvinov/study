package sp;

import static org.junit.Assert.*;
import org.junit.Test;
import static sp.SecondPartTasks.*;

import java.util.*;
import java.util.stream.DoubleStream;


/**
 * Created by dsavv on 10.04.2016.
 */

public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() throws Exception {
        List<String> paths = Arrays.asList(
                "src/test/resources/text1.txt",
                "src/test/resources/text2.txt",
                "src/test/resources/text3.txt",
                "src/test/resources/text4.txt"
        );

        List<String> result1 = Arrays.asList(
                "Gotta make you understand",
                "Gotta make you understand"
        );

        List<String> result2 = Arrays.asList(
                "Завтра ищешь в интернете книжку Java, A Beginner's Guide. Ничего страшного, если ничего не поймешь."
        );

        assertEquals(result1, findQuotes(paths, "understand"));
        assertEquals(result2, findQuotes(paths, "Guide"));
    }

    @Test
    public void testPiDividedBy4() throws Exception {
        assertEquals(
                Math.PI/4,
                DoubleStream.generate(SecondPartTasks::piDividedBy4).limit(1000).average().orElse(0.0),
                0.001);
    }

    @Test
    public void testFindPrinter() throws Exception {
        Map<String, List<String>> authors = new HashMap<String, List<String>>() {{
            put("Author1", Arrays.asList("1", "2", "3"));
            put("Author2", Arrays.asList("22", "1"));
            put("Author3", Arrays.asList("3334"));
        }};

        assertEquals("Author3", findPrinter(authors));

        assertEquals(null, findPrinter(Collections.emptyMap()));
    }

    @Test
    public void testCalculateGlobalOrder() throws Exception {
        Map<String, Integer> shop1 = new HashMap<String, Integer>() {{
            put("item1", 300);
            put("item2", 50);
            put("item3", 30);
            put("item4", 150);
        }};

        Map<String, Integer> shop2 = new HashMap<String, Integer>() {{
            put("item2", 20);
            put("item4", 15);
            put("item5", 90);
            put("item6", 300);
        }};

        Map<String, Integer> shop3 = new HashMap<String, Integer>() {{
            put("item5", 70);
            put("item1", 50);
            put("item10", 90);
            put("item2", 30);
        }};

        Map<String, Integer> result = new HashMap<String, Integer>() {{
            put("item1", 350);
            put("item2", 100);
            put("item3", 30);
            put("item4", 165);
            put("item5", 160);
            put("item6", 300);
            put("item10", 90);
        }};

        assertEquals(result,
                calculateGlobalOrder(Arrays.asList(shop1, shop2, shop3))
        );

        assertEquals(Collections.emptyMap(),
                Collections.emptyMap());
    }
}