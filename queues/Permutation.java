/* *****************************************************************************
 *  Name: lincanshu
 *  Date: 2020.7.6
 *  Description: Write a client program Permutation.java that takes an interger
 *  k as a command-line argument; reads a sequence of strings from standard input
 *  using StdIn.readString(); and prints exactly k of them, uniformly at random.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            randomizedQueue.enqueue(StdIn.readString());
        }
        for (int i = 0; i < k; ++i) {
            StdOut.println(randomizedQueue.dequeue());
        }
    }
}
