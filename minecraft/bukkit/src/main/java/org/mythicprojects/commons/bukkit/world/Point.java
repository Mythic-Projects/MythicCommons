package org.mythicprojects.commons.bukkit.world;

import java.util.Arrays;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class Point {

    public final double x;
    public final double y;
    public final double z;

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public @NotNull Vector vectorTo(@NotNull Point end) {
        return new Vector(
                end.x - this.x,
                end.y - this.y,
                end.z - this.z
        );
    }

    public double distance(@NotNull Point other) {
        return Math.sqrt(this.distanceSquared(other));
    }

    public double distanceSquared(@NotNull Point other) {
        return Math.pow(this.x - other.x, 2.0D) + Math.pow(this.y - other.y, 2.0D) + Math.pow(this.z - other.z, 2.0D);
    }

    public static @NotNull Point of(@NotNull org.bukkit.util.Vector bukkitVector) {
        return new Point(
                bukkitVector.getX(),
                bukkitVector.getY(),
                bukkitVector.getZ()
        );
    }

    public static @NotNull Point of(@NotNull Location location) {
        return new Point(
                location.getX(),
                location.getY(),
                location.getZ()
        );
    }

    public static @NotNull Point average(@NotNull Point... points) {
        return new Point(
                Arrays.stream(points).mapToDouble(p -> p.x).sum() / points.length,
                Arrays.stream(points).mapToDouble(p -> p.y).sum() / points.length,
                Arrays.stream(points).mapToDouble(p -> p.z).sum() / points.length
        );
    }

}