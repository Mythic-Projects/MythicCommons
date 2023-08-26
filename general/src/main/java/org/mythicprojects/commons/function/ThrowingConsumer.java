package org.mythicprojects.commons.function;

import java.util.function.Consumer;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ThrowingConsumer<T, E extends Throwable> {

    void accept(T value) throws E;

    static <T, E extends Throwable> ThrowingConsumer<T, E> of(@NotNull Consumer<T> consumer) {
        return consumer::accept;
    }

    static <T, E extends Throwable> ThrowingConsumer<T, E> empty() {
        return value -> {};
    }

    static <T, E extends Throwable> ThrowingConsumer<T, E> fail(@NotNull Supplier<? extends E> exceptionSupplier) {
        return value -> {
            throw exceptionSupplier.get();
        };
    }

}
