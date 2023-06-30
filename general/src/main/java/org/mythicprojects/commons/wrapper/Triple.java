package org.mythicprojects.commons.wrapper;

import java.util.Objects;

public class Triple<A, B, C> {

    private final A first;
    private final B second;
    private final C third;

    Triple(A first, B second, C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public A getFirst() {
        return this.first;
    }

    public B getSecond() {
        return this.second;
    }

    public C getThird() {
        return this.third;
    }

    public <R> Quad<A, B, C, R> with(R newValue) {
        return new Quad<>(this.first, this.second, this.third, newValue);
    }

    @Override
    public boolean equals(Object to) {
        if (this == to) {
            return true;
        }

        if (to == null || this.getClass() != to.getClass()) {
            return false;
        }

        Triple<?, ?, ?> triple = (Triple<?, ?, ?>) to;
        return Objects.equals(this.first, triple.first)
                && Objects.equals(this.second, triple.second)
                && Objects.equals(this.third, triple.third);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.first, this.second, this.third);
    }

    @Override
    public String toString() {
        return "['" + this.first + "', '" + this.second + "', '" + this.third + "']";
    }

    public static <A, B, C> Triple<A, B, C> of(A first, B second, C third) {
        return new Triple<>(first, second, third);
    }

}
