---
title: ElementarySorts
date: 2020-07-06 18:06:39
tags: algorithm
---

### Callbacks

Callback = reference to **executable code**.

- Client passes array of objects to sort() function.
- The sort() function calls back object's compareTo() method as needed.

### Implementing the Comparable interface

```java
public class Date implements Comparable<Date>
{
    private final int month, day, year;
    
    public Date(int m, int d, int y)
    {
        month = m;
        day = d;
        year = y;
    }
    
    public int compareTo(Date that)
    {
        if (this.year	< that.year	) return -1;
        if (this.year	> that.year	) return +1;
        if (this.month	< that.month) return -1;
        if (this.month	> that.month) return +1;
        if (this.day	< that.day	) return +1;
        if (this.day	> that.day	) return +1;
        return 0;
    }
}
```

<!--more-->

### Two useful sorting abstractions

Helper function. Refer to data through compares and exchanges.

- **Less**. Is item v less than w?
- **Exchange**. Swap item in array a[] at index i with the on at index j.

```java
private static boolean less(Comparable v, Comparable w) 
{
    return v.compareTo(w) < 0;
}

private static void exch(Comparable[] a, int i, int j)
{
    Comparable swap = a[i];
    a[i] = a[j];
    a[j] = swap;
}

private static boolean isSorted(Comparable[] a)
{
    for (int i = 1; i < a.length; ++i)
        if (less(a[i], a[i-1])) return false;
    return true;
}
```

### Selection sort: Java implementation

```java
public class Selection
{
    public static void sort(Comparable[] a) 
    {
        int N = a.length;
        for (int i = 0; i < N; ++i) 
        {
            int min = i;
            for (int j = i+1; j < N; ++j)
                if (less(a[j], a[min]))
                    min = j;
            exch(a, i, min);
        }
    }
    
    private static boolean less(Compable v, Compable w)
    {	/* as before */	}
    
    private static void exch(Compable[] a, int i, int j)
    {	/* as before */	}
}
```

### Insertion sort: Java implementation

```java
public class Insertion
{
    public static void sort(Comparable[] a) 
    {
        int N = a.length;
        for (int i = 0; i < N; ++i)
        {
            for (int j = i; j > 0; --j)
                if (less(a[j], a[j-1]))
                    exch(a, j, j-1);
            	else
                    break;
        }
    }
    
    private boolean less(Comparable v, Comparable w)
    {	/* as before */	}
    
    private void exch(Comparable[] a, int i, int j)
    {	/* as before */	}
}
```

### Shellsort: Java implementation

```java
public class Shell
{
    public static void sort(Comparable[] a)
    {
        int N = a.length;
        
        int h = 1;
        while (h < N/3) h = 3*h + 1; // 1, 4, 13, 40, 121, 364, ...
        
        while (h >= 1) 
        {	// h-sort the array
            for (int i = h; i < N; ++i)
            {
                for (int j = i; j >= h && less(a[j], a[j-h]); j -= h)
                    exch(a, j, j-h);
            }
            
            h = h/3;
        }
    }
    
    private boolean less(Comparable v, Comparable w)
    {	/* as before */	}
    
    private void exch(Comparable[] a, int i, int j)
    {	/* as before */	}
}
```

### Knuth shuffle

```java
public class StdRandom
{
    public static void shuffle(Object[] a)
    {
        int N = a.length;
        for (int i = 0; i < N; ++i)
        {
            int r = StdRandom.uniform(i + 1);
            exch(a, i, r);
        }
    }
}
```





### 

