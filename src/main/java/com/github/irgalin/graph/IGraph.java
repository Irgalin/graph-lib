package com.github.irgalin.graph;

import java.util.LinkedList;
import java.util.function.Consumer;

/**
 *
 * @param <T>
 */
public interface IGraph<T> {

    /**
     * Adds vertex to the graph
     */
    void addVertex(T vertex);

    /**
     * Adds edge to the graph
     */
    void addEdge(T source, T destination);

    /**
     * Adds weighted edge to the graph
     */
    void addEdge(T source, T destination, int weight);

    /**
     * @return a list of edges between 2 vertices (path doesn’t have to be optimal)
     */
    LinkedList<IEdge<T>> getPath(T source, T destination);

    /**
     *
     * @param consumer
     */
    void traverseGraph(Consumer<T> consumer);


}
