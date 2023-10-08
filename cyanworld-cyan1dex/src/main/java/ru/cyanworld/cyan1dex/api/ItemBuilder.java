package ru.cyanworld.cyan1dex.api;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemBuilder {
    public ItemStack itemStack;
    public ItemMeta itemMeta;

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public ItemBuilder(Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public ItemBuilder(Material material, int amount, int dura) {
        new ItemBuilder(material, amount, (short) dura);
    }

    public ItemBuilder(Material material, int amount, short dura) {
        this.itemStack = new ItemStack(material, amount, dura);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public ItemBuilder name(String name) {
        this.itemMeta.setDisplayName(name != null ? "\u00a7f" + name : null);
        return this;
    }

    public ItemBuilder locname(String locname) {
        this.itemMeta.setLocalizedName(locname);
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder dura(int dura) {
        return this.dura((short) dura);
    }

    public ItemBuilder dura(short dura) {
        this.itemStack.setDurability(dura);
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        this.itemMeta.setLore(lore);
        return this;
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        this.itemMeta.setUnbreakable(unbreakable);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchant, int lvl) {
        this.itemStack.addUnsafeEnchantment(enchant, lvl);
        return this;
    }

    public /* varargs */ ItemBuilder itemflag(ItemFlag... itemFlags) {
        this.itemMeta.addItemFlags(itemFlags);
        return this;
    }

    public ItemStack build() {
        this.itemStack.setItemMeta(this.itemMeta);
        return this.itemStack;
    }
}
