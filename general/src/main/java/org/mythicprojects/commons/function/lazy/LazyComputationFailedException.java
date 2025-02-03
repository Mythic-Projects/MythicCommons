package org.mythicprojects.commons.function.lazy;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class LazyComputationFailedException extends RuntimeException {

    public LazyComputationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LazyComputationFailedException(String message) {
        super(message);
    }

    @ApiStatus.Internal
    public static @NotNull LazyComputationFailedException failedComputation(@NotNull Throwable cause) {
        return new LazyComputationFailedException("Lazy computation has failed", cause);
    }

    @ApiStatus.Internal
    public static @NotNull LazyComputationFailedException alreadyComputed(@NotNull Throwable cause) {
        return new LazyComputationFailedException("Lazy computation has already been initialized and failed", cause);
    }

}
