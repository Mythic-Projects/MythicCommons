package org.mythicprojects.commons.function;

import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.mythicprojects.commons.wrapper.Quad;

@FunctionalInterface
public interface QuadSupplier<T, U, V, W> extends Supplier<Quad<T, U, V, W>> {

    static <T, U, V, W> QuadSupplier<T, U, V, W> empty() {
        return () -> null;
    }

    static <T, U, V, W> QuadSupplier<T, U, V, W> fail(@NotNull Supplier<? extends RuntimeException> exceptionSupplier) {
        return () -> {
            throw exceptionSupplier.get();
        };
    }

}
