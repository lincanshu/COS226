/* *****************************************************************************
 *  Name: Lincanshu
 *  Date: 2020/8/9
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.Map;

public final class WordNet {

    // store word -> id
    private final Map<String, Bag<Integer>> nounsMap;
    // store id -> synsets
    private final Map<Integer, String> synsetsMap;
    private final Digraph digraph;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("in WordNet() the argument is null");
        }

        nounsMap = new HashMap<>();
        synsetsMap = new HashMap<>();
        readSynsets(synsets);

        this.digraph = new Digraph(synsetsMap.size());
        readHypernyms(hypernyms);

        // check DAG
        if (new DirectedCycle(digraph).hasCycle())
            throw new IllegalArgumentException("The graph has cycle.");
        // check only one root
        boolean once = true;
        for (int v = 0; v < digraph.V(); v++) {
            if (!once && digraph.outdegree(v) == 0) {
                throw new IllegalArgumentException("The graph has more than one root.");
            }
            if (once && digraph.outdegree(v) == 0) {
                once = false;
            }
        }
        sap = new SAP(digraph);
    }

    private void readSynsets(String synsets) {
        In in = new In(synsets);
        while (in.hasNextLine()) {
            String[] fields = in.readLine().split(",");
            int id = Integer.parseInt(fields[0]);
            synsetsMap.put(id, fields[1]);
            String[] words = fields[1].split(" ");
            for (String word : words) {
                if (!nounsMap.containsKey(word)) {
                    nounsMap.put(word, new Bag<>());
                }
                nounsMap.get(word).add(id);
            }
        }
    }

    private void readHypernyms(String hypernyms) {
        In in = new In(hypernyms);
        while (in.hasNextLine()) {
            String[] fields = in.readLine().split(",");
            int v = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                int w = Integer.parseInt(fields[i]);
                digraph.addEdge(v, w);
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounsMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("in isNoun() the argument is null");
        }
        return nounsMap.containsKey(word);
    }

    private void validateNoun(String noun) {
        if (!nounsMap.containsKey(noun)) {
            throw new IllegalArgumentException("No noun is in wordNet");
        }
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        validateNoun(nounA);
        validateNoun(nounB);
        Bag<Integer> v = nounsMap.get(nounA);
        Bag<Integer> w = nounsMap.get(nounB);
        return sap.length(v, w);
    }

    // a synset (second field of synsets.txt) that is the common ancestor nounA and nounB
    // in a shortest ancestral path
    public String sap(String nounA, String nounB) {
        validateNoun(nounA);
        validateNoun(nounB);
        Bag<Integer> v = nounsMap.get(nounA);
        Bag<Integer> w = nounsMap.get(nounB);
        int ancestor = sap.ancestor(v, w);
        return synsetsMap.get(ancestor);
    }

    // do unit testing of this class
    public static void main(String[] args) {
    }
}
