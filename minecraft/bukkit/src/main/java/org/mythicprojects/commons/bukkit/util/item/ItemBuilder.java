package org.mythicprojects.commons.bukkit.util.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemBuilder extends AbstractItemBuilder<ItemBuilder> {

    private ItemBuilder(@NotNull Material material, int amount) {
        super(material, amount);
    }

    private ItemBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
    }

    @Override
    public @NotNull ItemBuilder copy() {
        return new ItemBuilder(this.itemStack);
    }

    public static @NotNull ItemBuilder of(@NotNull Material material, int amount) {
        return new ItemBuilder(material, amount);
    }

    public static @NotNull ItemBuilder of(@NotNull Material material) {
        return of(material, 1);
    }

    public static @NotNull ItemBuilder of(@NotNull ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    public static @NotNull ItemBuilder ofCopy(@NotNull ItemStack itemStack) {
        return of(itemStack.clone());
    }

}
