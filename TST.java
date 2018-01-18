
import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by Abrams on 8/2/17.
 */
public class TST {
    private TSTNode _root;

    public TST() {
        _root = new TSTNode();
        _root.setChildren(null, new TSTNode(), null);
    }

    /** Returns a string from this TST.
      * @return String the string with prefix and greatest weight.
      */
    public String find(String prefix) {
        TSTNode node = _root;
        for (int i = 0; i < prefix.length(); i++) {
            if (node.getChildren()[1] != null) {
                node = node.getChildren()[1].firstOccurrence(prefix.charAt(i));
                if (node == null) {
                    return null;
                }
            } else {
                return null;
            }
        }
        if (node.getMax().equals(node.getVal())) {
            return prefix;
        }
        String remainingCharacters = node.findMax();
        if (remainingCharacters.length() < 1) {
            return null;
        } else {
            return prefix + remainingCharacters;
        }
    }

    public double weightOf(String term) {
        TSTNode node = _root.getChildren()[1].firstOccurrence(term.charAt(0));
        for (int i = 1; i < term.length(); ++i) {
            if (node != null && node.getChildren()[1] != null) {
                node = node.getChildren()[1].firstOccurrence(term.charAt(i));
            } else {
                return 0.0;
            }
        }
        if (node == null || node.getVal() == null) {
            return 0.0;
        } else {
            return node.getVal();
        }
    }

     /** Returns a list of strings taken from this TST.
      * @param prefix
      * @param k
      * @return A list of K strings with given prefix and greatest K weights.
      */
    public Iterable<String> find(String prefix, int k) {
        ArrayList<String> result = new ArrayList<>();
        if (k == 1) {
            String match = find(prefix);
            if (match != null) {
                result.add(match);
            }
            return result;
        }
        TSTNode curr = _root;
        ArrayList<QueueEntry<Double>> bestAnswers = new ArrayList<>(k);
        ArrayList<QueueEntry<TSTNode>> possibleAnswers = new ArrayList<>(k);

        if (prefix.length() > 0) {
            for (int i = 0; i < prefix.length(); i++) {
                curr = curr.getChildren()[1].firstOccurrence(prefix.charAt(i));
                if (curr == null) {
                    return result;
                }
            }
        }
        if (curr.getVal() != null) {
            bestAnswers.add(new QueueEntry<>(prefix, curr.getVal()));
        }
        for (TSTNode n: curr.allNextCharacters()) {
            possibleAnswersHelper(possibleAnswers, n, k, "");
        }
        while (!possibleAnswers.isEmpty()) {
            QueueEntry<TSTNode> e = possibleAnswers.remove(0);
            curr = e.getValue();
            String soFar = e.getKey();
            Double currVal = curr.getVal();
            QueueEntry<Double> minBestAns = null;
            if (bestAnswers.size() > 0) {
                minBestAns = bestAnswers.get(bestAnswers.size() - 1);
            }
            if (bestAnswers.size() >= k && curr.getMax() <=  minBestAns.getValue()) {
                break;
            } else if (curr.getVal() != null) {
                String term = prefix + soFar + curr.getChar();
                if (bestAnswers.size() < k || currVal > minBestAns.getValue()) {
                    if (bestAnswers.size() >= k && currVal > minBestAns.getValue()) {
                        bestAnswers.set(bestAnswers.size() - 1, new QueueEntry(term, currVal));
                    } else {
                        bestAnswers.add(new QueueEntry(term, currVal));
                    }
                    int newSize = bestAnswers.size();
                    if (newSize > 1 && bestAnswers.get(newSize - 2).getValue() < currVal) {
                        bestAnswers.sort((a, b) -> b.getValue().compareTo(a.getValue()));
                    }
                }
            }
            for (TSTNode n : curr.allNextCharacters()) {
                possibleAnswersHelper(possibleAnswers, n, k, soFar + curr.getChar());
            }
        }
        return bestAnswers.stream()
                .map(TST.QueueEntry::getKey)
                .collect(Collectors.toList());
    }

    /** A helper method for FIND. Maintains invariants on POSSANS: size is less
     * than K, and elements therein have TSTNode values with the maximum MAX.
     * @param possAns A list of QueueEntries representing possible partial suffixes.
     * @param n A node representing a character in a possible suffix.
     * @param k The number of desired terms to return.
     * @param key A String of characters appearing before N in a suffix.
     */
    private void possibleAnswersHelper(ArrayList<QueueEntry<TSTNode>> possAns,
                                       TSTNode n, double k, String key) {
        int size = possAns.size();
        if (size < k || possAns.get(size - 1).getValue().getMax() < n.getMax()) {
            if (size >= k) {
                possAns.set(size - 1, new QueueEntry<>(key, n));
            } else {
                possAns.add(new QueueEntry<>(key, n));
            }
            int newSize = possAns.size();
            if (newSize > 1  && possAns.get(newSize - 2).getValue().getMax() < n.getMax()) {
                possAns.sort((a, b) -> b.getValue().getMax().compareTo(a.getValue().getMax()));
            }
        }
    }

    /** An entry for a priority queue. Keys are terms or parts of terms
     * associated with values of type V.
     */
    class QueueEntry<V> extends AbstractMap.SimpleEntry<String, V> {
        QueueEntry(String s, V val) {
            super(s, val);
        }
    }


