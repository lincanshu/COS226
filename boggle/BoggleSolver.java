import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.Set;

public final class BoggleSolver {
    private final StringST dict;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        dict = new StringST();
        for (String word : dictionary) {
            if (word.length() > 7) dict.add(word, 11);
            else if (word.length() > 6) dict.add(word, 5);
            else if (word.length() > 5) dict.add(word, 3);
            else if (word.length() > 4) dict.add(word, 2);
            else if (word.length() > 2) dict.add(word, 1);
        }
    }

    private void backtrack(int i, int j, boolean[] marked, BoggleBoard board, Set<String> set,
                           StringBuilder s) {
        if (i < 0 || i == board.rows() || j < 0 || j == board.cols() ||
                marked[i * board.cols() + j]) {
            return;
        }

        s.append(board.getLetter(i, j));
        if (board.getLetter(i, j) == 'Q') {
            s.append('U');
        }
        if (!dict.containsPrefix(s.toString())) {
            if (board.getLetter(i, j) == 'Q') {
                s.deleteCharAt(s.length() - 1);
            }
            s.deleteCharAt(s.length() - 1);
            return;
        }

        marked[i * board.cols() + j] = true;
        if (s.length() > 2 && dict.contains(s.toString())) {
            set.add(s.toString());
        }
        backtrack(i - 1, j, marked, board, set, s);
        backtrack(i + 1, j, marked, board, set, s);
        backtrack(i, j + 1, marked, board, set, s);
        backtrack(i, j - 1, marked, board, set, s);

        backtrack(i - 1, j - 1, marked, board, set, s);
        backtrack(i - 1, j + 1, marked, board, set, s);
        backtrack(i + 1, j + 1, marked, board, set, s);
        backtrack(i + 1, j - 1, marked, board, set, s);
        if (board.getLetter(i, j) == 'Q') {
            s.deleteCharAt(s.length() - 1);
        }
        s.deleteCharAt(s.length() - 1);
        marked[i * board.cols() + j] = false;
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Set<String> set = new HashSet<>();
        boolean[] marked = new boolean[board.rows() * board.cols()];
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                backtrack(i, j, marked, board, set, new StringBuilder());
            }
        }
        return set;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int score = dict.get(word);
        return score == -1 ? 0 : score;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

}
