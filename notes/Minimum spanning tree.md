## Minimum spanning tree

**Given.** Undirected graph G with positive edge weights (connected).

**Def.** A **spanning tree** of G is a subgraph T that is both a **tree** (connected and acyclic) and **spanning** (includes all of the vertices).

**Goal.** Find a min weight spanning tree.

## Cut property

**Def.** A **cut** in a graph is a partition of its vertices into two (nonempty) sets.

**Def.** A **crossing edge** connects a vertex in one set with a vertex in the other.

**Cut property.** Given any cut, the crossing edge of min weight is in the MST.

## Weighted edge API

```java
public class Edge implements Comparable<Edge>
{
    // create a weighted edge v-w
    Edge(int v, int w, double weight);
    
    // either endpoint
    int either();
    
    // the endpoint that's not v
    int other(int v);
    
    // compare this edge to that edge
    int compareTo(Edge that);
    
    // the weight
    double weight();
    
    // string representation
    String toString();
}
```

## Weighted edge: Java implementation

```java
public class Edge implements Comparable<Edge>
{
    private final int v, w;
    private final double weight;
    
    // constructor
    public Edge(int v, int w, double weight)
    {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }
    
    // either endpoint
    public int either()
    {
        return v;
    }
    
    // other endpoint
    public int other(int vertex)
    {
        if (vertex == v) return w;
        else return v;
    }
    
    // compare edges by weight
    int compareTo(Edge that)
    {
        if 	    (this.weight < that.weight) return -1;
        else if (this.weight > that.weight) return +1;
        else                                return  0;
    }
}
```

## Edge-weighted graph API

```java
public class EdgeWeightedGraph
{
    // create an empty graph with V vertices
    EdgeWeightedGraph(int V);
    
    // add weighted edge e to this graph
    void addEdge(Edge e);
    
    // edges incident to v
    Iterable<Edge> adj(int v);
}
```

## Edge-weighted graph: adjacency-lists implementation

```java
public class EdgeWeightedGraph
{
    private final int V;
    private final Bag<Edge>[] adj;
    
    public EdgeWeightedGraph(int V)
    {
        this.V = V;
        adj = (Bag<Edge>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<Edge>();
    }
    
    // add edge to both adjacency lists
    public void addEdge(Edge e)
    {
        int v = e.either(), w = e.other(v);
        adj[v].add(e);
        adj[w].add(e);
    }
    
    public Iterable<Edge> adj(int v)
    {
        return adj[v];
    }
}
```

## Minimum spanning tree API

```java
public class MST
{
    // construct
    MST(EdgeWeightedGraph G);
    
    // edges in MST
    Iterable<Edge> edges();
    
    double weight();
}
```

## Krsukal's algorithm: Java implementation

```java
public class KruskalMST
{
    private Queue<Edge> mst = new Queue<Edge>();
    
    public KruskalMST(EdgeWeightedGraph G)
    {
        MinPQ<Edge> pq = new MinPQ<Edge>();
        for (Edge e : G.edges())
            pq.insert(e);
        
        UF uf = new UF(G.V());
        while (!pq.isEmpty() && mst.size() < G.V() - 1)
        {
            Edge e = pq.delMin();
            int v = e.either(), w = e.other(v);
            if (!uf.connected(v, w))
            {
                uf.union(v, w);
                msn.enqueue(e);
            }
        }
    }
    
    public Iterable<Edge> edges()
    {
        return mst;
    }
}
```

## Prim's algorithm: lazy implementation

```java
public class LazyPrimMST
{
    private boolean[] marked;
    private Queue<Edge> mst;
    private MinPQ<Edge> pq;
    
    public LazyPrimMST(WeightedGraph G)
    {
        pq = new MinPQ<Edge>();
        mst = new Queue<Edge>();
        marked = new boolean[G.V()];
        visit(G, 0);
        
        while (!pq.isEmpyty() && mst.size() < G.V() - 1)
        {
            // repearedly delete the min weight edge e = v-w from PQ
            Edge e = pq.delMin();
            int v = e.either(), w = e.other(v);
            // ignore if both endpoints in T
            if (marked[v] && marked[w]) continue;
            // add edge e to tree
            mst.enqueue(e);
            // add v or w to tree
            if (!marked[v]) visit(G, v);
            if (!marked[w]) visit(G, w);
        }   
    }
    
    private void visit(WeightedGraph G, int v)
    {
        // add v to T
    	marked[v] = true;
        // for each edge e=v-w, add to PQ if w not already in T
        for (Edge e : G.adj(v))
            if (!marked[e.other(v)])
                pq.insert(e);
    }
    
    public Iterable<Edge> mst()
    {
        return mst;
    }
}
```

