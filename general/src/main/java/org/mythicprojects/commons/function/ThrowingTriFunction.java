package org.mythicprojects.commons.function;

import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ThrowingTriFunction <T, U, V, R, E extends Throwable>{

    R apply(T t, U u, V v) throws E;

    static <T, U, V, R, E extends Throwable> ThrowingTriFunction<T, U, V, R, E> of(@NotNull TriFunction<T, U, V, R> function) {
        return function::apply;
    }

    static <T, U, V, R, E extends Throwable> ThrowingTriFunction<T, U, V, R, E> empty() {
        return (t, u, v) -> null;
    }

    static <T, U, V, R, E extends Throwable> ThrowingTriFunction<T, U, V, R, E> fail(@NotNull Supplier<? extends E> exceptionSupplier) {
        return (t, u, v) -> {
            throw exceptionSupplier.get();
        };
    }

}
