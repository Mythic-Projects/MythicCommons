package org.mythicprojects.commons.function;

import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.mythicprojects.commons.wrapper.Quad;

@FunctionalInterface
public interface ThrowingQuadSupplier <T, U, V, W, E extends Exception> {

    Quad<T, U, V, W> get() throws E;

    static <T, U, V, W, E extends Exception> ThrowingQuadSupplier<T, U, V, W, E> of(@NotNull QuadSupplier<T, U, V, W> supplier) {
        return supplier::get;
    }

    static <T, U, V, W, E extends Exception> ThrowingQuadSupplier<T, U, V, W, E> empty() {
        return () -> null;
    }

    static <T, U, V, W, E extends Exception> ThrowingQuadSupplier<T, U, V, W, E> fail(@NotNull Supplier<? extends E> exceptionSupplier) {
        return () -> {
            throw exceptionSupplier.get();
        };
    }

}
