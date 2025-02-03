package org.mythicprojects.commons.function;

import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.mythicprojects.commons.wrapper.Triple;

@FunctionalInterface
public interface ThrowingTriSupplier<T, U, V, E extends Throwable> extends ThrowingSupplier<Triple<T, U, V>, E> {

    static <T, U, V, E extends Throwable> ThrowingTriSupplier<T, U, V, E> of(@NotNull TriSupplier<T, U, V> supplier) {
        return supplier::get;
    }

    static <T, U, V, E extends Throwable> ThrowingTriSupplier<T, U, V, E> empty() {
        return () -> null;
    }

    static <T, U, V, E extends Throwable> ThrowingTriSupplier<T, U, V, E> fail(@NotNull Supplier<? extends E> exceptionSupplier) {
        return () -> {
            throw exceptionSupplier.get();
        };
    }

}
