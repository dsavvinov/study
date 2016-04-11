package ru.spbau.mit.dsavvinov;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by dsavv on 11.04.2016.
 */
public class PredicateTest {

    private class isOdd implements Predicate<Integer> {
        public int callsCounter = 0;

        @Override
        public Boolean apply(Integer arg) {
            callsCounter += 1;
            return (arg % 2) == 1;
        }
    }

    private class longerThanOneSymbol implements Predicate<Object> {
        public int callsCounter = 0;

        @Override
        public Boolean apply(Object arg) {
            callsCounter += 1;
            return arg.toString().length() > 1;
        }
    }
    @Test
    public void testOr() throws Exception {
        isOdd pr1 = new isOdd();
        longerThanOneSymbol pr2 = new longerThanOneSymbol();

        assertEquals(
                Arrays.asList(1,3,5,7,9,10),
                Collections.filter(
                        Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
                        pr1.or(pr2)
                )
        );

        assertEquals(10, pr1.callsCounter);
        assertEquals(5, pr2.callsCounter);
    }

    @Test
    public void testAnd() throws Exception {
        isOdd pr1 = new isOdd();
        longerThanOneSymbol pr2 = new longerThanOneSymbol();

        assertEquals(
                Arrays.asList(11),
                Collections.filter(
                        Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11),
                        pr1.and(pr2)
                )
        );

        assertEquals(11, pr1.callsCounter);
        assertEquals(6, pr2.callsCounter);
    }

    @Test
    public void testNot() throws Exception {
        assertEquals(
                Arrays.asList(2,4,6,8,10),
                Collections.filter(
                        Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11),
                        new isOdd().not()
                )
        );
    }
}