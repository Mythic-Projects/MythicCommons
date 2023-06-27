package org.mythicprojects.commons.util;

import java.util.Objects;

public class Pair<A, B> {

    protected final A first;
    protected final B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public static <A, B> Pair<A, B> of(A first, B second) {
        return new Pair<>(first, second);
    }

    public <R> Pair<R, B> withFirst(R newValue) {
        return new Pair<>(newValue, this.second);
    }

    public <R> Pair<A, R> withSecond(R newValue) {
        return new Pair<>(this.first, newValue);
    }

    public A getFirst() {
        return this.first;
    }

    public B getSecond() {
        return this.second;
    }

    @Override
    public boolean equals(Object to) {
        if (this == to) {
            return true;
        }

        if (to == null || this.getClass() != to.getClass()) {
            return false;
        }

        Pair<?, ?> pair = (Pair<?, ?>) to;
        return Objects.equals(this.first, pair.first) && Objects.equals(this.second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.first, this.second);
    }

    @Override
    public String toString() {
        return "['" + this.first + "', '" + this.second + "']";
    }

}