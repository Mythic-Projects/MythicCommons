package org.mythicprojects.commons.function;

import java.util.function.BiConsumer;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ThrowingBiConsumer <T, U, E extends Throwable> {

    void accept(T t, U u) throws E;

    static <T, U, E extends Throwable> ThrowingBiConsumer<T, U, E> of(@NotNull BiConsumer<T, U> consumer) {
        return consumer::accept;
    }

    static <T, U, E extends Throwable> ThrowingBiConsumer<T, U, E> empty() {
        return (t, u) -> {};
    }

    static <T, U, E extends Throwable> ThrowingBiConsumer<T, U, E> fail(@NotNull Supplier<? extends E> exceptionSupplier) {
        return (t, u) -> {
            throw exceptionSupplier.get();
        };
    }

}
