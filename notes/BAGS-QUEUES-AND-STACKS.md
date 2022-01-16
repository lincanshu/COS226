---
title: 'BAGS, QUEUES, AND STACKS'
date: 2020-07-05 10:17:30
tags: algorithm
---

### Stack API

```java
public class StackOfStrings {
    // create an empty stack
    StaskofString();
    
    // insert a new string onto stack
    void push(String item);
    
    // remove and return the stirng most recently added
    void pop();
    
    // is the stack empty?
    boolean isEmtpy();
    
    // number of strings on the stack
    int size();
}
```

<!--more-->

### Generic Stack with Iterator: linked-list implementation

```java
import java.util.Iterator;

public class Stack<Item> implements Iterable<Item>
{
    private Node first = null;
    
    private class Node
    {
        Item item;
        Node next;
    }
    
    public boolean isEmpty()
    {
        return first == null;
    }
    
    public void push(Item item) 
    {
        Node oldfirst = fisrt;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
    }
    
    public Item pop()
    {
        Item item = first.item;
        first = first.next;
        return item;
    }
    
    public Iterator<Item> iterator() 
    {
        return new ListIterator();
    }
    
    private class ListIterator implements Iterator<Item>
    {
        private Node current = first;
        
        public boolean hasNext() 
        {
            return first != null;
        }
        
        public void remove() {
            /* Not support */
        }
        
        public Item next() 
        {
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
}
```

### Generic Stack with Iterator: Resizing-array implementation

```java
import java.util.Iterator;

public class Stack<Item> implements Iterable<Item>
{
    private Item[] s;
    private int N = 0;
    
    public Stack()
    {
        s = (Item[]) new Object[1];
    }
    
    public boolean isEmpty()
    {
        return N == 0;
    }
    
    private void resize(int capacity)
    {
    	Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < N; ++i)
        {
            copy[i] = s[i];
        }
        s = copy[i];
    }
    
    public void push(Item item)
    {
        if (N == s.length) 
        {
            resize(s.length * 2);
        }
        s[N++] = item;
    }
    
    public Item pop()
    {
        Item item = s[--N];
        s[N] = null;
        if (N > 0 && N == s.length/4)
        {
            resize(s.length/2);
        }
        return s[--N];
    }
    
    public Iterator<Item> iterator()
    {
        return new ReverseArrayIterator();
    }
    
    private class ReverseArrayIterator implements Iterable<Item>
    {
        private int i = N;
        
        public boolean hasNext()
        {
            return i > 0;
        }
        
        public void remove()
        {
            /* Not supported */
        }
        
        public Item next() 
        {
            return s[--i];
        }
    }
}
```

### Queue API

```java
public class QueueOfStrings
{
    // create an empty queue
    QueueOfStrings();
    
    // insert a new string onto queue
    void enqueue(String item);
    
    // remove and return the string least recently added
    String dequeue();
    
    // is the queue empty?
    boolean isEmtpy();
    
    // number of strings on the queue
    int size();    
}
```

### Queue: linked-list implementation in Java

```java
public class LinkedQueueOfStirngs
{
    private Node first, last;
    
    private class Node
    {
        String item;
        Node next;
    }
    
    public boolean isEmpty()
    {
        return first == null;
    }
    
    public void enqueue(String item)
    {
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = oldlast;
        if (isEmpty()) first = last;
        else oldlast.next = last;
    }
    
    public String dequeue()
    {
        String item = first.item;
        first = first.next;
        if (isEmpty()) last = null;
        return item;
    }
}
```

### Bag API

```java
public class Bag<Item> implements Iterable<Item>
{
    // create an empty bag
    Bag();
    
    // insert a new item onto bag
    void add(Item x);
    
    // numberof items int bag
    int size();
    
    // iterator for all items in bag
    Iterable<Item> iterator();
}
```



