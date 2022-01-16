## Union-find data type(API)

```java
public class UF
{
    // initialize union-find data structure with N objects (0 to N-1)
    UF(int N);

    // add connection between p and q
    void union(int p, int q);
    
    // are p and q in the same component?
    boolean connected(int p, int q);
    
    // component identifier for p (0 to N-1)
    int find(int p);
    
    // number of components
    int count();
}
```

## Quick-find: Java implementation

```java
public class QuickFindUF
{
    private int[] id;
    
    // set id of each object to itself
    // (N array accesses)
    public QuickFindUF(int N)
    {
        id = new int[N];
        for (int i = 0; i < N; i++)
            id[i] = i;
    }
    
    // check whether p and q are in the same component
    // (2 array accesses)
    public boolean connected(int p, int q)
    {
        return id[p] == id[q];
    }
    
    // change all entries with id[p] to id[q]
    // (at most 2N + 2 array accesses)
    public void union(int p, int q)
    {
        int pid = id[p];
        int qid = id[q];
        for (int i = 0; i < id.length; i++)
            if (id[i] == pid)
                id[i] = qid;
    }
}
```

## Quick-union: Java implementation

```java
public class QuickUniondUF
{
    private int[] id;
    
    // set id of each object to itself
    // (N array accesses)
    public QuickFindUF(int N)
    {
        id = new int[N];
        for (int i = 0; i < N; i++) id[i] = i;
    }
    
    // chase parent pointers until reach root
    // (depth of i array accesses)
    private int root(int i)
    {
    	while (i != id[i]) i = id[i];
        return i;
    }
    
    // check whether p and q have same root
    // (depth of p and q array accesses)
    public boolean connected(int p, int q)
    {
        return root(p) == root(q);
    }
    
    // change root of p to point to root of q
    // (depth of p and q array accesses)
    public void union(int p, int q)
    {
        int i = root(p);
        int j = root(q);
        id[i] = j;
    }
}
```

## Weighted quick-union：Java implementation

```java
public class WeightedQuickUniondUF
{
    private int[] id;
    private int[] sz;
    
    // set id of each object to itself
    // (N array accesses)
    public WeightedQuickUniondUF(int N)
    {
        id = new int[N];
        sz = new int[N];
        for (int i = 0; i < N; i++)
        {
            id[i] = i;
            sz[i] = 1;
        }
    }
    
    // chase parent pointers until reach root
    // (depth of i array accesses)
    private int root(int i)
    {
    	while (i != id[i]) i = id[i];
        return i;
    }
    
    // check whether p and q have same root
    // (depth of p and q array accesses)
    public boolean connected(int p, int q)
    {
        return root(p) == root(q);
    }
    
    // change root of p to point to root of q
    // (depth of p and q array accesses)
    public void union(int p, int q)
    {
        int i = root(p);
        int j = root(q);
        if (i == j) return;
        if (sz[i] < sz[j]) { id[i] = j; sz[j] += sz[i]; }
        else			  { id[j] = i; sz[i] += sz[j]; }
    }
}
```

## Path compression: Java implementation

```java
private int root(int i) 
{
    while (i != id[i])
    {
        id[i] = id[id[i]];
        i = id[i];
    }
    return i;
}
```

