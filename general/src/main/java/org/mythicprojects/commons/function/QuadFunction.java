package org.mythicprojects.commons.function;

import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface QuadFunction<T, U, V, W, R> {

    R apply(T t, U u, V v, W w);

    static <T, U, V, W, R> QuadFunction<T, U, V, W, R> empty() {
        return (t, u, v, w) -> null;
    }

    static <T, U, V, W, R> QuadFunction<T, U, V, W, R> fail(@NotNull Supplier<? extends RuntimeException> exceptionSupplier) {
        return (t, u, v, w) -> {
            throw exceptionSupplier.get();
        };
    }

}