    /** Inserts a string S into this trie and returns true if successful.
     * If S is already in the trie, returns false.
     * @throws IllegalArgumentException if S is null or the empty String.
     */
    public boolean insert(String s, double weight) throws IllegalArgumentException {
        if (s == null || s.equals("")) {
            throw new IllegalArgumentException("Can't insert null or empty String.");
        }
        if (_root.getMax() == null || _root.getMax() < weight) {
            _root.setMax(weight);
        }
        TSTNode currNode = _root.getChildren()[1];
        for (int i = 0; i < s.length();) {
            Character currChar = currNode.getChar();
            Double currMax = currNode.getMax();
            TSTNode[] currKids = currNode.getChildren();
            Character toInsert = s.charAt(i);
            boolean isLast = i == s.length() - 1;

            if (currChar == null) {
                currNode.setChar(toInsert);
                currNode.setMax(weight);
                if (!isLast) {
                    currKids[1] = new TSTNode();
                    currNode = currKids[1];
                } else {
                    currNode.setVal(weight);
                }
                i++;
            } else if (currChar.equals(toInsert)) {
                if (currMax == null || currMax < weight) {
                    currNode.setMax(weight);
                }
                if (isLast) {
                    if (currNode.getVal() != null) {
                        return false;
                    } else {
                        currNode.setVal(weight);
                        return true;
                    }
                }
                if (currKids[1] == null) {
                    currKids[1] = new TSTNode();
                }
                currNode = currKids[1];
                i++;
            } else if (currChar < toInsert) {
                if (currKids[2] == null) {
                    currKids[2] = new TSTNode();
                }
                currNode = currKids[2];
            } else {
                if (currKids[0] == null) {
                    currKids[0] = new TSTNode();
                }
                currNode = currKids[0];
            }

        }
        currNode.setVal(weight);
        return true;
    }

    /** Returns a list of all TSTNodes currently in this TST in
     * breadth-first order.
     */
    public ArrayList<TSTNode> breadthTraversal() {
        ArrayList<TSTNode> result = new ArrayList<>();
        ArrayDeque<TSTNode> fringe = new ArrayDeque<TSTNode>();
        fringe.offer(_root);
        while (!fringe.isEmpty()) {
            TSTNode curr = fringe.poll();
            for (TSTNode t: curr.getChildren()) {
                if (t != null) {
                    fringe.offer(t);
                }
            }
            result.add(curr);
        }
        return result;
    }

    /** Returns the root node. */
    public TSTNode getRoot() {
        return _root;
    }


    public class TSTNode {
        /** A map of this node's children, where keys represent the next character
         * in a word. If children contains a null key, this node marks the end
         * of a word.
         */
        private TSTNode[] _children;
        /** The weight of a given term. Null if THIS does not represent
         * the end of a term.
         */
        private Double _val;
        /** The max weight of all terms in this node's subtries. */
        private Double _max;
        /** This node's character. */
        private Character _c;

        /** Construct an empty STSNode. */
        public TSTNode() {
            _children = new TSTNode[3];
        }

        /**
         * Returns a string made up of the trailing characters starting from this node
         * that lead to a maximum value.
         */
        public String findMax() {
            String result = "";
            double max = getMax();
            TSTNode currNode = this;
            while (currNode.getVal() == null || currNode.getVal() != max) {
                currNode = currNode.maxNextChar(max);
                if (currNode.getChar() != null) {
                    result += currNode.getChar();
                }
            }
            return result;
        }

        /** Return the TSTNode with the largest MAX at the level below
         * this node.
         */
        public TSTNode maxNextCharHelper(double max) {
            if (_max == max) {
                return this;
            } else {
                TSTNode result = null;
                if (_children[0] != null) {
                    result = _children[0].maxNextCharHelper(max);
                    if (result != null) {
                        return result;
                    }
                }
                if (_children[2] != null) {
                    result = _children[2].maxNextCharHelper(max);
                    if (result != null) {
                        return result;
                    }
                }
            }
            return null;
        }
        /**
         * @return The TSTNode child of this TSTNode with the largest max value.
         */
        public TSTNode maxNextChar(double max) {
            if (_children[1] != null) {
                return _children[1].maxNextCharHelper(max);
            }
            return null;
        }

        /**
         * Finds the TSTNode in this TST with the given character
         * and is the beginning of a word.
         * @param c A strings first character.
         * @return TSTNode with character representing the start of String.
         */
        private TSTNode firstOccurrence(Character c) {
            if (c.equals(_c)) {
                return this;
            } else if (c < _c) {
                if (getChildren()[0] == null) {
                    return null;
                }
                return getChildren()[0].firstOccurrence(c);
            } else {
                if (getChildren()[2] == null) {
                    return null;
                }
                return getChildren()[2].firstOccurrence(c);
            }
        }

        /** Return a list of all nodes with characters that follow the
         * current node's character. Returns an empty list if current
         * node is a leaf.
         */
        public ArrayList<TSTNode> allNextCharacters() {
            ArrayList<TSTNode> result = new ArrayList<>();
            result.addAll(allNextCharactersHelper(_children[1]));
            return result;
        }
        /** A helper method for allNextCharacters. */
        public ArrayList<TSTNode> allNextCharactersHelper(TSTNode n) {
            ArrayList<TSTNode> result = new ArrayList<>();
            if (n == null) {
                return result;
            }
            TSTNode left = n.getChildren()[0];
            TSTNode right = n.getChildren()[2];
            result.addAll(allNextCharactersHelper(left));
            result.addAll(allNextCharactersHelper(right));
            result.add(n);
            return result;
        }



        // Getters and Setters
        public TSTNode[] getChildren() {
            return _children;
        }

        public Double getVal() {
            return _val;
        }

        public void setVal(Double val) {
            _val = val;
        }

        public Double getMax() {
            return _max;
        }

        public void setMax(Double d) {
            _max = d;
        }

        public Character getChar() {
            return _c;
        }

        public void setChar(Character c) {
            _c = c;
        }

        public void setChildren(TSTNode left, TSTNode middle, TSTNode right) {
            _children[0] = left;
            _children[1] = middle;
            _children[2] = right;

        }
    }
}
