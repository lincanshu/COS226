## Flow edge API

```java
public class FlowEdge
{
    // create a flow edge v->w
    FlowEdge(int v, int w, double capacity);
    
    // vertex this edge points from
    int from();
    
    // vertex this edge points to
    int to();
    
    // other endpoint
    int other(int v);
    
    // capacity of this edge
    double capacity();
    
    // flow in this edge
    double flow();
    
    // residual capacity toward v
    double residualCapacityTo(int v);
    
    // add delta flow toward v
    void addResidualFlowTo(int v, double delta);
}
```

## Flow edge : Java implementation

```java
public class FlowEdge
{
    private final int v, w;
    private final double capacity;
    private double flow;
    
    public FlowEdge(int v, int w, double capacity)
    {
        this.v = w;
        this.w = w;
        this.capacity = capacity;
    }
    
    // vertex this edge points from
    int from()
    {
        return v;
    }
    
    // vertex this edge points to
    int to()
    {
        return w;
    }
    
    // other endpoint
    int other(int vertex)
    {
        if (vertex == v) return w;
        else if (vertex == w) return v;
        else throw new RuntimeException("Illegal endpoint");
    }
    
    // capacity of this edge
    double capacity()
    {
        return capacity;
    }
    
    // flow in this edge
    double flow()
    {
        return flow;
    }
    
    // residual capacity toward v
    double residualCapacityTo(int vertex)
    {
        // backward edge
        if (vertex == v) return flow;
        // forward edge
        else if (vertex == w) return capacity - flow;
        else throw new IlledgalArgumentException();
    }
    
    // add delta flow toward v
    void addResidualFlowTo(int vertex, double delta)
    {
        // backward edge
        if (vertex == v) flow -= delta;
        // forward edge
        else if (vertex == w) flow += delta;
        else throw new IllegalArgumentException();
    }
}
```

## Flow network API

```java
public class FlowNetwork
{
    // create an empty flow network with V vertices
    FlowNetwork(int V);
    
    // add flow edge e to this flow network
    void addEdge(FlowEdge e);
    
    // forward and backward edges incident tot v
    Iterable<FlowEdge> adj(int v);
}
```

## Flow network: Java implementation

```java
public class FlowNetwork
{
    private final int V;
    private Bag<FlowEdge>[] adj;
    
    public FlowNetwork(int V)
    {
    	this.V = V;
        adj = (Bag<FlowEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<FlowEdge>();
    }
    
    // add flow edge e to this flow network
    public void addEdge(FlowEdge e)
    {
        int v = e.from();
        int w = e.to();
        adj[v].add(e);
        adj[w].add(e);
    }
    
    // forward and backward edges incident tot v
    public Iterable<FlowEdge> adj(int v)
    {
        return adj[v];
    }
}
```

## Ford-Fulkerson: Java implementation

```java
public class FordFulkerson
{
    private boolean[] marked;
    private FlowEdge[] edgeTo;
    private double value;
    
    public FordFulkerson(FlowNetwork G, int s, int t)
    {
        value = 0.0;
        while (hasAugmentingPath(G, s, t))
        {
            double bottle = Double.POSITIVE_INFINITY;
            for (int v = t; v != s; v = edgeTo[v].other(v))
                bottle = Math.min(bottle, edgeTo[v].residualCapacityTo(v));
            
            for (int v = t; v != s; v = edgeTo[v].other(v))
                edgeTo[v].addResidualFlowTo(v, bottle);
            
            value += bottle;
        }
    }
    
    private boolean hasAugmentingPath(FlowNetwork G, int s, int t)
    {
        edgeTo = new FlowEdge[G.V()];
        marked = new boolean[G.V()];
        
        Queue<Integer>queue = new Queue<Integer>();
        queue.enqueue(s);
        marked[s] = true;
        while (!queue.isEmpty())
        {
            int v = queue.dequeue();
            
            for (FlowEdge e : G.adj(V))
            {
                int w = e.other(v);
                // found path from s to w in the residual network
                if (e.residualCapacityTo(w) > 0 && !marked[w])
                {
                    // save last edge on path to w
                    edgeTo[w] = e;
                    // mark w
                    marked[w] = true;
                    // add w to the queue
                    queue.enqueue(w);
                }
            }
        }
        // is t reachable from s in residual network
        return marked[t];
    }
    
    public double value()
    {
        return value;
    }
    
    public boolean inCut(int v)
    {
        return marked[v];
    }
}
```

