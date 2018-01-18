import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.HashSet;

/**
 * AlphabetSort takes input from stdin and prints to stdout.
 * The first line of input is the alphabet permutation.
 * The the remaining lines are the words to be sorted.
 * 
 * The output should be the sorted words, each on its own line, 
 * printed to std out.
 */
public class AlphabetSort {

    /** A string representing this AlphabetSort's alphabet. */
    private static String _alphabet;
    /** The terms given by the input file, terms containing characters
     * not in the given alphabet are excluded.*/
    private static ArrayList<String> _terms = new ArrayList<>();

    /**
     * Reads input from standard input and prints out the input words in
     * alphabetical order.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        InputStreamReader iSReader = new InputStreamReader(System.in);
        BufferedReader input = new BufferedReader(iSReader);
        try {
            _alphabet = input.readLine();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
        char[] distinctionCheck = _alphabet.toCharArray();
        HashSet<Character> distinct = new HashSet<Character>();
        for (int i = 0; i < distinctionCheck.length; ++i) {
            distinct.add(distinctionCheck[i]);
        }
        if (distinct.size() != _alphabet.length()) {
            throw new IllegalArgumentException("A letter of your alphabet appears"
                    + " more than once in the given alphabet.");
        }

        _terms = input.lines().filter(s -> {
            boolean isValid = true;
            for (int i = 0; i < s.length(); ++i) {
                if (!distinct.contains(s.charAt(i))) {
                    isValid = false;
                    break;
                }
            }
            return isValid;
        }).sorted((s1, s2) -> {
            int comparison = 0;
            for (int i = 0; i < Math.min(s1.length(), s2.length()); ++i) {
                if (_alphabet.indexOf(s1.charAt(i)) < _alphabet.indexOf(s2.charAt(i))) {
                    comparison = -1;
                    break;
                } else if (_alphabet.indexOf(s1.charAt(i)) > _alphabet.indexOf(s2.charAt(i))) {
                    comparison = 1;
                    break;
                }
            }
            if (comparison == 0 && s1.length() < s2.length()) {
                comparison = -1;
            } else if (comparison == 0 & s1.length() > s2.length()) {
                comparison = 1;
            }
            return comparison;
        }).collect(Collectors.toCollection(ArrayList::new));
        if (_terms.isEmpty()) {
            throw new IllegalArgumentException("No terms were given to be sorted according to"
                    + " the given alphabet.");
        }
        for (String s : _terms) {
            System.out.println(s);
        }


    }
}
