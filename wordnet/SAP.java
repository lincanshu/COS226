import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public final class SAP {

    private final Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("in SAP(), argument is null");
        }
        this.G = new Digraph(G);
    }

    private void checkRange(int v) {
        // Any vertex argument is outside its prescribed range
        if (v < 0 || v >= G.V()) {
            throw new IllegalArgumentException("argument is outside its prescribed range");
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        checkRange(v);
        checkRange(w);
        BreadthFirstDirectedPaths vP = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths wP = new BreadthFirstDirectedPaths(G, w);
        int shortest = Integer.MAX_VALUE;
        for (int i = 0; i < G.V(); i++) {
            if (vP.hasPathTo(i) && wP.hasPathTo(i)) {
                shortest = Math.min(shortest, vP.distTo(i) + wP.distTo(i));
            }
        }
        if (shortest == Integer.MAX_VALUE) {
            return -1;
        }
        return shortest;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        checkRange(v);
        checkRange(w);
        BreadthFirstDirectedPaths vP = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths wP = new BreadthFirstDirectedPaths(G, w);
        int ancestral = -1;
        int shortest = Integer.MAX_VALUE;
        for (int i = 0; i < G.V(); i++) {
            if (vP.hasPathTo(i) && wP.hasPathTo(i) && shortest > vP.distTo(i) + wP.distTo(i)) {
                shortest = vP.distTo(i) + wP.distTo(i);
                ancestral = i;
            }
        }
        return ancestral;
    }

    private void checkNull(Iterable<Integer> v) {
        // Any argument is null
        if (v == null) {
            throw new IllegalArgumentException("argument is null");
        }
        // Any iterable argument contains a null item
        for (Integer i : v) {
            if (i == null) {
                throw new IllegalArgumentException("iterable argument contains a null item");
            }
            checkRange(i);
        }
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        checkNull(v);
        checkNull(w);
        if (count(v) == 0 || count(w) == 0) return -1;
        BreadthFirstDirectedPaths vP = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths wP = new BreadthFirstDirectedPaths(G, w);
        int shortest = Integer.MAX_VALUE;
        for (int i = 0; i < G.V(); i++) {
            if (vP.hasPathTo(i) && wP.hasPathTo(i)) {
                shortest = Math.min(shortest, vP.distTo(i) + wP.distTo(i));
            }
        }
        if (shortest == Integer.MAX_VALUE) {
            return -1;
        }
        return shortest;
    }

    private int count(Iterable<Integer> v) {
        int count = 0;
        for (int i : v) {
            count++;
        }
        return count;
    }
    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        checkNull(v);
        checkNull(w);
        if (count(v) == 0 || count(w) == 0) return -1;
        BreadthFirstDirectedPaths vP = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths wP = new BreadthFirstDirectedPaths(G, w);
        int ancestral = -1;
        int shortest = Integer.MAX_VALUE;
        for (int i = 0; i < G.V(); i++) {
            if (vP.hasPathTo(i) && wP.hasPathTo(i) && shortest > vP.distTo(i) + wP.distTo(i)) {
                shortest = vP.distTo(i) + wP.distTo(i);
                ancestral = i;
            }
        }
        return ancestral;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
