package org.mythicprojects.commons.function;

import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ThrowingQuadFunction<T, U, V, W, R, E extends Throwable> {

    R apply(T t, U u, V v, W w) throws E;

    static <T, U, V, W, R, E extends Throwable> ThrowingQuadFunction<T, U, V, W, R, E> of(@NotNull QuadFunction<T, U, V, W, R> function) {
        return function::apply;
    }

    static <T, U, V, W, R, E extends Throwable> ThrowingQuadFunction<T, U, V, W, R, E> empty() {
        return (t, u, v, w) -> null;
    }

    static <T, U, V, W, R, E extends Throwable> ThrowingQuadFunction<T, U, V, W, R, E> fail(@NotNull Supplier<? extends E> exceptionSupplier) {
        return (t, u, v, w) -> {
            throw exceptionSupplier.get();
        };
    }

}
