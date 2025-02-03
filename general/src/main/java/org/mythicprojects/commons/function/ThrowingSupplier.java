package org.mythicprojects.commons.function;

import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;
import org.mythicprojects.commons.function.lazy.LazyFunctional;
import org.mythicprojects.commons.function.lazy.SupplierLazy;

@FunctionalInterface
public interface ThrowingSupplier<VALUE, EXCEPTION extends Throwable>
        extends LazyFunctional<SupplierLazy<VALUE>> {

    @UnknownNullability
    VALUE get() throws EXCEPTION;

    @Override
    default @NotNull SupplierLazy<VALUE> lazy(boolean recomputeIfFailed) {
        return new SupplierLazy<>(this, recomputeIfFailed);
    }

    static <T, E extends Throwable> ThrowingSupplier<T, E> of(@NotNull Supplier<T> supplier) {
        return supplier::get;
    }

    static <T, E extends Throwable> ThrowingSupplier<T, E> empty() {
        return () -> null;
    }

    static <T, E extends Throwable> ThrowingSupplier<T, E> fail(@NotNull Supplier<? extends E> exceptionSupplier) {
        return () -> {
            throw exceptionSupplier.get();
        };
    }

}
