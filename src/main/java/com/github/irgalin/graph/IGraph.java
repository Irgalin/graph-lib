package com.github.irgalin.graph;

import java.util.LinkedList;
import java.util.function.Consumer;

/**
 * Represents a graph of custom defined vertexes connected by {@link IEdge edges} and provides methods to work with it.
 * <p>The graph can be directed and undirected.</p>
 *
 * @param <T> type of vertexes from which the graph will be constructed
 *           (vertexes must override {@link Object#equals(Object)} and {@link Object#hashCode()}).
 */
public interface IGraph<T> {

    /**
     * Adds new vertex to the graph.
     *
     * @param vertex vertex of custom defined type
     *               (must override {@link Object#equals(Object)} and {@link Object#hashCode()}).
     */
    void addVertex(T vertex);

    /**
     * Adds simple (non-weighted) {@link IEdge edge} between existing vertexes in the graph.
     * <p>In case of undirected graph the edge will be traversed from both directions.</p>
     * <p>In case of directed graph the edge will be traversed only
     * from {@code source} to {@code destination} vertex.</p>
     *
     * @param source      an existing in the graph vertex.
     * @param destination an existing in the graph vertex.
     */
    void addEdge(T source, T destination);

    /**
     * Adds weighted {@link IEdge edge} between existing vertexes in the graph.
     * <p>In case of undirected graph the edge will be traversed from both directions.</p>
     * <p>In case of directed graph the edge will be traversed only
     * from {@code source} to {@code destination} vertex.</p>
     *
     * @param source      an existing in the graph vertex.
     * @param destination an existing in the graph vertex.
     */
    void addEdge(T source, T destination, int weight);

    /**
     * Finds the optimal path between two existing in the graph vertices.
     *
     * @return a list of {@link IEdge edges} between two existing vertices.
     */
    LinkedList<IEdge<T>> getPath(T source, T destination);

    /**
     * Traverses the graph and applies the provided function to every vertex in the graph.
     *
     * @param traverseFunction function that will be applied to every vertex in the graph.
     */
    void traverseGraph(Consumer<T> traverseFunction);


}
