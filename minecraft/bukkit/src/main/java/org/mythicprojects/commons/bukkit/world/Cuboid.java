package org.mythicprojects.commons.bukkit.world;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

public class Cuboid {

    public final BoundingBox box;
    public final Set<Plane> walls;

    public Cuboid(@NotNull BoundingBox box) {
        this.box = box;

        Point vertexA = Point.of(box.getMin());
        Point vertexB = Point.of(box.getMin().setX(box.getMaxX()));
        Point vertexC = Point.of(box.getMax().setY(box.getMinY()));
        Point vertexD = Point.of(box.getMin().setZ(box.getMaxZ()));
        Point vertexE = Point.of(box.getMin().setY(box.getMaxY()));
        Point vertexF = Point.of(box.getMax().setZ(box.getMinZ()));
        Point vertexG = Point.of(box.getMax());
        Point vertexH = Point.of(box.getMax().setX(box.getMinX()));

        this.walls = Set.of(
                new Plane(vertexA, vertexB, vertexC, vertexD),
                new Plane(vertexA, vertexB, vertexF, vertexE),
                new Plane(vertexB, vertexC, vertexG, vertexF),
                new Plane(vertexC, vertexD, vertexH, vertexG),
                new Plane(vertexD, vertexA, vertexE, vertexH),
                new Plane(vertexE, vertexF, vertexG, vertexH)
        );
    }

    public @NotNull BoundingBox getBox() {
        return this.box;
    }

    public boolean contains(double x, double y, double z) {
        return this.box.contains(x, y, z);
    }

    public boolean contains(@NotNull Point point) {
        return this.contains(point.x, point.y, point.z);
    }

    public boolean contains(@NotNull BoundingBox box) {
        return this.box.contains(box);
    }

    public Optional<Plane> getIntersectedWall(@NotNull Point from, @NotNull Point to) {
        return this.getIntersectedWall(from, to, WallSorting.IGNORED);
    }

    // https://math.stackexchange.com/questions/4402134/determining-plane-intersection-with-a-ray
    public Optional<Plane> getIntersectedWall(@NotNull Point from, @NotNull Point to, @NotNull WallSorting wallSorting) {
        for (Plane wall : this.getWalls(from, wallSorting)) {
            double fromDirection = wall.normal.dotProduct(from.vectorTo(wall.center));
            double toDirection = wall.normal.dotProduct(to.vectorTo(wall.center));

            // From and to on the same side of the plane => exclude
            if (Math.signum(fromDirection) == Math.signum(toDirection)) {
                continue;
            }

            double moveDirection = wall.normal.dotProduct(from.vectorTo(to));
            if (Math.signum(fromDirection) == Math.signum(moveDirection)) {
                return Optional.of(wall);
            }
        }

        return Optional.empty();
    }

    private Collection<Plane> getWalls(@NotNull Point from, @NotNull WallSorting wallSorting) {
        if (wallSorting == WallSorting.IGNORED) {
            return this.walls;
        }

        return this.walls.stream()
                .sorted(Comparator.comparingDouble((plane) -> wallSorting.getDistanceForComparison(from, plane)))
                .toList();
    }

    public static @NotNull Cuboid of(@NotNull org.bukkit.util.Vector center, double radius) {
        BoundingBox box = BoundingBox.of(center, 0.5, 0.5, 0.5);
        box.expand(radius, radius, radius);
        return new Cuboid(box);
    }

}