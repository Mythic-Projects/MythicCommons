package org.mythicprojects.commons.function;

import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.mythicprojects.commons.wrapper.Triple;

@FunctionalInterface
public interface TriSupplier<T, U, V> extends Supplier<Triple<T, U, V>> {

    static <T, U, V> TriSupplier<T, U, V> empty() {
        return () -> null;
    }

    static <T, U, V> TriSupplier<T, U, V> fail(@NotNull Supplier<? extends RuntimeException> exceptionSupplier) {
        return () -> {
            throw exceptionSupplier.get();
        };
    }

}
