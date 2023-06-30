package org.mythicprojects.commons.function;

import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ThrowingSupplier<T, E extends Throwable> {

    T get() throws E;

    static <T, E extends Throwable> ThrowingSupplier<T, E> empty() {
        return () -> null;
    }

    static <T, E extends Throwable> ThrowingSupplier<T, E> fail(@NotNull Supplier<? extends E> exceptionSupplier) {
        return () -> {
            throw exceptionSupplier.get();
        };
    }

}
