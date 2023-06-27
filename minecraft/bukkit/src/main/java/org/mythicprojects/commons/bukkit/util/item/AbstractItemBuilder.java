package org.mythicprojects.commons.bukkit.util.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mythicprojects.commons.bukkit.util.ColorUtil;
import org.mythicprojects.commons.replace.Replacement;
import org.mythicprojects.commons.replace.StringReplacer;
import org.mythicprojects.commons.util.Validate;

public abstract class AbstractItemBuilder<T extends AbstractItemBuilder<T>> {

    protected final ItemStack itemStack;

    protected AbstractItemBuilder(@NotNull Material material, int amount) {
        Validate.notNull(material, "Material cannot be null");
        this.itemStack = new ItemStack(material, amount);
    }

    protected AbstractItemBuilder(@NotNull ItemStack itemStack) {
        this.itemStack = Validate.notNull(itemStack, "ItemStack cannot be null");
    }

    @Contract("_ -> this")
    public T durability(short durability) {
        return this.manipulateMeta(meta -> {
            if (meta instanceof Damageable) {
                ((Damageable) meta).setDamage(durability);
            }
        });
    }

    @Contract("_ -> this")
    public T unbreakable(boolean unbreakable) {
        return this.manipulateMeta(meta -> meta.setUnbreakable(unbreakable));
    }

    @Contract("_ -> this")
    public T amount(int amount) {
        this.itemStack.setAmount(amount);
        return (T) this;
    }

    @Contract("_ -> this")
    public T name(@Nullable String name) {
        return this.manipulateMeta(meta -> meta.setDisplayName(name));
    }

    @Contract("_ -> this")
    public T coloredName(@Nullable String name) {
        return this.name(ColorUtil.color(name));
    }

    @Contract("_ -> this")
    public T lore(@Nullable List<String> lore) {
        return this.manipulateMeta(meta -> meta.setLore(lore));
    }

    @Contract("_ -> this")
    public T lore(@Nullable String... lore) {
        return this.lore(Arrays.asList(lore));
    }

    @Contract("_ -> this")
    public T coloredLore(@Nullable List<String> lore) {
        return this.lore(ColorUtil.color(lore));
    }

    @Contract("_ -> this")
    public T coloredLore(@Nullable String... lore) {
        return this.lore(ColorUtil.color(lore));
    }

    @Contract("_ -> this")
    public T appendLore(@Nullable String line) {
        return this.manipulateMeta(meta -> {
            List<String> lore = meta.hasLore()
                    ? new ArrayList<>(meta.getLore())
                    : new ArrayList<>();
            lore.add(line);
            meta.setLore(lore);
        });
    }

    @Contract("_, _ -> this")
    public T appendLore(int lineIndex, @Nullable String line) {
        return this.manipulateMeta(meta -> {
            List<String> lore = meta.hasLore()
                    ? new ArrayList<>(meta.getLore())
                    : new ArrayList<>();
            lore.set(lineIndex, line);
            meta.setLore(lore);
        });
    }

    /*
        Item Flags
     */
    @Contract("_ -> this")
    public T itemFlags(@NotNull ItemFlag... itemFlags) {
        return this.manipulateMeta(meta -> meta.addItemFlags(itemFlags));
    }

    @Contract("_ -> this")
    public T itemFlags(@NotNull Collection<ItemFlag> itemFlags) {
        return this.manipulateMeta(meta -> itemFlags.forEach(meta::addItemFlags));
    }

    /*
        Enchantments
     */
    @Contract("_ -> this")
    public T enchantments(@NotNull Map<Enchantment, Integer> enchantments) {
        return this.manipulateMeta(meta -> enchantments.forEach((enchantment, level) -> meta.addEnchant(enchantment, level, false)));
    }

    @Contract("_, _ -> this")
    public T enchantment(@NotNull Enchantment enchantment, int level) {
        return this.enchantments(Collections.singletonMap(enchantment, level));
    }

    @Contract("_ -> this")
    public T unsafeEnchantments(@NotNull Map<Enchantment, Integer> enchantments) {
        return this.manipulateMeta(meta -> enchantments.forEach((enchantment, level) -> meta.addEnchant(enchantment, level, true)));
    }

    @Contract("_, _ -> this")
    public T unsafeEnchantment(@NotNull Enchantment enchantment, int level) {
        return this.unsafeEnchantments(Collections.singletonMap(enchantment, level));
    }

    /*
        Book Enchantments
     */
    @Contract("_ -> this")
    public T bookEnchantments(@NotNull Map<Enchantment, Integer> enchantments) {
        return this.<EnchantmentStorageMeta>manipulateMeta(meta -> enchantments.forEach((enchantment, level) -> meta.addStoredEnchant(enchantment, level, false)));
    }

