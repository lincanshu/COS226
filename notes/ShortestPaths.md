## Weighted directed edge API:

```java
public class DirectedEdge
{
    // weighted edge v->w
    DirectedEdge(int v, int w, double weight);
    
    // vertex v
    int from();
    
    // vertex w
    int to();
    
    // weight of this edge
    double weight();
    
    // string representation
    String toString();
}
```

## Weighted directed edge: implementation in Java

```java
public class DirectedEdge
{
    private final int v, w;
    private final double weight;
    
    public DirectedEdge(int v, int w, double weight)
    {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }
    
    public int from()
    {
        return v;
    }

    public int to()
    {
        return w;
    }
    
    public double weight()
    {
        return weight;
    }
}
```

## Edge-weighted digraph API

```java
public class EdgeWeightedDigraph
{
    // edge-weighted digraph with V vertices
    EdgeWeightedDigraph(int V);
    
    // add weighted directed edge e
    void addEdge(DirectedEdge e);
    
    // edges pointing from v
    Iterable<DirectedEdge> adj(int v);
    
    // number of vertices
    int V();
    
    // number of edges
    int E();
}
```

## Edge-weighted digraph: adjacency-lists implementation in Java

```java
public class EdgeWeightedDigraph
{
    private final int V;
    private final Bag<DirectedEdge>[] adj;
    
    public EdgeWeightedDigraph(int V)
    {
        this.V =V;
        adj = (Bag<DirectedEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<DirectedEdge>();
    }
    
    // add edge e = v->w to only v's adjacency list
    public void addEdge(DirectedEdge e)
    {
        int v = e.from();
        adj[v].add(e);
    }
    
    public Iterable<DirectedEdge> adj(int v)
    {
        return adj[v];
    }
}
```

## Single-source shortest paths API:

```java
public class SP
{
    // shortest paths from s in graph G
    SP(EdgeWeightedDigraph G, int s);
    
    // length of shortest path from s to v
    double distTo(int v);
    
    // shortest path from s to v
    Iterable<DirectedEdge> pathTo(int v);
    
    // is there a path from s to v?
    boolean hashPathTo(int v);
}
```

## Data structure for single-source shortest paths

**Goal.** Find the shortest path from s to every other vertex.

**Observation.** A **shortest-paths tree** (SPT) solution exists.

**Consequence.** Can represent the SPT with two vertex-index arrays:

- distTo[v] is length of shortest path from s to v.
- edgeTo[v] is last edge on shortest path form s to v.

```java
public double distTo(int v)
{
    return distTo[v];
}

public Iterable<DirectedEdge> pathTo(int v)
{
    Stack<DirectedEdge> path = new Stack<DirectedEdge>();
    for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()])
        path.push(e);
   	return path;
}
```

## Edge relaxation

**Relax edge e = v->w.**

- distTo[v] is length of shortest **known** path from s to v.
- distTo[w] is length of shortest **known** path from s to w.
- edgeTo[w] is last edge on shortest **known** path from s to w.
- If e = v->w gives shortest path to w through v, update both distTo[w] and edgeTo[w].

```java
public void relax(DirectedEdge e)
{
    int v = e.from(), w = e.to();
    if (distTo[w] > distTo[v] + e.weight())
    {
        distTo[w] = distTo[v] + e.weight();
        edgeTo[w] = e;
    }
}
```

**Generic shortest-paths algorithm**

```markdown
Generic algorithm(to compute SPT from s)：
	Initialize distTo[s] = 0 and distTo[v] = ∞ for all vertices.
	Repeat untill optimality conditions are satisfied:
	- Relax any edge.
```

## Dijkstra's algorithm: Java implementation

```java
public class DijkstraSP
{
    private DirectedEdge[] edgeTo;
    private double[] distTo;
    private IndexMinPQ<Doubel> pq;
    
    public DijkstraSP(EdgeWeightedDigraph G, int s)
    {
        edgeTo = new DirectedEdge[G.V()];
        distTo = new double[G.V()];
        pq = new IndexMinPQ<Double>(G.V());
        
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;
        
        // relax vertices in order of distance from s
        pq.insert(s, 0.0);
        while (!pq.isEmpty()) 
        {
            int v = pq.delMin();
            for (DirectedEdge e : G.adj(V))
                relas(e);
        }
    }
    
    private void relax(DirectedEdge e)
    {
        int v = e.from(), w = e.to();
        if (distTo[w] > distTo[v] + e.weight())
        {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
            if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
            else 			   pq.insert	 (w, distTo[w]);
        }
    }
}
```

## Shortest paths in edge-weighted DAGs

```java
public class AcyclicSP
{
    private DirectedEdge[] edgeTo;
    private double[] distTo;
    
    public AcyclicSP(EdgeWeightedDigraph G, int s)
    {
        edgeTo = new DirectedEdge[G.V()];
        distTo = new double[G.V()];
        
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0;
        
        // topological order
        Topological topological = new Topological(G);
        for (int v : topological.order())
            for (DirectedEdge e : G.adj(v))
                relax(e);
    }
}
```

## Bellman-Ford algorithm

```java
for (int i = 0; i < G.V(); i++)
    for (int v = 0; v < G.V(); v++) 
        for (DirectedEdge e : G.adj(v))
            relax(e);
```

