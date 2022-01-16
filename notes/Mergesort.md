---
title: Mergesort
date: 2020-07-07 15:06:01
tags: algorithm
---

### Merging: Java implementation

```java
private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi)
{
    assert isSorted(a, lo, mid);	// precondition: a[lo..mid] sorted
    assert isSorted(a, mid+1, hi);	// precondition: a[mid+1..hi] sorted

    for (int k = lo; k <= hi; ++k)
        aux[k] = a[k];

    int i = lo, int j = mid+1;
    for (int k = lo; k <= hi; ++k)
    {
        if 		(i > mid)			a[k] = aux[j++];
        else if (j > hi) 			a[k] = aux[i++];
        else if (less(a[i], a[j]))	a[k] = a[i++];
        else						a[k] = a[j++];
    }
    assert isSorted(a, lo, hi);		// postcondition: a[lo..hi] sorted
}
```

<!--more-->

### Mergesort: Java implementation

```java
public class Merge
{
    private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi)
    {	/* as befoe	*/	}
    
    private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi)
    {
        if (hi <= lo) return;
        int mid = lo + (hi - lo)/2;
        sort(a, aux, lo, mid);
        sort(a, aux, mid + 1, hi);
        merge(a, aux, lo, mid, hi);
    }
    
    public static void sort(Comparable[] a)
    {
        aux = new Comparable[a.length];
        sort(a, aux, 0, a.length - 1);
    }
}
```

### Assertions

**Assertion**. Statement to test assumptions about your program.

- Helps detect logic bugs.
- Documents code.

**Can enable or disable at runtime**. No cost in production code

```shell
java -ea MyProgram	// enable assertions
java -da MyProgram	// disable assertions (default)
```

### Mergesort: practical improvements

**Use insertion sort for small subarrays**. Cutoff is about 7 items.

```java
private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi)
{
    if (hi <= lo + CUTOFF - 1)
    {
        Intertion.sort(a, lo, hi);
        return;
    }
    int mid = lo + (hi - lo) / 2;
    sort(a, aux, lo, mid);
    sort(a, aux, mid+1, hi);
    merge(a, aux, lo, mid, hi);
}
```

**Stop if already sorted**

```java
private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi)
{
    if (hi <= lo) return;
    int mid = lo + (hi - lo) / 2;
    sort(a, aux, lo, mid);
    sort(a, aux, mid+1, hi);
    if (!less(a[mid+1], a[mid])) return;
    merge(a, aux, lo, mid, hi);
}
```

**Eliminate the copy to the auxiliary array**. Save time

```java
private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi)
{
    int i = lo, j = mid+1;
    for (int k = lo; k <= hi; ++k)
    {
        if (i > mid) aux[k] = a[j++];
        else if (j > hi) aux[k] = a[i++];
        else if (less(a[j], a[i])) aux[k] = a[j++];
        else aux[k] = a[i++];
    }
}

private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi)
{
    if (hi <= lo) return;
    int mid = lo + (hi - lo)/2;
    sort(a, aux, lo, mid);
    sort(a, aux, mid+1, hi);
    merge(a, aux, lo, mid, hi);
}
```

### Bottom-up mergesort: Java implementation

```java
public class MergeBU
{
    private static void merge(...)
    {	/*	as before */	}
    
    public static void sort(Comparable[] a)
    {
        int N = a.length;
        Comparable[] aux = new Comparable[N];
        for (int sz = 1; sz < N; sz = sz+sz)
            for (int lo = 0; lo < N-sz; lo += sz+sz)
                merge(a, aux, lo, lo+sz-1, Math.min(lo+sz+sz-1, N-1));
    }
}
```

### Comparator interface: using with our sorting libraries

- use **Object** instead of Comparable
- pass Comparator to sort() and less() and use it in less()

```java
public static void sort(Object[] a, Comparator comparator)
{
    int N = a.length;
    for (int i = 0; i < N; ++i)
        for (int j = i; j > 0 && less(comparator, a[j], a[j-1]); --j)
            exch(a, j, j-1);
}

private static boolean less(Comparator c, Objcet v, Object w)
{
    return c.compare(v, w) < 0;
}

private static void exch(Object[] a, int i, int j)
{
    Object swap = a[i];
    a[i] = a[j];
    a[j] = swap;
}
```

### Comparator interface: implementing

- Define a (nested) class that implements the Comparator interface
- Implement the compare() method

```java
public class Student
{
    public static final Comparator<Student> BY_NAME		= new ByName();
    public static final Comparator<Student> BY_SECTION	= new BySection();
    private final String name;
    private final int section;
    ...
    
    private static class ByName implements Comparator<Student>
    {
        public int compare(Student v, Student w)
        {
            return v.name.compareTo(w.name);
        }
    }
    
    private static class BySection implements Comparator<Student>
    {
        public int compare(Student v, Student w)
        {
            return v.section - w.section;	// here works since no danger of overflow
        }
    }
}
```

