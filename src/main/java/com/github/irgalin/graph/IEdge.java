package com.github.irgalin.graph;

/**
 *
 * @param <T>
 */
public interface IEdge<T> {

    /**
     *
     * @return
     */
    T getSource();

    /**
     *
     * @return
     */
    T getDest();

    /**
     *
     * @return
     */
    int getWeight();

}
