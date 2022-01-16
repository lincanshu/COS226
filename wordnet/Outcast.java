import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public final class Outcast {

    private final WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordNet) {
        this.wordNet = wordNet;
    }

    // given an arry of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int n = nouns.length;
        int[] dist = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    dist[i] += wordNet.distance(nouns[i], nouns[j]);
                }
            }
        }
        int res = 0;
        for (int i = 0; i < n; i++) {
            if (dist[res] < dist[i]) res = i;
        }
        return nouns[res];
    }

    public static void main(String[] args) {
        WordNet wordNet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordNet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
