---
title: Priority Queue
date: 2020-07-22 22:25:15
tags: algorithm
---

### Priority queue API

```java
public class MaxPQ<Key extends Comparable<Key>>
{
    // create an empty priority queue
    MaxPQ();
    
    // create a priority queue with given keys
    MaxPQ(Key[] a);
    
    // insert a key into the priority queue
    void insert(Key v);
    
    // return and remove the largest key
    Key delMax();
    
    // is the priority queue empty?
    boolean isEmpty();
    
    // return the largest key
    Key max();
    
    // number of entries in the priority queue
    int size();    
}
```

<!--more-->

### Priority queue: unordered array implementation

```java
public class UnorderedMaxPQ<Key extends Comparable<Key>>
{
    private Key[] pq;
    private int N;
    
    public UnorderedMaxPQ(int capacity)
    {	pq = (Key[]) new Comparable[capacity];	}
    
    public boolean isEmpty()
    {	return N == 0;	}
    
    public void insert(Key x)
    {	pq[N++] = x;	}
    
    public Key delMax()
    {
        int max = 0;
        for (int i = 1; i < N; i++)
            if (less(max, i)) max = i;
        exch(max, N-1);
        return pq[--N];
    }
}
```

## Binary heap: Java implementation

```java
public class MaxPQ<Key extends Comparable<Key>>
{
    private Key[] pq;
    private int N;
    
    public MaxPQ(int capacity)
    {
        pq = (Key[]) new Comparable[capacity+1];
    }
    
    // PQ ops
    public boolean isEmpty()
    {
        return N == 0;
    }
    
    public void insert(Key x)
    {
    	pq[++N] = x;
        swim(x);
    }
    
    public void delMax()
    {
    	Key max = pq[1];
        exch(1, N--);
        sink(1);
        pq[N+1] = null;
        return max;
    }
    
    // heap helper functions
    private void swim(int k) 
    {
        while (k > 1 && less(k/2, k)) 
        {
            exch(k, k/2);
            k = k/2;
        }
    }
    
    private void sink(int k)
    {
        while (2*k <= N)
        {
            int j = 2*k;
            if (j < N && less(j, j+1)) j++;
            if (!less(k, j)) break;
            exch(k, j);
            k = j;
        }
    }
    
    // array helper functions
    private boolean less(int i, int j)
    {
        return pq[i].compareTo(pq[j]) < 0;
    }
    
    private void exch(int i, int j)
    {
        Key t = pq[i];
        pq[i] = pq[j];
        pq[j] = t;
    }
}
```

### Immutability: implementing in Java

- Classes should be immutable unless there's a very good reason to make them mutable... If a class cannot be made immutable, you should still limit its mutability as much as possible.
- can't override instance methods
- all instance variables private and final
- defensive copy of mutable instance variables
- instance method don't change instance variables

```java
public final class Vector
{
    private final int N;
    private final double[] data;
    
    public Vector(double[] data)
    {
        this.N = data.length;
        this.data = new double[N];
        for (int i = 0; i < N; i++)
            this.data[i] = data[i];
    }
}
```

#### Advantages

- Simplifies debugging.
- Safer in presence of hostile code.
- Simplifies concurrent programming.
- Safe to use as key in priority queue or symbol table.

#### Disadvantage. 

- Must create new object for each data type value.

### Heapsort: Java implementation

```java
public class Heap
{
    public static void sort(Comparable[] a)
    {
        int N = a.length;
        for (int k = N/2; k >= 1; k--)
            sink(a, k, N);
        while (N > 1)
        {
            exch(a, 1, N);
            sink(a, 1, --N);
        }
    }
    
    private static void sink(Comparable[] a, int k, int N)
    {
        while (k*2 <= N)
        {
            int j = k*2;
            if (j < N && less(a, j, j+1)) j++;
            if (!less(a, k, j)) break;
            exch(a, k, j);
            k = j;
        }
    }
}
```

