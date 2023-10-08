package ru.cyanworld.cyanuniverse.menus.nbt.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import ru.cyanworld.cyan1dex.api.CustomMenu;
import ru.cyanworld.cyan1dex.api.ItemBuilder;

import static ru.cyanworld.cyanuniverse.CyanUniverse.server;

public class NbtItemMenu extends CustomMenu {
    public static ItemStack[] contents;
    private ItemBuilder itemBuilder;

    public NbtItemMenu(ItemBuilder itemBuilder) {
        super(server, 3, "Редактор NBT - Предмет");
        this.itemBuilder = itemBuilder;
        initItems();
    }

    public void initItems() {
        if (contents != null) {
            this.inventory.setContents(contents);
        } else {
            this.inventory.setItem(3, new ItemBuilder(Material.BOOK).name("Название").build());
            this.inventory.setItem(4, new ItemBuilder(Material.ENDER_PEARL).name("Описание").build());
            contents = this.inventory.getContents();
        }

        this.inventory.setItem(10, itemBuilder.build());
        calculateUnbreakable();

    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getClickedInventory().getHolder() != this) return;
        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) return;
        Player player = (Player) event.getWhoClicked();
        switch (event.getSlot()) {
            case 10: { //Предмет
                player.getInventory().addItem(item);
                break;
            }
            case 3: { //Название
                player.openInventory(new NbtItemNameMenu(itemBuilder).getInventory());
                break;
            }
            case 4: { //Описание
                player.openInventory(new NbtItemLore(itemBuilder).getInventory());
                break;
            }
            case 5: {
                if (this.inventory.getItem(10).getItemMeta().isUnbreakable()) {
                    this.inventory.setItem(10, itemBuilder.unbreakable(false).build());
                } else {
                    this.inventory.setItem(10, itemBuilder.unbreakable(true).build());
                }
                calculateUnbreakable();
                break;
            }
        }
    }

    public void calculateUnbreakable() {
        if (this.inventory.getItem(10).getItemMeta().isUnbreakable()) {
            inventory.setItem(5, new ItemBuilder(Material.GOLD_PICKAXE).enchant(Enchantment.DIG_SPEED, 1).itemflag(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS).name("§aНеразрушимость - Вкл").build());
        } else {
            inventory.setItem(5, new ItemBuilder(Material.GOLD_PICKAXE).name("§cНеразрушимость - Выкл").itemflag(ItemFlag.HIDE_ATTRIBUTES).build());
        }
    }
}
