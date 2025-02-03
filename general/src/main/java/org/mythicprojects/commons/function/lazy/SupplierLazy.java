package org.mythicprojects.commons.function.lazy;

import java.util.Optional;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import org.mythicprojects.commons.function.ThrowingSupplier;

public class SupplierLazy<T> implements Lazy, Supplier<T> {

    private final ThrowingSupplier<T, ? extends Throwable> supplier;
    private final boolean recomputeIfFailed;

    private @Nullable Optional<T> value;
    private @Nullable Throwable exception;

    public SupplierLazy(@NotNull ThrowingSupplier<T, ? extends Throwable> supplier, boolean recomputeIfFailed) {
        this.supplier = supplier;
        this.recomputeIfFailed = recomputeIfFailed;
    }

    public SupplierLazy(@NotNull ThrowingSupplier<T, ? extends Throwable> supplier) {
        this(supplier, false);
    }

    public SupplierLazy(@NotNull Supplier<T> supplier, boolean recomputeIfFailed) {
        this.supplier = supplier::get;
        this.recomputeIfFailed = recomputeIfFailed;
    }

    public SupplierLazy(@NotNull Supplier<T> supplier) {
        this(supplier, false);
    }

    @Override
    public synchronized @UnknownNullability T get() {
        if (this.value != null) {
            return this.value.orElse(null);
        }

        if (this.exception != null && !this.recomputeIfFailed()) {
            throw LazyComputationFailedException.alreadyComputed(this.exception);
        }

        T value;
        try {
            value = this.supplier.get();
        }
        catch (Throwable throwable) {
            value = this.whenFailed(throwable);
        }
        this.value = Optional.ofNullable(value);
        return value;
    }

    protected @Nullable T whenFailed(@NotNull Throwable throwable) {
        this.exception = throwable;
        throw LazyComputationFailedException.failedComputation(throwable);
    }

    @Override
    public boolean recomputeIfFailed() {
        return this.recomputeIfFailed;
    }

}