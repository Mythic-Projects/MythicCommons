package org.mythicprojects.commons.function;

import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ThrowingFunction<T, U, E extends Throwable> {

    U apply(T t) throws E;

    static <T, E extends Throwable> ThrowingFunction<T, T, E> identity() {
        return t -> t;
    }

    static <T, U, E extends Throwable> ThrowingFunction<T, U, E> fail(@NotNull Supplier<E> exceptionSupplier) {
        return t -> {
            throw exceptionSupplier.get();
        };
    }

}
