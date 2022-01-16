## Digraph API

```java
public class Digraph
{
    // create an empty digraph with V vertices
    Digraph(int V);
    
    // add a directed edge v to w
    void addEdge(int v, int w);
    
    // vertices pointing from v
    Iterable<Integer> adj(int v);
    
    // number of vertices
    int V();
    
    // number of edges
    int E();
    
    // reverse of thie digraph
    Digraph reverse();
    
    // string representation
    String toString();
}
```

## Adjacency-lists digraph representation: Java implementation

```java
public class Digraph
{
    private final int V;
    // adjacency lists
    private final Bag<Integer>[] adj;
    
    // create an empty digraph with V vertices
    public Digraph(int V)
    {
        this.V = V;
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<Integer>();
    }
    
    // add edge v to w
    public void addEdge(int v, int w)
    {
        adj[v].add(w);
    }
    
    // iterator for vertices pointing from v
    public Iterable<Integer> adj(int v)
    {
        return adj[v];
    }
}
```

## Depth-first search (in directed graphs)

```java
public class DirectedDFS
{
    // true is path from s
    private boolean[] marked;
    
    // constructor marks vertices reachable from s
    public DirectedDFS(Digraph G, int s) 
    {
        marked = new boolean[G.V()];
        dfs(G, s);
    }
    
    // recursive DFS does the work
    private void dfs(Digraph G, int v)
    {
        marked[v] = true;
        for (int w : G.adj(v))
			if (!marked[w]) dfs(G, w);
    }
    
    // client can ask whether any vertex is reachable from s
    public boolean visited(int v)
    {
        return marked[v];
    }
}
```

## Breadth-first search in digraphs application: web crawler

**Goal.** Crawl web, starting from some root web page, say www.princeton.edu

**Solution.** 

- Choose root web page as source s.
- Maintain a Queue of websites to explore.
- Maintain a SET of discovered websites.
- Dequeue the next website and enqueue websites to which it link.

## Bare-bones web crawler: Java implementation

```java
// queue of websites to crawl
Queue<String> queue = new Queue<String>();
// set of marked websites
SET<String> marked = new SET<String>();

// start crawling from root website
String root = "http://www.princeton.edu";
queue.enqueue(root);
marked.add(root);

while (!queue.isEmpty())
{
    // read in raw html from next website in queue
    String v = queue.dequeue();
    StdOut.println(v);
    In in = new In(v);
    String input = in.readAll();
    
    // use regular expression to find all URLs in website of form http://xxx.yyy.zzz
    String regexp = "http://(\\w+\\.)*(\\w+)";
    Pattern pattern = Pattern.compile(regexp);
    Matcher matcher = pattern.matcher(input);
    while (matcher.find())
    {
        String w = matcher.group();
        // if unmarked, mark it and put on the queue
        if (!marked.contains(w))
        {
            marked.add(w);
            queue.enqueue(w);
        }
    }    
}
```

## Precedence scheduling

**Goal.** Given a set of tasks to be completed with precedence consttrains, in which order should we schedule the tasks?

**Digraph model.** vertex = task; edge = precedence constraint.

**DAG.** Directed acyclic graph.

**Topological sort.** Redraw DAG so all edges point upwards.

## Depth-first search order

```java
public class DepthFirstOrder
{
    private boolean[] marked;
    private Stack<Integer> reversePost;
    
    public DepthFirstOrder(Digraph G)
    {
        reversePost = new Stack<Integer>();
        marked = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++)
            if (!marked[v]) dfs(G, v);
    }
    
    private void dfs(Digraph G, int v)
    {
        marked[v] = true;
        for (int w : G.adj(v))
			if (!marked[w]) dfs(G, w);
        reversePost.push(v);
    }
    
    // returns all vertices in "reverse DFS postorder"
    public Iterable<Integer> reversePost()
    {
        return reversePost;
    }
}
```

## Strongly-connected components

**Def.** Vertices v and w are **strongly connected** if there is both a directed path from v to w and a directed path from w to v

**Def.** A **strong component** is a maximal subset of strongly-connected vertices.

## Strong components in a digraph(with two DFS)

```java
public class KosarajuSharirSCC
{
    private boolean marked[];
    private int[] id;
    private int count;
    
    public KosarajuSharirSCC(Digraph G)
    {
        marked = new boolean[G.V()];
        id = new int[G.V()];
        DepthFirstOrder dfs = new DepthFirstOrder(G.reverse());
        for (int v : dfs.reversePost())
        {
            if (!marked[v])
            {
                dfs(G, v);
                count++;
            }
        }
    }
    
    private void dfs(Digraph G, int v)
    {
        marked[v] = true;
        id[v] = count;
        for (int w : G.adj(v))
            if (!marked[w])
                dfs(G, w);
    }
    
    public boolean stronglyConnected(int v, int w)
    {
        return id[v] == id[w];
    }
}
```

