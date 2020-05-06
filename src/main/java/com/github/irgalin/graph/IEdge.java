package com.github.irgalin.graph;

/**
 * Represents weighted edge between two vertexes in a {@link IGraph graph}.
 *
 * @param <T> type of vertexes that the edge connects.
 */
public interface IEdge<T> {

    /**
     * @return source vertex.
     */
    T getSource();

    /**
     * @return destination vertex.
     */
    T getDest();

    /**
     * @return edge weight.
     */
    int getWeight();

}
