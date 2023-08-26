package org.mythicprojects.commons.function;

import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ThrowingTriConsumer <T, U, V, E extends Throwable> {

    void accept(T t, U u, V v) throws E;

    static <T, U, V, E extends Throwable> ThrowingTriConsumer<T, U, V, E> of(@NotNull TriConsumer<T, U, V> consumer) {
        return consumer::accept;
    }

    static <T, U, V, E extends Throwable> ThrowingTriConsumer<T, U, V, E> empty() {
        return (t, u, v) -> {};
    }

    static <T, U, V, E extends Throwable> ThrowingTriConsumer<T, U, V, E> fail(@NotNull Supplier<? extends E> exceptionSupplier) {
        return (t, u, v) -> {
            throw exceptionSupplier.get();
        };
    }

}
