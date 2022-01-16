/* *****************************************************************************
 *  Name: lincs
 *  Date: 2020/9/11
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;
import java.util.List;

public class MoveToFront {

    // alphabet size of extended ASCII
    private static final char R = 256;

    // apply move-to-front encoding. reading from standard input and writing to
    // standard ouput
    public static void encode() {
        LinkedList<Character> list = new LinkedList<>();
        for (char i = 0; i < R; i++) {
            list.add(i);
        }
        while (!BinaryStdIn.isEmpty()) {
            char ch = BinaryStdIn.readChar();
            int i = list.indexOf(ch);
            BinaryStdOut.write(i, 8);
            list.remove(i);
            list.addFirst(ch);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front deconing. reading from standard input and writing to
    // standard output
    public static void decode() {
        List<Character> list = new LinkedList<>();
        for (char i = 0; i < R; i++) {
            list.add(i);
        }
        while (!BinaryStdIn.isEmpty()) {
            char i = BinaryStdIn.readChar();
            char res = list.remove(i);
            BinaryStdOut.write(res);
            list.add(0, res);
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
