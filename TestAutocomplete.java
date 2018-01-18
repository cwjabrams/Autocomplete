import ucb.junit.textui;
import org.junit.Test;


import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import static org.junit.Assert.*;

/** The suite of all JUnit tests for the Autocomplete class.
 *  @author
 */
public class TestAutocomplete {

    @Test
    public void testConstructorExceptions() {
        String[] terms = new String[] {"I'm", "never", "gonna", "dance", "again",
            "guilty", "feet", "have", "got", "no", "rhythm"};
        double[] weights = new double[terms.length];
        Random r = new Random();
        for (int i =  0; i < weights.length; i++) {
            weights[i] = Math.abs(r.nextDouble() * 1000.0);
        }
        double[] longWeights = Arrays.copyOf(weights, terms.length + 1);
        longWeights[longWeights.length - 1] = Math.abs(r.nextDouble() * 1000.0);
        double[] shortWeights = Arrays.copyOf(weights, terms.length - 1);
        double[] negWeights = Arrays.copyOf(weights, terms.length);
        negWeights[negWeights.length - 1] = -100.0;
        String[] duplicateTerms = new String[] {"Wake", "me", "up", "before", "you", "go", "go"};
        try {
            new Autocomplete(terms, longWeights);
            new Autocomplete(terms, shortWeights);
            new Autocomplete(terms, negWeights);
            new Autocomplete(duplicateTerms, weights);
            fail();
        } catch (IllegalArgumentException e) {
            e.getMessage();
        }
        try {
            new Autocomplete(terms, weights);
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void testTopMatch() {
        String[] terms = new String[] {"bubbles", "blossom", "buttercup", "dexter",
                                       "didi", "cow", "chicken", "chick", "ed", "edd"};
        double[] weights = new double[] {8.0, 5.0, 9.8, 6.2, 7.4, 1.4, 10.0, 9.3, 5.1, 4.1};
        Autocomplete a = new Autocomplete(terms, weights);

        //check for prefixes not matching any term in dictionary
        assertNull(a.topMatch("a"));
        assertNull(a.topMatch("bux"));
        assertNull(a.topMatch("cows"));

        //check empty String
        assertEquals("chicken", a.topMatch(""));

        //check prefixes with one match
        assertEquals("didi", a.topMatch("di"));
        assertEquals("buttercup", a.topMatch("but"));

        // check prefixes with >1 match
        assertEquals("buttercup", a.topMatch("b"));
        assertEquals("chicken", a.topMatch("c"));

        // check prefix where top match is exactly the prefix
        assertEquals("ed", a.topMatch("ed"));
    }

    @Test
    public void testTopMatches() {
        String[] terms = new String[] {"cheddar", "camembert", "brie", "colby jack", "goat",
                                          "gorgonzola", "gruyere", "green", "greenish"};
        double[] weights = new double[] {10.0, 20.0, 9.0, 8.0, 1.0, 7.1, 7.0, 30.4, 0.1};
        Autocomplete a = new Autocomplete(terms, weights);

        //check if topMatches correctly throws exceptions for k < 0
        try {
            a.topMatches("g", -1);
            fail();
        } catch (IllegalArgumentException e) {
            System.out.print(e.getMessage());
        }

        //check that matches are returned for the correct
        // prefix in the correct order
        Iterator<String> matchG = a.topMatches("g", 4).iterator();
        assertEquals("green", matchG.next());
        assertEquals("gorgonzola", matchG.next());
        assertEquals("gruyere", matchG.next());
        assertEquals("goat", matchG.next());
        assertTrue(!matchG.hasNext());

        Iterator<String> matchCH = a.topMatches("ch", 1).iterator();
        assertEquals("cheddar", matchCH.next());
        assertTrue(!matchCH.hasNext());

        //for prefix with > k matches, check that k are returned
        Iterator<String> matchG2 = a.topMatches("g", 2).iterator();
        assertEquals("green", matchG2.next());
        assertEquals("gorgonzola", matchG2.next());
        assertTrue(!matchG2.hasNext());

        //for prefix with < k matches, check that k are returned
        Iterator<String> matchB = a.topMatches("b", 25).iterator();
        assertEquals("brie", matchB.next());
        assertTrue(!matchB.hasNext());

        //check that empty prefix returns top weighted words
        Iterator<String> matchEmpty = a.topMatches("", 4).iterator();
        assertEquals("green", matchEmpty.next());
        assertEquals("camembert", matchEmpty.next());
        assertEquals("cheddar", matchEmpty.next());
        assertEquals("brie", matchEmpty.next());
        assertTrue(!matchEmpty.hasNext());

        // check term that is identical to prefix
        Iterator<String> matchColbyJack = a.topMatches("colby jack", 2).iterator();
        assertEquals("colby jack", matchColbyJack.next());
        assertTrue(!matchColbyJack.hasNext());

        //check term that is substring of other term
        Iterator<String> matchGreen = a.topMatches("green", 2).iterator();
        assertEquals("green", matchGreen.next());
        assertEquals("greenish", matchGreen.next());
        assertTrue(!matchGreen.hasNext());

        // terms and weights taken from example in CS61BL Project 3 spec, summer 2017
        String[] terms2 = new String[] {"smog", "buck", "sad", "spite", "spit", "spy"};
        double[] weights2 = new double[] {5.0, 10.0, 12.0, 20.0, 15.0, 7.0};
        Autocomplete a2 = new Autocomplete(terms2, weights2);
        Iterator<String> matchS = a2.topMatches("s", 3).iterator();
        assertEquals("spite", matchS.next());
        assertEquals("spit", matchS.next());
        assertEquals("sad", matchS.next());

    }

    @Test
    public void testWeightOf() {
        String[] terms = new String[] {"cheddar", "camembert", "brie", "colby jack", "goat",
                                       "gorgonzola", "gruyere", "green"};
        double[] weights = new double[] {10.0, 20.0, 9.0, 8.0, 1.0, 7.1, 7.0, 30.4};
        Autocomplete a = new Autocomplete(terms, weights);
        for (int i = 0; i < terms.length; i++) {
            assertTrue(a.weightOf(terms[i]) == weights[i]);
        }
        assertTrue(a.weightOf("velveeta") == 0.0);
        assertTrue(a.weightOf("c") == 0.0);
        assertTrue(a.weightOf("chedda") == 0.0);
        assertTrue(a.weightOf("") == 0.0);
    }



    /** Run the JUnit tests above. */
    public static void main(String[] ignored) {
        textui.runClasses(TestAutocomplete.class);
    }
}
