---
title: Binary Search Trees Symbol Tables
date: 2020-07-30 10:13:36
tags: algorithm
---

### Implementing equals for user-defined types

```java
public final class Date implements Comparable<Date>
{
    private final int month;
    private final int day;
    private final int year;
    ...
        
    public boolean equals(Object y)
    {
        if (y == this) return true;
        // check for null
        if (y == null) return false;
        // Object must be in the same class
        if (y.getClass() != this.getClass()) 
            return false;
        // check that all significant fields are the same
        Date that = (Date) y;
        if (this.day != that.day) return false;
        if (this.month != that.month) return false;
        if (this.year != that.yar) return false;
        return true;
    }
}
```

### BST implementation

```java
public class BST<Key extends Comparable<Key>, Value>
{
    private Node root;
    
    private class Node
    {
        private Key key;
        private Value val;
        private Node left, right;
        private int count;
        public Node(Key key, Value val, int count) 
        {
            this.key = key;
            this.val = val;
            this.count = count;
        }
    }
    
    public int size() 
    {
        return size(root);
    }
    
    private int size(Node x)
    {
    	if (x == null) return 0;
        else return x.count;
    }
    
    public int rank(Key key)
    {
        return rank(key, root);
    }
    
    private int rank(Key key, Node x)
    {
        if (x == null) return 0;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return rank(key, x.left);
        else if (cmp > 0) return 1 + size(x.left) + rank(key, x.right);
        else return size(x.left);
    }
    
    private Node put(Node x, Key key, Value val)
    {
        if (x == null) return new Node(key, val, 1);
        int cmp = key.compareTe(x.key);
        if (cmp < 0) x.left = put(x.left, key, val);
        else if (cmp > 0) x.right = put(x.right, key, val);
        else x.val = val;
        x.count = 1 + size(x.left) + size(x.right);
        return x;
    }
    
    public void put(Key key, Value val)
    {
        root = put(root, key, val);
    }
    
    public Value get(Key key)
    {
        Node x = root;
        while (x != null)
        {
            int cmp = key.compareTo(x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else return x.val;
        }
        return null;
    }
    
    public void deleteMin() 
    {
        root = deleteMin(root);
    }
    
    private Node deleteMin(Node x)
    {
    	if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        x.count = 1 + size(x.left) + size(x.right);
        return x;
    }
    
    public void delete(Key key)
    {
        root = delete(root, key);
    }
    
    private Node delete(Node x, Key key) 
    {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        // search for key
        if (cmp < 0) x.left = delete(x.left, key);
        else if (cmp > 0) x.right = delete(x.right, key);
        else {
            // only one child
            if (x.left == null) return x.right;
            if (x.right == null) return x.left;
            
            Node t = x;
            x = min(t.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
        }
        x.count = 1 + size(x.left) + size(x.right);
        return x;
    }
    
    private Node min(Node x) 
    {
        while (x.left != null) x = x.left;
        return x;
    }
    
    public Iterable<Key> iterator()
    {
        Queue<Key> q = new Queue<>();
        inorder(root, q);
        return q;
    }
    
    private void inorder(Node x, Queue<Key> q)
    {
        if (x == null) return;
        inorder(x.left, q);
        q.enqueue(x.key);
        inorder(x.right, q);
    }
}
```