    @Contract("_, _ -> this")
    public T bookEnchantment(Enchantment enchantment, int level) {
        return this.bookEnchantments(Collections.singletonMap(enchantment, level));
    }

    @Contract("_ -> this")
    public T unsafeBookEnchantments(Map<Enchantment, Integer> enchantments) {
        return this.<EnchantmentStorageMeta>manipulateMeta(meta -> enchantments.forEach((enchantment, level) -> meta.addStoredEnchant(enchantment, level, true)));
    }

    @Contract("_, _ -> this")
    public T unsafeBookEnchantment(Enchantment enchantment, int level) {
        return this.unsafeBookEnchantments(Collections.singletonMap(enchantment, level));
    }

    /*
        Skulls
     */
    @Contract("_ -> this")
    public T skullOwner(@Nullable OfflinePlayer skullOwner) {
        return this.<SkullMeta>manipulateMeta(meta -> meta.setOwningPlayer(skullOwner));
    }

    /*
        Colors
     */
    @Contract("_ -> this")
    public T leatherArmorColor(@Nullable Color color) {
        return this.<LeatherArmorMeta>manipulateMeta(meta -> meta.setColor(color));
    }

    /*
        Potions
     */
    @Contract("_ -> this")
    public T potionData(@NotNull PotionData data) {
        return this.<PotionMeta>manipulateMeta(meta -> meta.setBasePotionData(data));
    }

    @Contract("_ -> this")
    public T potionType(@NotNull PotionType type) {
        return this.potionData(new PotionData(type));
    }

    @Contract("_, _ -> this")
    public T potionEffects(@NotNull Collection<PotionEffect> effects, boolean overwrite) {
        return this.<PotionMeta>manipulateMeta(meta -> effects.forEach(effect -> meta.addCustomEffect(effect, overwrite)));
    }

    @Contract("_ -> this")
    public T potionEffects(@NotNull Collection<PotionEffect> effects) {
        return this.potionEffects(effects, false);
    }

    @Contract("_, _ -> this")
    public T potionEffect(@NotNull PotionEffect effect, boolean overwrite) {
        return this.potionEffects(Collections.singleton(effect), overwrite);
    }

    @Contract("_ -> this")
    public T potionEffect(@NotNull PotionEffect effect) {
        return this.potionEffect(effect, false);
    }

    /*
        Persistent Data Containers
     */
    @Contract("_ -> this")
    public T manipulatePersistentDataContainer(@NotNull Consumer<PersistentDataContainer> manipulation) {
        Validate.notNull(manipulation, "Manipulation cannot be null");
        return this.manipulateMeta(meta -> manipulation.accept(meta.getPersistentDataContainer()));
    }

    /*
        Custom
     */
    @Contract("_ -> this")
    public T ownMeta(@NotNull ItemMeta itemMeta) {
        Validate.notNull(itemMeta, "ItemMeta cannot be null");
        this.itemStack.setItemMeta(itemMeta);
        return (T) this;
    }

    @Contract("_ -> this")
    public <U extends ItemMeta> T manipulateMeta(@NotNull Consumer<U> manipulation) {
        Validate.notNull(manipulation, "Manipulation cannot be null");

        ItemMeta meta = this.itemStack.getItemMeta();
        if (meta == null) {
            return (T) this;
        }

        try {
            U metaCast = (U) meta;
            manipulation.accept(metaCast);
            this.itemStack.setItemMeta(metaCast);
        } catch (ClassCastException ignored) {
        }

        return (T) this;
    }

    @Contract("_ -> this")
    public T manipulate(@NotNull Consumer<ItemStack> manipulation) {
        Validate.notNull(manipulation, "Manipulation cannot be null");
        manipulation.accept(this.itemStack);
        return (T) this;
    }

    /*
        Replacements
     */
    @Contract("_ -> this")
    public T replaceInName(@NotNull Replacement... replacements) {
        return this.manipulateMeta(meta -> meta.setDisplayName(StringReplacer.replace(meta.getDisplayName(), replacements)));
    }

    @Contract("_ -> this")
    public T replaceInLore(@NotNull Replacement... replacements) {
        return this.manipulateMeta(meta -> meta.setLore(StringReplacer.replace(meta.getLore(), replacements)));
    }

    @Contract("_ -> this")
    public T replace(@NotNull Replacement... replacements) {
        this.replaceInName(replacements);
        this.replaceInLore(replacements);

        return (T) this;
    }

    /*
        Build
     */
    public @NotNull ItemStack get() {
        return this.itemStack;
    }

    @Contract(pure = true)
    public abstract @NotNull T copy();

}
