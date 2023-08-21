package org.mythicprojects.commons.function;

import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Throwable> {

    R apply(T t) throws E;

    static <T, E extends Throwable> ThrowingFunction<T, T, E> identity() {
        return t -> t;
    }

    static <T, R, E extends Throwable> ThrowingFunction<T, R, E> fail(@NotNull Supplier<? extends E> exceptionSupplier) {
        return t -> {
            throw exceptionSupplier.get();
        };
    }

}
