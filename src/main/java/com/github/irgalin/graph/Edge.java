package com.github.irgalin.graph;

import java.util.Objects;

public class Edge<T> implements IEdge {

    private T source;

    private T dest;

    private int weight;

    public Edge(T source, T dest) {
        this.source = source;
        this.dest = dest;
        this.weight = 1;
    }

    public Edge(T source, T dest, int weight) {
        this.source = source;
        this.dest = dest;
        this.weight = weight;
    }

    public T getSource() {
        return source;
    }

    public T getDest() {
        return dest;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge<?> edge = (Edge<?>) o;
        return weight == edge.weight &&
                source.equals(edge.source) &&
                dest.equals(edge.dest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, dest, weight);
    }

    @Override
    public String toString() {
        return "Edge{" +
                "source=" + source +
                ", dest=" + dest +
                ", weight=" + weight +
                '}';
    }
}
