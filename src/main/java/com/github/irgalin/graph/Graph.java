package com.github.irgalin.graph;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

/**
 * Concurrent implementation of {@link IGraph}.
 */
public class Graph<T> implements IGraph<T> {

    /**
     * Concurrent data structure that represents the graph.
     */
    private final ConcurrentHashMap<T, ConcurrentLinkedQueue<IEdge<T>>> adjacencyMap;

    private final boolean directed;

    /**
     * Initializes directed or undirected graph.
     *
     * @param directed if this param is set to {@code true} the constructor initializes a directed graph,
     *                 otherwise it initializes an undirected graph.
     */
    public Graph(boolean directed) {
        this.directed = directed;
        this.adjacencyMap = new ConcurrentHashMap<>();
    }

    @Override
    public void addVertex(T vertex) {
        if (vertex != null && !adjacencyMap.containsKey(vertex)) {
            ConcurrentLinkedQueue<IEdge<T>> edges = new ConcurrentLinkedQueue<>();
            adjacencyMap.put(vertex, edges);
        }
    }

    @Override
    public void addEdge(T source, T destination) {
        addEdge(source, destination, 1);
    }

    @Override
    public void addEdge(T source, T destination, int weight) {
        if (source != null && destination != null &&
                adjacencyMap.containsKey(source) && adjacencyMap.containsKey(destination)) {
            IEdge<T> newSrcVertexEdge = new Edge<>(source, destination, weight);
            Queue<IEdge<T>> srcVertexEdges = adjacencyMap.get(source);
            srcVertexEdges.add(newSrcVertexEdge);
            if (!directed) {
                Queue<IEdge<T>> destVertexEdges = adjacencyMap.get(destination);
                Edge<T> newDestVertexEdge = new Edge<>(destination, source, weight);
                destVertexEdges.add(newDestVertexEdge);
            }
        }
    }

    @Override
    public LinkedList<IEdge<T>> getPath(T source, T destination) {
        if (source == null || destination == null) {
            return new LinkedList<>();
        }
        Map<T, VertexMinDistance<T>> vertexMinDistanceMap = new HashMap<>(adjacencyMap.size());
        PriorityQueue<VertexMinDistance<T>> sortedByMinDistanceQueue = new PriorityQueue<>(adjacencyMap.size());
        for (T vertex : adjacencyMap.keySet()) {
            vertexMinDistanceMap.put(vertex, new VertexMinDistance<>(vertex, Integer.MAX_VALUE));
        }
        vertexMinDistanceMap.get(source).minDistance = 0;
        sortedByMinDistanceQueue.offer(vertexMinDistanceMap.get(source));
        while (!sortedByMinDistanceQueue.isEmpty()) {
            VertexMinDistance<T> currentVertexMinDistance = sortedByMinDistanceQueue.poll();
            if (currentVertexMinDistance.vertex.equals(destination)) {
                return currentVertexMinDistance.shortestPath;
            }
            if (!currentVertexMinDistance.visited) {
                currentVertexMinDistance.visited = true;
                calculateDistancesToNeighbors(currentVertexMinDistance, sortedByMinDistanceQueue, vertexMinDistanceMap);
            }
        }
        return new LinkedList<>();
    }

    private void calculateDistancesToNeighbors(VertexMinDistance<T> currentVertexMinDistance,
                                               PriorityQueue<VertexMinDistance<T>> sortedByMinDistanceQueue,
                                               Map<T, VertexMinDistance<T>> vertexMinDistanceMap) {
        Queue<IEdge<T>> edges = adjacencyMap.get(currentVertexMinDistance.vertex);
        for (IEdge<T> edge : edges) {
            T neighborVertex = edge.getDest();
            VertexMinDistance<T> neighborVertexMinDistance = vertexMinDistanceMap.get(neighborVertex);
            if (!neighborVertexMinDistance.visited) {
                int newNeighborMinDistance = currentVertexMinDistance.minDistance + edge.getWeight();
                int oldNeighborMinDistance = neighborVertexMinDistance.minDistance;
                if (oldNeighborMinDistance > newNeighborMinDistance) {
                    neighborVertexMinDistance.minDistance = newNeighborMinDistance;
                    neighborVertexMinDistance.shortestPath.clear();
                    neighborVertexMinDistance.shortestPath.addAll(currentVertexMinDistance.shortestPath);
                    neighborVertexMinDistance.shortestPath.add(edge);
                    sortedByMinDistanceQueue.offer(neighborVertexMinDistance);
                }
            }
        }
    }

    @Override
    public void traverseGraph(Consumer<T> traverseFunction) {
        if (traverseFunction == null) {
            return;
        }
        Queue<T> vertexQueue = new LinkedList<>();
        Map<T, Boolean> visitedVertexesMap = new HashMap<>();
        Map.Entry<T, ConcurrentLinkedQueue<IEdge<T>>> entry = adjacencyMap.entrySet().iterator().next();
        vertexQueue.offer(entry.getKey());
        while (!vertexQueue.isEmpty()) {
            T currentVertex = vertexQueue.poll();
            if (visitedVertexesMap.get(currentVertex) != null && visitedVertexesMap.get(currentVertex)) {
                continue;
            }
            visitedVertexesMap.put(currentVertex, true);
            traverseFunction.accept(currentVertex);
            ConcurrentLinkedQueue<IEdge<T>> edges = adjacencyMap.get(currentVertex);
            for (IEdge<T> edge : edges) {
                vertexQueue.offer(edge.getDest());
            }
        }

    }

    /**
     * Represents the minimum distance from some source vertex to the given vertex.
     *
     * @param <T> the given vertex type.
     */
    private static class VertexMinDistance<T> implements Comparable<VertexMinDistance<T>> {

        /**
         * Any given vertex in the graph.
         */
        private final T vertex;
        /**
         * The minimum possible distance (sum of edge weights) between some source vertex and given vertex.
         */
        private Integer minDistance;
        /**
         * The shortest path (with minimum weight) between some source vertex and given vertex.
         */
        private LinkedList<IEdge<T>> shortestPath;

        /**
         * Shows if the given vertex's been already traversed or not.
         */
        private boolean visited;

        public VertexMinDistance(T vertex, int minDistance) {
            this.vertex = vertex;
            this.minDistance = minDistance;
            this.shortestPath = new LinkedList<>();
        }

        @Override
        public int compareTo(VertexMinDistance anotherVertexMinDistance) {
            return this.minDistance.compareTo(anotherVertexMinDistance.minDistance);
        }

    }

}
