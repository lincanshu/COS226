/* *****************************************************************************
 *  Name: lincs
 *  Date: 2020/9/12
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {

    private class CircularSuffix implements Comparable<CircularSuffix> {
        private int index;

        public CircularSuffix(int index) {
            this.index = index;
        }

        public int compareTo(CircularSuffix that) {
            for (int i = 0; i < n; i++) {
                char a = s.charAt((index + i) % n);
                char b = s.charAt((that.index + i) % n);
                if (a == b) {
                    continue;
                }
                return a - b;
            }
            return 0;
        }
    }

    private int n;
    private String s;
    private int[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException("s is null");
        }
        this.s = s;
        this.n = s.length();
        this.index = new int[n];
        CircularSuffix[] array = new CircularSuffix[n];
        for (int i = 0; i < n; i++) {
            array[i] = new CircularSuffix(i);
        }
        Arrays.sort(array);
        for (int i = 0; i < n; i++) {
            index[i] = array[i].index;
        }
    }

    // length of s
    public int length() {
        return n;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= n) {
            throw new IllegalArgumentException("i is not valid");
        }
        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray test = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < 12; i++) {
            StdOut.println(test.index(i));
        }
    }

}
