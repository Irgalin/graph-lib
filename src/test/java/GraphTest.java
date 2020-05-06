import com.github.irgalin.graph.Graph;
import com.github.irgalin.graph.IEdge;
import com.github.irgalin.graph.IGraph;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit-tests for {@link Graph}.
 */
public class GraphTest {

    @Test
    public void testGetPathWithSimpleEdges() {
        IGraph<Integer> undirectedGraph = new Graph<>(false);
        undirectedGraph.addVertex(0);
        undirectedGraph.addVertex(1);
        undirectedGraph.addVertex(2);
        undirectedGraph.addVertex(3);
        undirectedGraph.addVertex(4);
        undirectedGraph.addVertex(5);

        undirectedGraph.addEdge(0, 1);
        undirectedGraph.addEdge(0, 2);
        undirectedGraph.addEdge(1, 3);
        undirectedGraph.addEdge(1, 4);
        undirectedGraph.addEdge(2, 4);
        undirectedGraph.addEdge(3, 5);

        LinkedList<IEdge<Integer>> path = undirectedGraph.getPath(0, 5);

        assertEquals(path.size(), 3, "The path size must be 3!");
        assertEquals(path.get(0).getDest(), 1, "The first edge must be from '0' to '1' vertex!");
        assertEquals(path.get(1).getDest(), 3, "The second edge must be from '1' to '3' vertex!");
        assertEquals(path.get(2).getDest(), 5, "The third edge must be from '3' to '5' vertex!");
    }

    @Test
    public void testGetPathWithWeightedEdges() {
        IGraph<Integer> graph = new Graph<>(false);
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addVertex(4);
        graph.addVertex(5);
        graph.addEdge(0, 1, 6);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 2, 5);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 5);
        graph.addEdge(2, 4, 1);
        graph.addEdge(3, 4, 3);
        graph.addEdge(3, 5, 1);
        graph.addEdge(4, 5, 1);
        LinkedList<IEdge<Integer>> path = graph.getPath(0, 1);

        assertEquals(path.size(), 5, "The path size must be 5!");
        assertEquals(path.get(0).getDest(), 2, "The first edge must be from '0' to '2' vertex!");
        assertEquals(path.get(1).getDest(), 4, "The second edge must be from '2' to '4' vertex!");
        assertEquals(path.get(2).getDest(), 5, "The third edge must be from '4' to '5' vertex!");
        assertEquals(path.get(3).getDest(), 3, "The fourth edge must be from '5' to '3' vertex!");
        assertEquals(path.get(4).getDest(), 1, "The fifth edge must be from '3' to '1' vertex!");
    }

    @Test
    public void testGetPathWithDirectedGraph() {
        IGraph<Integer> directedGraph = new Graph<>(true);
        directedGraph.addVertex(0);
        directedGraph.addVertex(1);
        directedGraph.addVertex(2);
        directedGraph.addVertex(3);
        directedGraph.addVertex(4);
        directedGraph.addVertex(5);
        directedGraph.addEdge(0, 1);
        directedGraph.addEdge(1, 2);
        directedGraph.addEdge(1, 4);
        directedGraph.addEdge(2, 0);
        directedGraph.addEdge(3, 1);
        directedGraph.addEdge(3, 5);
        directedGraph.addEdge(4, 2);
        directedGraph.addEdge(4, 3);
        directedGraph.addEdge(5, 4);

        LinkedList<IEdge<Integer>> path = directedGraph.getPath(0, 5);

        assertEquals(path.size(), 4, "The path size must be 4!");
        assertEquals(path.get(0).getDest(), 1, "The first edge must be from '0' to '1' vertex!");
        assertEquals(path.get(1).getDest(), 4, "The second edge must be from '0' to '1' vertex!");
        assertEquals(path.get(2).getDest(), 3, "The third edge must be from '0' to '1' vertex!");
        assertEquals(path.get(3).getDest(), 5, "The fourth edge must be from '0' to '1' vertex!");
    }


    @Test
    public void testTraverseGraph() {
        IGraph<TestVertex> undirectedGraph = new Graph<>(false);
        TestVertex vertex = new TestVertex(0);
        TestVertex vertex1 = new TestVertex(1);
        TestVertex vertex2 = new TestVertex(2);
        TestVertex vertex3 = new TestVertex(3);
        TestVertex vertex4 = new TestVertex(4);
        TestVertex vertex5 = new TestVertex(5);
        List<TestVertex> testVerticesList = Arrays.asList(vertex, vertex1, vertex2, vertex3, vertex4, vertex5);

        testVerticesList.forEach(undirectedGraph::addVertex);
        undirectedGraph.addEdge(vertex, vertex1);
        undirectedGraph.addEdge(vertex, vertex2);
        undirectedGraph.addEdge(vertex1, vertex3);
        undirectedGraph.addEdge(vertex2, vertex3);
        undirectedGraph.addEdge(vertex2, vertex4);
        undirectedGraph.addEdge(vertex3, vertex5);

        undirectedGraph.traverseGraph((a) -> a.modifiedByFunction = true);

        testVerticesList.forEach((a) -> assertTrue(a.modifiedByFunction, "The vertex must be modified by function: " + a));
    }

    private static class TestVertex {

        private final int id;

        private boolean modifiedByFunction = false;

        public TestVertex(int id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestVertex that = (TestVertex) o;
            return id == that.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public String toString() {
            return "TestVertex{" +
                    "id=" + id +
                    '}';
        }
    }

}
