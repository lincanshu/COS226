/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class StringST {
    private static final int R = 26;

    private Node root;

    private static class Node {
        private int val;
        private Node[] next = new Node[R];
    }

    public StringST() {
    }

    private Node put(Node x, String key, int val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.val = val;
            return x;
        }
        int i = getIndex(key, d);
        x.next[i] = put(x.next[i], key, val, d + 1);
        return x;
    }

    public void add(String key, int val) {
        if (key == null) throw new IllegalArgumentException("argument ot get() is null");
        root = put(root, key, val, 0);
    }

    private int getIndex(String key, int d) {
        return key.charAt(d) - 'A';
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        int i = getIndex(key, d);
        return get(x.next[i], key, d + 1);
    }

    public int get(String key) {
        if (key == null) throw new IllegalArgumentException("argument ot get() is null");
        Node x = get(root, key, 0);
        if (x == null) return -1;
        return x.val;
    }

    public boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        int val = get(key);
        return val != -1 && val != 0;
    }

    public boolean containsPrefix(String key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != -1;
    }
}
