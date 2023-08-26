package org.mythicprojects.commons.function;

import java.util.function.BiFunction;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ThrowingBiFunction<T, U, R, E extends Throwable> {

    R apply(T t, U u) throws E;

    static <T, U, R, E extends Throwable> ThrowingBiFunction<T, U, R, E> of(@NotNull BiFunction<T, U, R> function) {
        return function::apply;
    }

    static <T, U, R, E extends Throwable> ThrowingBiFunction<T, U, R, E> empty() {
        return (t, u) -> null;
    }

    static <T, U, R, E extends Throwable> ThrowingBiFunction<T, U, R, E> fail(@NotNull Supplier<? extends E> exceptionSupplier) {
        return (t, u) -> {
            throw exceptionSupplier.get();
        };
    }

}
