import java.util.HashMap;

/**
 * Prefix-Trie. Supports linear time find() and insert().
 * Should support determining whether a word is a full word in the
 * Trie or a prefix.
 *
 * @author
 */
public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }
    /** If isFullWord*/
    public boolean find(String s, boolean isFullWord) {
        /* YOUR CODE HERE. */
        return root.find(s, isFullWord);
    }

    public void insert(String s) throws IllegalArgumentException {
        root.insert(s);
    }

    public class TrieNode {
        /** A map of this node's children, where keys represent the next character
         * in a word. If children contains a null key, this node marks the end
         * of a word.
         */
        private HashMap<Character, TrieNode> children;

        public TrieNode() {
            children = new HashMap<Character, TrieNode>();
        }

        public void insert(String s) throws IllegalArgumentException {
            if (s == null || s.equals("")) {
                throw new IllegalArgumentException("Cannot add null or empty String.");
            }
            TrieNode currNode = this;
            for (int i = 0; i < s.length(); i++) {
                char currChar = s.charAt(i);
                HashMap currKids = currNode.getChildren();
                if (!currKids.containsKey(currChar)) {
                    currNode.addChild(currChar);
                    currNode = (TrieNode) currKids.get(currChar);
                } else {
                    currNode = (TrieNode) currKids.get(currChar);
                }
            }
            currNode.addChild(null);
        }

        public boolean find(String s, boolean isFullWord) {
            if (s.length() == 0) {
                return ((!isFullWord)
                    || (isFullWord && getChildren().containsKey(null)));
            } else {
                if (children.containsKey(s.charAt(0))) {
                    return children.get(s.charAt(0)).find(s.substring(1, s.length()), isFullWord);
                } else {
                    return false;
                }
            }
        }

        public HashMap<Character, TrieNode> getChildren() {
            return children;
        }

        public void addChild(Character c) {
            if (c != null) {
                children.put(c, new TrieNode());
            } else {
                children.put(c, null);
            }
        }
    }
}
