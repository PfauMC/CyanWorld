package ru.cyanworld.cyan1dex.menus;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.cyanworld.cyan1dex.Cyan1dex;
import ru.cyanworld.cyan1dex.CyanEcoManager;
import ru.cyanworld.cyan1dex.api.CustomItem;
import ru.cyanworld.cyan1dex.api.CustomMenu;

import java.util.Collections;
import java.util.List;

public class BuyingMenu
        extends CustomMenu {
    public ItemStack buyingitem;
    public int price;

    public BuyingMenu(ItemStack item, int price) {
        super(Cyan1dex.server, 3, "Купить: " + item.getItemMeta().getDisplayName() + " за " + price + " монеток");
        this.buyingitem = item;
        this.price = price;
        this.setupItems();
    }

    private void setupItems() {
        this.inventory.setItem(10, new CustomItem(new ItemStack(Material.STAINED_CLAY, 1, (short) 13), "§aКупить", "cyan1dex.buying.buy").lore(Collections.singletonList("§7Цена: §b" + this.price + " монеток")));
        this.inventory.setItem(16, new CustomItem(new ItemStack(Material.STAINED_CLAY, 1, (short) 14), "§cОтмена", "cyan1dex.buying.cancel").lore(Collections.singletonList("§7Закрыть меню")));
        this.inventory.setItem(13, this.buyingitem);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        if (item == null) return;
        Player player = (Player) event.getWhoClicked();
        switch (item.getItemMeta().getLocalizedName()) {
            case "cyan1dex.buying.buy": {
                int balance = CyanEcoManager.getEco(player);
                if (balance < this.price) {
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 2.14748365E9f, 1.0f);
                    player.closeInventory();
                    player.sendTitle("§cНе достаточно монеток!", "Цена: §b" + this.price + " §rНужно еще: §b" + (this.price - balance), 0, 60, 10);
                    return;
                }
                player.closeInventory();
                String uuid = player.getUniqueId().toString();
                ItemMeta itemmeta = this.buyingitem.getItemMeta();
                List list = Cyan1dex.cfgplayers.getStringList(uuid + ".bought");
                list.add(itemmeta.getLocalizedName());
                Cyan1dex.cfgplayers.set(uuid + ".bought", list);
                Cyan1dex.saveCfg(Cyan1dex.cfgplayers, "players.yml");
                player.sendTitle("§aПриобретено!", itemmeta.getDisplayName(), 10, 60, 10);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2.14748365E9f, 0.5f);
                CyanEcoManager.addEco(player, this.price * -1, false);
                break;
            }
            case "cyan1dex.buying.cancel": {
                player.closeInventory();
                break;
            }
        }
    }
}
