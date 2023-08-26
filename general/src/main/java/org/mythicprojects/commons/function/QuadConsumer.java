package org.mythicprojects.commons.function;

import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface QuadConsumer<T, U, V, W> {

    void accept(T t, U u, V v, W w);

    static <T, U, V, W> QuadConsumer<T, U, V, W> empty() {
        return (t, u, v, w) -> {};
    }

    static <T, U, V, W> QuadConsumer<T, U, V, W> fail(@NotNull Supplier<? extends RuntimeException> exceptionSupplier) {
        return (t, u, v, w) -> {
            throw exceptionSupplier.get();
        };
    }

}
