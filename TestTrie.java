import ucb.junit.textui;
import org.junit.Test;
import static org.junit.Assert.*;

/** The suite of all JUnit tests for the Trie class.
 *  @authors Keeley Takimoto, Cameron W.J. Abrams
 */
public class TestTrie {


    @Test
    public void basicFindTest() {
        // Some terms taken from CS61BL Summer 2017 Project 3 spec
        Trie t = new Trie();
        t.insert("hello");
        t.insert("world");
        t.insert("hey");
        t.insert("goodbye");
        t.insert("flipflops");
        assertEquals(true, t.find("hello", true));
        assertEquals(true, t.find("world", true));
        assertEquals(true, t.find("hey", true));
        assertEquals(true, t.find("goodbye", true));
        assertEquals(true, t.find("flipflops", true));
        assertEquals(true, t.find("hello", false));
        assertEquals(false, t.find("worl", true));
        assertEquals(false, t.find("he", true));
        assertEquals(false, t.find("new", false));
        assertEquals(false, t.find("flipfloops", true));
    }

    @Test
    public void basicTest2() {
        Trie t = new Trie();
        t.insert("hello");
        t.insert("hey");
        t.insert("goodbye");
        assertEquals(true, t.find("hell", false));
        assertEquals(true, t.find("hello", true));
        assertEquals(true, t.find("good", false));
        assertEquals(false, t.find("bye", false));
        assertEquals(false, t.find("heyy", false));
        assertEquals(false, t.find("hell", true));
    }

    @Test
    public void findTest() {
        Trie t = new Trie();
        t.insert("*%2");
        t.insert("12321");
        t.insert("c");
        t.insert("b");
        t.insert("baloo");
        t.insert("hello world!");
        t.insert("ç˚ß∆∂ƒ˚∆ƒß¬å∆˚¬˜å¬");
        assertEquals(true, t.find("*", false));
        assertEquals(true, t.find("c", true));
        assertEquals(true, t.find("c", false));
        assertEquals(false, t.find("*&2", true));
        assertEquals(true, t.find("*%2", true));
        assertEquals(true, t.find("ç˚ß∆∂ƒ˚∆ƒß¬å∆˚¬˜å¬", true));
        assertEquals(false, t.find("ç˚ß∆∂ƒ˚∆ƒß¬å∆˚¬˜å", true));
        assertEquals(false, t.find("", true));

    }

    @Test
    public void insertTest() {
        Trie testTrie = new Trie();
        String[] badInserts = new String[] {null, ""};
        for (String s: badInserts) {
            try {
                testTrie.insert(s);
                fail();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        String[] okInserts = new String[] {" ", "0", "1234", "9876", "abc", "ABC", "?><}:",
                                           "Never g0nn@ g!ve y0u up"};
        for (String s: okInserts) {
            try {
                testTrie.insert(s);
            } catch (IllegalArgumentException e) {
                fail();
            }
        }
    }

    /** Run the JUnit tests above. */
    public static void main(String[] ignored) {
        textui.runClasses(TestTrie.class);
    }
}
