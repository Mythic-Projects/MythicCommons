package org.mythicprojects.commons.function;

import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ThrowingQuadConsumer<T, U, V, W, E extends Throwable> {

    void accept(T t, U u, V v, W w) throws E;

    static <T, U, V, W, E extends Throwable> ThrowingQuadConsumer<T, U, V, W, E> of(@NotNull QuadConsumer<T, U, V, W> consumer) {
        return consumer::accept;
    }

    static <T, U, V, W, E extends Throwable> ThrowingQuadConsumer<T, U, V, W, E> empty() {
        return (t, u, v, w) -> {};
    }

    static <T, U, V, W, E extends Throwable> ThrowingQuadConsumer<T, U, V, W, E> fail(@NotNull Supplier<? extends E> exceptionSupplier) {
        return (t, u, v, w) -> {
            throw exceptionSupplier.get();
        };
    }

}
