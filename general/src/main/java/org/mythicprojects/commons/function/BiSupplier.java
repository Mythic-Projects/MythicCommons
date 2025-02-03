package org.mythicprojects.commons.function;

import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.mythicprojects.commons.wrapper.Pair;

@FunctionalInterface
public interface BiSupplier<T, U> extends Supplier<Pair<T, U>> {

    static <T, U> BiSupplier<T, U> empty() {
        return () -> null;
    }

    static <T, U> BiSupplier<T, U> fail(@NotNull Supplier<? extends RuntimeException> exceptionSupplier) {
        return () -> {
            throw exceptionSupplier.get();
        };
    }

}
