import org.junit.Test;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class TSTNodeTest {
    @Test
    public void insert() throws Exception {
        TST t = new TST();
        t.insert("sansa", 20);
        t.insert("sandor", 10);
        t.insert("sam", 15);
        t.insert("bran", 10);
        t.insert("bronn", 50);
        assertEquals((Character) 's', t.getRoot().getChildren()[1].getChar());
        ArrayList<TST.TSTNode> traversal = t.breadthTraversal();
        assertEquals(17, traversal.size());
        ArrayList<String> allNodes = traversal.stream().map(n -> "Char: " + n.getChar()
                + ", Val: " + n.getVal()
                + " , Max: " + n.getMax() + "\n")
                .collect(Collectors.toCollection(ArrayList::new));
        System.out.println(allNodes);

    }

    @Test
    public void find() throws Exception {
        TST t = new TST();
        t.insert("sansa", 20);
        t.insert("sansar", 20.1);
        t.insert("sandor", 10);
        t.insert("sam", 15);
        t.insert("sammy", 14.5);
        t.insert("bran", 50);
        t.insert("bronn", 20);
        t.insert("arya", 100);
        t.insert("cersei", 3);
        t.insert("cameron", 33);
        t.insert("melany", 527);
        assertTrue("cameron".equals(t.find("cam")));
        assertTrue("arya".equals(t.find("arya")));
        assertTrue("sansar".equals(t.find("san")));
        assertTrue("sam".equals(t.find("sam")));
        assertTrue("melany".equals(t.find("mel")));


    }

    @Test
    public void find1() throws Exception {
        TST t = new TST();
        t.insert("sansa", 20);
        t.insert("sansar", 20.1);
        t.insert("sandor", 10);
        t.insert("sam", 15);
        t.insert("sammy", 14.5);
        t.insert("bran", 50);
        t.insert("bronn", 20);
        t.insert("arya", 100);
        t.insert("cersei", 3);
        t.insert("cameron", 33);
        t.insert("melany", 527);
        assertTrue("sansar".equals(t.find("s")));
        assertTrue("arya".equals(t.find("a")));
        assertTrue("cameron".equals(t.find("c")));
        assertTrue("melany".equals(t.find("m")));
        assertTrue("bran".equals(t.find("b")));
    }

    @Test
    public void weightOf() throws Exception {
        TST t = new TST();
        t.insert("sansa", 20);
        t.insert("sansar", 20.1);
        t.insert("sandor", 10);
        t.insert("sam", 15);
        t.insert("sammy", 14.5);
        t.insert("bran", 50);
        t.insert("bronn", 20);
        t.insert("arya", 100);
        t.insert("cersei", 3);
        t.insert("cameron", 33);
        t.insert("melany", 527);
        assertTrue(t.weightOf("sansa") == 20);
        assertTrue(t.weightOf("sam") == 15);
        assertTrue(t.weightOf("arya") == 100);
        assertTrue(t.weightOf("cameron") == 33);
        assertTrue(t.weightOf("melany") == 527);
        assertTrue(t.weightOf("gregor") == 0.0);
    }
}
