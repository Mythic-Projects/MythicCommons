package org.mythicprojects.commons.bukkit.barrier;

import java.util.Optional;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.mythicprojects.commons.bukkit.world.Cuboid;
import org.mythicprojects.commons.bukkit.world.Plane;
import org.mythicprojects.commons.bukkit.world.Point;
import org.mythicprojects.commons.bukkit.world.Vector;

public class BarrierListener implements Listener {

    private final Plugin plugin;
    private final Set<Cuboid> cuboids;
    private final Vector velocityVector;

    public BarrierListener(@NotNull Plugin plugin, @NotNull Set<Cuboid> cuboids, @NotNull Vector velocityVector) {
        this.plugin = plugin;
        this.cuboids = cuboids;
        this.velocityVector = velocityVector;
    }

    @EventHandler
    public void onMove(@NotNull PlayerMoveEvent event) {
        Location fromLocation = event.getFrom();
        Location toLocation = event.getTo();
        if (!shouldHandleMove(fromLocation, toLocation)) {
            return;
        }

        Point from = Point.of(fromLocation);
        Point to = Point.of(toLocation);

        for (Cuboid cuboid : this.cuboids) {
            if (cuboid.contains(from) || !cuboid.contains(to)) {
                continue;
            }

            Optional<Plane> wallOptional = cuboid.getIntersectedWall(from, to);
            if (wallOptional.isEmpty()) {
                continue;
            }

            Plane wall = wallOptional.get();
            Vector moveDirection = from.vectorTo(to);
            Vector newVelocity = moveDirection
                    .subtract(moveDirection
                            .multiply(wall.normal)
                            .multiply(2.0D)
                            .multiply(wall.normal)
                    )
                    .normalize()
                    .multiply(this.velocityVector);

            Bukkit.getScheduler().runTask(this.plugin, () -> event.getPlayer().setVelocity(newVelocity.toBukkit()));
            event.setCancelled(true);
            return;
        }
    }

    private static boolean shouldHandleMove(@NotNull Location from, @NotNull Location to) {
        return from.getBlockX() != to.getBlockX()
                || from.getBlockY() != to.getBlockY()
                || from.getBlockZ() != to.getBlockZ();
    }

}
