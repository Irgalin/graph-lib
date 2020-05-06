# Simple graph library

Library that allows to create a simple concurrent data structure that represents a graph and perform operations on it.

## Building the library jar using Maven

From the project folder (which contains pom.xml) run the following command:

```mvn package```

The jar file will be created in the ``target`` folder:

```{project_dir}/target/graph-lib-0.0.1.jar```

Add the library jar to your project to start using it.

## Usage example

```
import com.github.irgalin.graph.Graph;
import com.github.irgalin.graph.IEdge;
import com.github.irgalin.graph.IGraph;

import java.util.LinkedList;

public class GraphUsageExample {

    public static void main(String[] args) {
    
        // initializing an undirected graph with the integer type vertexes
        IGraph<Integer> undirectedGraph = new Graph<>(false);
        
        // adding vertexes to the graph
        undirectedGraph.addVertex(0);
        undirectedGraph.addVertex(1);
        undirectedGraph.addVertex(2);
        undirectedGraph.addVertex(3);
        undirectedGraph.addVertex(4);
        undirectedGraph.addVertex(5);
        
        // connecting existing vertexes with simple (non-weighted) edges
        undirectedGraph.addEdge(0, 1);
        undirectedGraph.addEdge(0, 2);
        undirectedGraph.addEdge(1, 3);
        undirectedGraph.addEdge(1, 4);
        undirectedGraph.addEdge(2, 4);
        undirectedGraph.addEdge(3, 5);
        
        // getting and printing the optimal path between two existing in the graph vertices
        LinkedList<IEdge<Integer>> path = undirectedGraph.getPath(0, 5);
        System.out.println(path);
        
        // traversing every vertex and applying the given function (prints every vertex in console)
        undirectedGraph.traverseGraph(System.out::println);
    }
    
}
```
