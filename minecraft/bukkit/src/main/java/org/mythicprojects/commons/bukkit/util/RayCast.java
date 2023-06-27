package org.mythicprojects.commons.bukkit.util;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public final class RayCast {

    private static final double STEP_SIZE = 0.2D;
    private static final Vector BLOCK_CORRECTION = new Vector(0.5D, 0.5D, 0.5D);

    private RayCast() {
    }

    public static @NotNull List<Block> getBlocksBetween(@NotNull Location start, @NotNull Block end) {
        return getBlocksBetween(start, end, STEP_SIZE);
    }

    public static @NotNull List<Block> getBlocksBetween(@NotNull Location start, @NotNull Block end, double stepSize) {
        return getBlocksBetween(start, end.getLocation().add(BLOCK_CORRECTION), stepSize);
    }

    public static @NotNull List<Block> getBlocksBetween(@NotNull Block start, Location end) {
        return getBlocksBetween(start, end, STEP_SIZE);
    }

    public static @NotNull List<Block> getBlocksBetween(@NotNull Block start, @NotNull Location end, double stepSize) {
        return getBlocksBetween(start.getLocation().add(BLOCK_CORRECTION), end, stepSize);
    }

    public static @NotNull List<Block> getBlocksBetween(@NotNull Block start, @NotNull Block end) {
        return getBlocksBetween(start, end, STEP_SIZE);
    }

    public static @NotNull List<Block> getBlocksBetween(@NotNull Block start, @NotNull Block end, double stepSize) {
        return getBlocksBetween(start.getLocation().add(BLOCK_CORRECTION), end.getLocation().add(BLOCK_CORRECTION), stepSize);
    }

    public static @NotNull List<Block> getBlocksBetween(@NotNull Location start, @NotNull Location end) {
        return getBlocksBetween(start, end, STEP_SIZE);
    }

    public static @NotNull List<Block> getBlocksBetween(@NotNull Location start, @NotNull Location end, double stepSize) {
        List<Block> blocks = new ArrayList<>();
        Block endBlock = end.getBlock();
        Block lastBlock = start.getBlock();

        Vector vector = end.toVector().subtract(start.toVector());
        double distance = vector.length();
        Vector delta = vector.clone().normalize().multiply(stepSize);

        Location current = start.clone();
        for (double step = 1; step <= Math.floor(distance / stepSize); step++) {
            current.add(delta);

            Block currentBlock = current.getBlock();
            if (endBlock.equals(currentBlock)) {
                break;
            }

            if (lastBlock.equals(currentBlock)) {
                continue;
            }

            blocks.add(currentBlock);
            lastBlock = currentBlock;
        }

        return blocks;
    }

}