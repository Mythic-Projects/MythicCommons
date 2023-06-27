package org.mythicprojects.commons.bukkit.world;

import java.util.function.BiFunction;
import org.jetbrains.annotations.NotNull;

public enum WallSorting {

    IGNORED((point, plane) -> 0.0D),
    CLOSEST_FIRST((point, plane) -> plane.center.distanceSquared(point)),
    FARTHEST_FIRST((point, plane) -> -plane.center.distanceSquared(point));

    private final BiFunction<Point, Plane, Double> distanceForComparisonProducer;

    WallSorting(BiFunction<Point, Plane, Double> distanceForComparisonProducer) {
        this.distanceForComparisonProducer = distanceForComparisonProducer;
    }

    public double getDistanceForComparison(@NotNull Point point, @NotNull Plane plane) {
        return this.distanceForComparisonProducer.apply(point, plane);
    }
}