package org.mythicprojects.commons.bukkit.util.item;

import com.destroystokyo.paper.MaterialSetTag;
import com.destroystokyo.paper.MaterialTags;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Campfire;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.Conduit;
import org.bukkit.block.Container;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.DaylightDetector;
import org.bukkit.block.EnchantingTable;
import org.bukkit.block.EnderChest;
import org.bukkit.block.Jigsaw;
import org.bukkit.block.Jukebox;
import org.bukkit.block.Lectern;
import org.bukkit.block.Sign;
import org.bukkit.block.Structure;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.Rail;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Beehive;
import org.bukkit.block.data.type.Cake;
import org.bukkit.block.data.type.EndPortalFrame;
import org.bukkit.block.data.type.Grindstone;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ItemUtil {

    private ItemUtil() {
    }

    public static boolean isFunctionalBlock(@Nullable Block block) {
        if (block == null) {
            return false;
        }

        BlockState state = block.getState();
        boolean isFunctional = state instanceof Beacon
                || state instanceof Beehive
                || state instanceof Campfire
                || state instanceof CommandBlock
                || state instanceof Conduit
                || state instanceof Container
                || state instanceof CreatureSpawner
                || state instanceof DaylightDetector
                || state instanceof EnchantingTable
                || state instanceof EnderChest
                || state instanceof Jigsaw
                || state instanceof Jukebox
                || state instanceof Lectern
                || state instanceof Sign
                || state instanceof Structure;

        if (isFunctional) {
            return true;
        }

        BlockData data = block.getBlockData();
        isFunctional = data instanceof Bed
                || data instanceof Cake
                || data instanceof EndPortalFrame
                || data instanceof Grindstone
                || data instanceof Powerable
                || data instanceof Rail
                || data instanceof RespawnAnchor;

        if (isFunctional) {
            return true;
        }

        Material material = block.getType();
        return Tag.ANVIL.isTagged(material);
    }

    public static boolean isTool(@Nullable Material material) {
        if (material == null) {
            return false;
        }
        return MaterialTags.SWORDS.isTagged(material)
                || MaterialTags.PICKAXES.isTagged(material)
                || MaterialTags.AXES.isTagged(material)
                || MaterialTags.SHOVELS.isTagged(material)
                || MaterialTags.HOES.isTagged(material)
                || material == Material.BOW
                || material == Material.CROSSBOW;
    }

    public static boolean isArmor(@Nullable Material material) {
        if (material == null) {
            return false;
        }
        return MaterialTags.HEAD_EQUIPPABLE.isTagged(material)
                || MaterialTags.CHESTPLATES.isTagged(material)
                || material == Material.ELYTRA
                || MaterialTags.LEGGINGS.isTagged(material)
                || MaterialTags.BOOTS.isTagged(material);
    }

    public static short getTier(@Nullable ItemStack item) {
        if (item == null) {
            return -1;
        }

        return switch (item.getType()) {
            case WOODEN_SWORD, WOODEN_PICKAXE, WOODEN_AXE, WOODEN_SHOVEL, WOODEN_HOE, LEATHER_HELMET, LEATHER_CHESTPLATE, LEATHER_LEGGINGS, LEATHER_BOOTS, BOW, CROSSBOW:
                yield 0;
            case STONE_SWORD, STONE_PICKAXE, STONE_AXE, STONE_SHOVEL, STONE_HOE, CHAINMAIL_HELMET, CHAINMAIL_CHESTPLATE, CHAINMAIL_LEGGINGS, CHAINMAIL_BOOTS:
                yield 1;
            case GOLDEN_SWORD, GOLDEN_PICKAXE, GOLDEN_AXE, GOLDEN_SHOVEL, GOLDEN_HOE, GOLDEN_HELMET, GOLDEN_CHESTPLATE, GOLDEN_LEGGINGS, GOLDEN_BOOTS:
                yield 2;
            case IRON_SWORD, IRON_PICKAXE, IRON_AXE, IRON_SHOVEL, IRON_HOE, IRON_HELMET, IRON_CHESTPLATE, IRON_LEGGINGS, IRON_BOOTS:
                yield 3;
            case DIAMOND_SWORD, DIAMOND_PICKAXE, DIAMOND_AXE, DIAMOND_SHOVEL, DIAMOND_HOE, DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS:
                yield 4;
            case NETHERITE_SWORD, NETHERITE_PICKAXE, NETHERITE_AXE, NETHERITE_SHOVEL, NETHERITE_HOE, NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS:
                yield 5;
            default:
                yield -1;
        };
    }

    public static boolean isSameItemType(@Nullable ItemStack item1, @Nullable ItemStack item2) {
        if (item1 == null || item2 == null) {
            return false;
        }

        Material material1 = item1.getType();
        Material material2 = item2.getType();
        if (material1 == material2) {
            return true;
        }

        ToolType type1 = ToolType.getType(item1);
        ToolType type2 = ToolType.getType(item2);
        if (type1 == null || type2 == null) {
            return false;
        }

        return type1 == type2;
    }

    public enum ToolType {
        SWORD(MaterialTags.SWORDS),
        PICKAXE(MaterialTags.PICKAXES),
        AXE(MaterialTags.AXES),
        SHOVEL(MaterialTags.SHOVELS),
        HOE(MaterialTags.HOES);

        private final MaterialSetTag tags;

        ToolType(@NotNull MaterialSetTag tags) {
            this.tags = tags;
        }

        public @NotNull MaterialSetTag getTags() {
            return this.tags;
        }

        public static @Nullable ToolType getType(@Nullable Material material) {
            if (material == null) {
                return null;
            }
            for (ToolType type : values()) {
                if (!type.tags.isTagged(material)) {
                    continue;
                }
                return type;
            }
            return null;
        }

        public static @Nullable ToolType getType(@Nullable ItemStack item) {
            if (item == null) {
                return null;
            }
            return getType(item.getType());
        }

    }

}
