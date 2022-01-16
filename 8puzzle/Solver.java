/* *****************************************************************************
 *  Name: Lincanshu
 *  Date: 2020/7/26
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public final class Solver {

    private final boolean solvable;
    private final Stack<Board> results;

    private class Node implements Comparable<Node> {
        private final Board initial;
        private final Node prev;
        private final int moves;
        private final int manhattan;
        
        public Node(Board initial) {
            this.initial = initial;
            this.manhattan = this.initial.manhattan();
            this.moves = 0;
            this.prev = null;
        }

        public Node(Board initial, int moves, Node prev) {
            this.initial = initial;
            this.manhattan = this.initial.manhattan();
            this.moves = moves;
            this.prev = prev;
        }

        public int compareTo(Node that) {

            if (this.moves + this.manhattan < that.moves + that.manhattan) {
                return -1;
            }
            else if (this.moves + this.manhattan > that.moves + that.manhattan) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }

    // find a solution to the initial board(using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("initial is null");
        }
        Node finalRes = new Node(initial);
        MinPQ<Node> solvePQ = new MinPQ<>();
        solvePQ.insert(new Node(initial));

        MinPQ<Node> unsolvePQ = new MinPQ<>();
        unsolvePQ.insert(new Node(initial.twin()));
        boolean flag = true;
        while (!solvePQ.isEmpty()) {
            if (flag) {
                Node cur = solvePQ.delMin();
                if (cur.initial.isGoal()) {
                    finalRes = cur;
                    break;
                }
                for (Board i : cur.initial.neighbors()) {
                    if (cur.prev != null && i.equals(cur.prev.initial)) continue;
                    solvePQ.insert(new Node(i, cur.moves + 1, cur));
                }
            } else {
                Node cur = unsolvePQ.delMin();
                if (cur.initial.isGoal()) {
                    break;
                }
                for (Board i : cur.initial.neighbors()) {
                    if (cur.prev != null && i.equals(cur.prev.initial)) continue;
                    unsolvePQ.insert(new Node(i, cur.moves + 1, cur));
                }

            }
            flag = !flag;
        }
        solvable = flag;
        if (solvable) {
            results = new Stack<>();
            while (finalRes != null) {
                results.push(finalRes.initial);
                finalRes = finalRes.prev;
            }
        } else {
            results = null;
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!solvable) return -1;
        return results.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!solvable) return null;
        return results;
    }

    // test client
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
