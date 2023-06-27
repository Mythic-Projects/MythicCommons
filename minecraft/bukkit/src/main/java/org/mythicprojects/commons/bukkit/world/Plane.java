package org.mythicprojects.commons.bukkit.world;

import org.jetbrains.annotations.NotNull;

public class Plane {

    public final Point center;
    public final Vector normal;

    // Normal to a plane - https://web.ma.utexas.edu/users/m408m/Display12-5-4.shtml
    public Plane(@NotNull Point corner1, @NotNull Point corner2, @NotNull Point corner3, @NotNull Point corner4) {
        this.center = Point.average(corner1, corner2, corner3, corner4);

        Vector planeVector1 = this.center.vectorTo(corner1);
        Vector planeVector2 = this.center.vectorTo(corner2);
        this.normal = planeVector1.crossProduct(planeVector2).normalize();
    }

}