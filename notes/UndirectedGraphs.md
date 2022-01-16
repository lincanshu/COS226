## Graph API

```java
public class Graph
{
    // create an empty graph with V vertices
    Graph(int V);
    
    // create a graph from input stream
    Graph(In in);
    
    // add an edge v-w
    void addEdge(int v, int w);
    
    // vertices adjacent to v
    Iterable<Integer> adj(int v);
    
    // number of vertices
    int V();
    
    // number of edges
    int E();
    
    // string representation
    String toString();
}
```

## Typical graph-processing code

```java
// compute the degree of v
public static int degree(Graph G, int v)
{
    int degree = 0;
    for (int w : G.adj()) degree++;
    return degree;
}

// compute maximum degree
public static int maxDegree(Graph G)
{
    int max = 0;
    for (int v = 0; v < G.V(); v++)
        if (degree(G, v) > max)
            max = degree(G, v);
    return max;
}

// compute average degree
public static double averageDegree(Graph G)
{
    return 2.0 * G.E() / G.V(); 
}

// count self-loops
public static int numberOfSelfLoops(Graph G)
{
    int count = 0;
    for (int v = 0; v < G.V(); v++)
        for (int w : G.adj(V))
            if (v == w) count++;
    return count/2; // each edge counted twice
}
```

## Adjacency-list graph representation: Java implementation

```java
public class Graph
{
    private final int V;
    private Bag<Integer>[] adj;
   
    public Graph(int V)
    {
        this.V = V;
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<Integer>();
    }
    
    // add edge v-w
    // parallel edges and self-loops allowed
    public void addEdge(int v, int w)
    {
        adj[v].add(w);
        adj[w].add(v);
    }
    
    // iterator for vertices adjacent to v
    public Iterable<Integer> adj(int v)
    {
        return adj[v];
    }
}
```

## Graph representations

**In practice.** Use adjacency-lists representation.

- Algorithms based on iterating over vertices adjacent to v.
- Real-world graphs tend to be sparse.

| representation   | space | add edge | edge between v and w? | iterate over vertices adjacent to v? |
| ---------------- | ----- | -------- | --------------------- | ------------------------------------ |
| list of edges    | E     | 1        | E                     | E                                    |
| adjacency matrix | V^2   | 1        | 1                     | V                                    |
| adjacency lists  | E+V   | 1        | degree(V)             | degree(V)                            |

## Depth-first search

```markdown
DFS(to visit a vertex v):
	Mark v as visited.
	Recursively visit all unmarked vertices w adjacent to v.
```

**Typical applications.**

- Find all vertices connected to a given source vertex.
- Find a path between two vertices.

## Design pattern for graph processing

**Design pattern.** Decouple graph data type from graph processing.

- Create a Graph object.
- Pass the Graph to a graph-processing routine.
- Query the graph-processing routine for information.

```java
public class Paths
{
    // find paths in G from source s
    Paths(Graph G, int s);
    
    // is there a path from s to v
    boolean hasPathTo(int v);
    
    // path from s to v; null if no such path
    Iterable<Integer> pathTo(int v);
}
```

```java
public class DepthFirsePaths
{
    // marked[v] = true if v connected to s
    private boolean[] marked;
    // edgeTo[v] = previous vertex on path from s to v
    private int[] edgeTo;
    private int s;
    
    public DepthFirstPaths(Graph G, int s)
    {
        // initialize data structures
        ...
        // find vertices connected to s
        dfs(G, s);
    }
    
    // recursive DFS does the work
    private void dfs(Graph G, int v)
    {
        marked[v] = true;
        for (int w : G.adj(v))
            if (!marked[w])
            {
                dfs(G, w);
                edgeTo[w] = v;
            }
    }
    
    // iterative
    private void dfs(Graph G, int v)
    {
        Stack<Integer> stack = new Stack<>();
        stakc.push(v);
        while (!stack.isEmpty()) {
            v = stack.pop();
            marked[v] = true;
            for (int w : G.adj(v))
            	if (!marked[w])
                {
                    stack.push(w);
                    edgeTo[w] = v;
                }
        }
    }
    
    public boolean hashPath(int v)
    {
        return marked[v];
    }
    
    public Iterable<Integer> pathTo(int v)
    {
        if (!hashPath(V)) return null;
        Stack<Integer> path = new Stack<Integer>();
        for (int x = v; x != s; x = edgeTo[x])
            path.push(x);
        path.push(s);
        return path;
    }
}
```

## Breadth-first search

```markdown
BFS(from source vertex s):
	PUT s onto a FIFO queue, and mark s as visited.
	Repeat until the queue is empty:
	- remove the least recently added vertex v
	- add each of v's unvisited neighbors to the queue, and mark them as visited
```

```java
public class BreadthFirstPaths
{
    private boolean[] marked;
    private int[] edgeTo;
    ...
    
    private void bfs(Graph G, int s)
    {
        Queue<Integer> q = new Queue<Integer>();
        q.enqueue(s);
        marked[s] = true;
        while (!q.isEmpty())
        {
            int v = queue.dequeue();
            for (int w : G.adj(v))
            {
                if (!marked[w])
                {
                    q.enqueue(w);
                    marked[w] = true;
                    edgeTo[w] = v;
                }
            }
        }
    }
}
```

## Connectivity queries

**Def.** Vertices v and w are connected if there is a path between them.

**Goal.** Preprocess graph to answer queries of the from is v connected to w? in constant time.

```java
public class CC
{
    // find connected components in G
    CC(Graph G);
    
    // are v and w connected?
    boolean connected(int v, int w);
    
    // number of connected components
    int count();
    
    // component identifier for v
    int id(int v);
}
```

## Connected components

**Def.** A connected component is a maximal set of connected vertices.

```markdown
Connected componets:
	Initialize all vertices v as unmarked
	
	For each unmarked vertex v, run DFS to identify all 
	vertices discovered as part of the same component.
```

```java
public class CC
{
    private boolean[] marked;
    // id[v] = id of component containing v
    private int[] id;
    // number of components
    private int count;
    
    public CC(Graph G)
    {
        marked = new boolean[G.V()];
        id = new int[G.V()];
        for (int v = 0; v < G.V(); v++)
        {
            // run DFS from one vertex in each component
            if (!marked[v])
            {
                dfs(G, v);
                count++;
            }
        }
    }
    
    public int count()
    {
        return count;
    }
    
    public int id(int v)
    {
        return id[v];
    }
    
    private void dfs(Graph G, int v)
    {
        marked[v] = true;
        // all vertices discovered in same call of dfs have same id
        id[v] = count;
        for (int w : G.adj(v))
            if (!marked[w])
                dfs(G, w);
    }
}
```



