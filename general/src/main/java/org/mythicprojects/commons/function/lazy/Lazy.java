package org.mythicprojects.commons.function.lazy;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface Lazy {

    /**
     * @return whether the value should be recomputed if the computation failed previously
     */
    default boolean recomputeIfFailed() {
        return false;
    }

}
