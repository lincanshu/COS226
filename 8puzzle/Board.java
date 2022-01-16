/* *****************************************************************************
 *  Name: lincanshu
 *  Date: 2020/7/25
 *  Description: Create a data type that models an n-by-n board with sliding tiles.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public final class Board {

    private final int n;
    private final int amount;
    private final int[] array;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        amount = n * n;
        array = new int[amount + 1];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                array[1 + j + i * n] = tiles[i][j];
    }

    private Board(int n, int[] copy) {
        this.n = n;
        this.amount = n * n;
        this.array = copy.clone();
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder(String.valueOf(n) + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(array[1 + j + i * n] + " ");
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int res = 0;
        for (int i = amount - 1; i > 0; i--) {
            if (array[i] != i) res++;
        }
        return res;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int res = 0;
        for (int i = amount; i > 0; i--) {
            if (array[i] != 0 && array[i] != i) {
                res += Math.abs((array[i] - 1) / n - (i - 1) / n);
                res += Math.abs((array[i] - 1) % n - (i - 1) % n);
            }
        }
        return res;
    }

    // is this board the goal board?
    public boolean isGoal() {
        if (array[amount] != 0) return false;
        for (int i = amount - 1; i > 0; i--) {
            if (array[i] != i) return false;
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != Board.class) return false;
        Board that = (Board) y;
        return Arrays.equals(this.array, that.array);
    }

    private void exch(int a, int b) {
        int temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> queue = new Queue<>();
        int i = amount;
        while (i > 0 && array[i] != 0) {
            i--;
        }
        if (i - n > 0) {
            exch(i, i - n);
            queue.enqueue(new Board(this.n, this.array));
            exch(i, i - n);
        }
        if (i + n <= amount) {
            exch(i, i + n);
            queue.enqueue(new Board(this.n, this.array));
            exch(i, i + n);
        }
        if ((i - 1) % n > 0) {
            exch(i, i - 1);
            queue.enqueue(new Board(this.n, this.array));
            exch(i, i - 1);
        }
        if ((i - 1) % n + 1 < n) {
            exch(i, i + 1);
            queue.enqueue(new Board(this.n, this.array));
            exch(i, i + 1);
        }
        return queue;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int a = 1, b = 2;
        if (array[a] == 0 || array[b] == 0) {
            a = 3;
            b = 4;
        }
        exch(a, b);
        Board other = new Board(this.n, this.array);
        exch(a, b);
        return other;
    }

    // unit testing
    public static void main(String[] args) {

        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board initial = new Board(tiles);
        /**
         * test twin()
         */
        for (int i = 0; i < 5; i++) {
            StdOut.println(initial.twin());
        }

        /**
         * test neighbors()
         */
        // for (Board i : initial.neighbors()) {
        //     StdOut.println(i);
        // }

        /**
         * test equals()
         */
        // StdOut.println(initial.equals(null));
        // StdOut.println(initial.equals(initial));
        // Board copy = new Board(initial.n, initial.array);
        // StdOut.println(initial.equals(copy));
        // copy.array[1] = 100;
        // StdOut.println(initial.equals(copy));

        /**
         * test isGoal()
         */
        // StdOut.println(initial.isGoal());

        /**
         * test manhattan()
         */
        // StdOut.println(initial.manhattan());

        /**
         * test hamming()
         */
        // StdOut.println(initial.hamming());
    }
}
