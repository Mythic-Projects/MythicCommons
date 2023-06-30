package org.mythicprojects.commons.function;

import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface TriFunction<T, U, V, R> {

    R apply(T t, U u, V v);

    static <T, U, V, R> TriFunction<T, U, V, R> empty() {
        return (t, u, v) -> null;
    }

    static <T, U, V, R> TriFunction<T, U, V, R> fail(@NotNull Supplier<? extends RuntimeException> exceptionSupplier) {
        return (t, u, v) -> {
            throw exceptionSupplier.get();
        };
    }

}
