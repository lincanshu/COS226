**Hash function.** Method for computing array index from key.

**Requirement. ** If x.equals(y), then (x.hashCode() == y.hashCode()).

**Highly desirable.** If !x.equals(y), then (x.hashCode() != y.hashCode()).

## Implementing hash code: strings

```java
public final class String
{
    private final char[] s;
    ...
    
    public int hashCode()
    {
        int hash = 0;
        for (int i = 0; i < length(); i++)
            hash = s[i] + (31 * hash);
        return hash;
    }
}
```

## Implementing hash code: user-defined types

```java
public final class Transaction implements Comparable<Transaction>
{
    private final String who;
    private final Date	 when;
    private final double amount;
    
    public Transaction(String who, Date when, double amount)
    {	/* as before */	}
    
    public boolean equals(Object y)
    {	/* as before */ }
    
    public int hashCode()
    {
        int hash = 17;
        hash = 31*hash + who.hashCode();
        hash = 31*hash + when.hashCode();
        hash = 31*hash + ((Double) amount).hashCode();
        return hash;
    }
}
```

## Hash code design

- Combine each significant field using the 31x+y rule.
- If field is a primitive type, use wrapper type hashCode().
- If field is null, return 0.
- If field is a reference type, use hashCode(). // applies rule recursively
- If field is an array, apply to each entry. // or use Arrays.deepHashCode()

### Modular hashing

**Hash code.** An int between -2^31 and 2^31 - 1.

**Hash function.** An int between 0 and M - 1.

```java
// bug
private int hash(Key key)
{
    return key.hashCode() % M;
}
// 1-in-a-billion bug
private int hash(Key key)
{
    return Math.abs(key.hashCode()) % M;
}
// correct
private int hash(Key key)
{
    return (Key.hashCode() & 0x7fffffff) % M;
}
```

### Separate chaining ST: Java implementation

```java
public class SeparateChainingHashST<Key, Value>
{
    private int M = 97;
    private Node[] st = new Node[M];
    
    private static class Node
    {
        private Object key;
        private Object val;
        private Node next;
        Node(Object key, Object val, Node next) 
        {
            this.key = key;
            this.val = val;
            this.next = next;
        }
    }
    
    private int hash(Key key)
    {
        return (key.hashCode() & 0x7fffffff) % M;
    }
    
    public Value get(Key key)
    {
        int i = hash(key);
        for (Node x = st[i]; x != null; x = x.next)
        {
            if (key.equals(x.key)) 
            {
                return (Value)x.val;
            }
        }
        return null;
    }
    
    public void put(Key key, Value val) 
    {
        int i = hash(key);
        for (Node x = st[i]; x != null; x = x.next)
        {
            if (key.equals(x.key)) {
                x.val = val;
                return;
            }
        }
        st[i] = new Node(key, val, st[i]);
    }
}
```

## Linear probing hash table

**Hash.** Map key to integer i between 0 and M-1.

**Insert.** Put at table index i if free; if not try i+1, i+2, etc.

**Search.** Search table index i; if occupied but not match, try i+1, i+2, etc.

**Note.** Array size M must be greater than number of key-value pairs N.

```java
public class LinearProbingHashST<Key, Value>
{
    private int M = 30001;
    private Value[] vals = (Value[]) new Object[M];
    private Key[] 	keys = (Key[])	 new Object[M];
    
    private int hash(Key key) 
    {
        return (key.hashCode() & 0x7fffffff) % M;
    }
    
    public void put(Key key, Value val)
    {
        int i;
        for (i = hash(key); key[i] != null; i = (i+1) % M)
        {
            if (keys[i].eqauls(key))
            {
                break;
            }
        }            
        keys[i] = key;
        vals[i] = val;
    }
    
    public Value get(Key key)
    {
        for (int i = hash(key); keys[i] != null; i = (i+1) % M)
        {
            if (key.equals(keys[i])) 
            {
                return vals[i];
            }
        }
        return null;
    }
}
```

