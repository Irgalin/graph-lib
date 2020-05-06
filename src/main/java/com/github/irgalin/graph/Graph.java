package com.github.irgalin.graph;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class Graph<T> implements IGraph<T> {

    private final Map<T, ConcurrentLinkedQueue<IEdge<T>>> adjacencyMap;

    private final boolean directed;

    public Graph(boolean directed) {
        this.directed = directed;
        this.adjacencyMap = new ConcurrentHashMap<>();
    }

    @Override
    public void addVertex(T vertex) {
        if (!adjacencyMap.containsKey(vertex)) {
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
        if (adjacencyMap.containsKey(source)) {
            IEdge<T> edge = new Edge<T>(source, destination, weight);
            Queue<IEdge<T>> edges = adjacencyMap.get(source);
            edges.add(edge);
        }
        if (!directed) {
            if (adjacencyMap.containsKey(destination)) {
                Queue<IEdge<T>> edges = adjacencyMap.get(destination);
                Edge<T> edge = new Edge<T>(destination, source, weight);
                edges.add(edge);
            }
        }
    }

    @Override
    public LinkedList<IEdge<T>> getPath(T source, T destination) {
        Map<T, VertexMinDistance<T>> vertexMinDistanceMap = new HashMap<>(adjacencyMap.size());
        PriorityQueue<VertexMinDistance<T>> sortedByMinDistanceQueue = new PriorityQueue<>(adjacencyMap.size());
        for (T vertex : adjacencyMap.keySet()) {
            vertexMinDistanceMap.put(vertex, new VertexMinDistance<T>(vertex, Integer.MAX_VALUE));
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
            T neighborVertex = (T) edge.getDest();
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
    public void traverseGraph(Consumer<T> consumer) {
        Queue<T> vertexQueue = new LinkedList<>();
        Map<T, Boolean> visitedMap = new HashMap<>();
        Map.Entry<T, ConcurrentLinkedQueue<IEdge<T>>> entry = adjacencyMap.entrySet().iterator().next();
        vertexQueue.offer(entry.getKey());
        while (!vertexQueue.isEmpty()) {
            T currentVertex = vertexQueue.poll();
            if (visitedMap.get(currentVertex) != null && visitedMap.get(currentVertex)) {
                continue;
            }
            visitedMap.put(currentVertex, true);
            consumer.accept(currentVertex);
            ConcurrentLinkedQueue<IEdge<T>> edges = adjacencyMap.get(currentVertex);
            for (IEdge<T> edge : edges) {
                vertexQueue.offer(edge.getDest());
            }
        }

    }

    /**
     *
     * @param <T>
     */
    private static class VertexMinDistance<T> implements Comparable<VertexMinDistance<T>> {

        private final T vertex;

        private Integer minDistance;

        private LinkedList<IEdge<T>> shortestPath;

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
