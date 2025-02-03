package org.mythicprojects.commons.function.lazy;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface LazyFunctional<LAZY extends Lazy> {

    @NotNull LAZY lazy(boolean recomputeIfFailed);

    default @NotNull LAZY lazy() {
        return this.lazy(false);
    }

}
