package org.mythicprojects.commons.function;

import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ThrowingRunnable<E extends Throwable> {

    void run() throws E;

    static <E extends Throwable> ThrowingRunnable<E> of(@NotNull Runnable runnable) {
        return runnable::run;
    }

    static <E extends Throwable> ThrowingRunnable<E> empty() {
        return () -> {};
    }

    static <E extends Throwable> ThrowingRunnable<E> fail(@NotNull Supplier<? extends E> exceptionSupplier) {
        return () -> {
            throw exceptionSupplier.get();
        };
    }

}
