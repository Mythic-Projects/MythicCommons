package org.mythicprojects.commons.bukkit.world;

import org.jetbrains.annotations.NotNull;

public class Vector {

    private final double x;
    private final double y;
    private final double z;
    private final double length;

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.length = Math.sqrt(Math.pow(x, 2.0D) + Math.pow(y, 2.0D) + Math.pow(z, 2.0D));
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public @NotNull Vector subtract(@NotNull Vector other) {
        return new Vector(
                this.x - other.x,
                this.y - other.y,
                this.z - other.z
        );
    }

    public @NotNull Vector multiply(double scalar) {
        return new Vector(
                this.x * scalar,
                this.y * scalar,
                this.z * scalar
        );
    }

    public @NotNull Vector multiply(@NotNull Vector other) {
        return new Vector(
                this.x * other.x,
                this.y * other.y,
                this.z * other.z
        );
    }

    public @NotNull Vector crossProduct(@NotNull Vector other) {
        return new Vector(
                this.y * other.z - this.z * other.y,
                this.z * other.x - this.x * other.z,
                this.x * other.y - this.y * other.x
        );
    }

    public double dotProduct(@NotNull Vector other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public @NotNull Vector normalize() {
        return new Vector(
                this.x / this.length,
                this.y / this.length,
                this.z / this.length
        );
    }

    public @NotNull org.bukkit.util.Vector toBukkit() {
        return new org.bukkit.util.Vector(
                this.x,
                this.y,
                this.z
        );
    }

}