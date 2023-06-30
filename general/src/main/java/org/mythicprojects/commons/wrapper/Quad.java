package org.mythicprojects.commons.wrapper;

public class Quad<A, B, C, D> {

    private final A first;
    private final B second;
    private final C third;
    private final D fourth;

    Quad(A first, B second, C third, D fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    public D getFourth() {
        return this.fourth;
    }

    @Override
    public boolean equals(Object to) {
        if (this == to) {
            return true;
        }

        if (to == null || this.getClass() != to.getClass()) {
            return false;
        }

        Quad<?, ?, ?, ?> quad = (Quad<?, ?, ?, ?>) to;
        return super.equals(to) && this.fourth.equals(quad.fourth);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + this.fourth.hashCode();
    }

    @Override
    public String toString() {
        return "['" + this.first + "', '" + this.second + "', '" + this.third + "', '" + this.fourth + "']";
    }

    public static <A, B, C, D> Quad<A, B, C, D> of(A first, B second, C third, D fourth) {
        return new Quad<>(first, second, third, fourth);
    }

}
