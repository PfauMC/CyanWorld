package ru.cyanworld.cyan1dex.api;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class CustomMenu implements InventoryHolder {
    public final Inventory inventory;
    public final ItemStack GLASS = new CustomItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11), "", "glass");
    public final ItemStack AIR = new ItemStack(Material.AIR);

    public CustomMenu(Server server, int rows, String title) {
        this.inventory = server.createInventory(this, rows * 9, title);
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public abstract void onClick(InventoryClickEvent var1);
}
