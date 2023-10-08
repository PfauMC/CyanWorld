package ru.cyanworld.cyanuniverse.menus.decorations;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.cyanworld.cyan1dex.Cyan1dex;
import ru.cyanworld.cyan1dex.api.CustomItem;
import ru.cyanworld.cyan1dex.api.CustomMenu;
import ru.cyanworld.cyanuniverse.CyanUniverse;

import java.util.ArrayList;
import java.util.List;

import static ru.cyanworld.cyanuniverse.CyanUniverse.server;

public class DecoMain extends CustomMenu {

    public DecoMain() {
        super(server, 3, "Декорации");
        setupItems();
    }

    public static ItemStack getItem(String uuid, ItemStack item) {
        ItemStack finalitem = item.clone();
        String id = item.getItemMeta().getLocalizedName();
        List<String> list = Cyan1dex.cfgplayers.getStringList(uuid + ".bought");

        ItemMeta meta = finalitem.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) lore = new ArrayList<>();
        if (!list.contains(id)) {
            lore.add(" ");
            lore.add("§cНе приобретенно!");
            lore.add("§7Цена: §b" + CyanUniverse.itemPrice.get(meta.getLocalizedName()) + " монеток");
            meta.setLore(lore);
        }
        finalitem.setItemMeta(meta);
        return finalitem;
    }

    private void setupItems() {
        inventory.addItem(new CustomItem(Material.DRAGON_EGG).name("Особые блоки"));
        inventory.addItem(new CustomItem(Material.SKULL_ITEM).name("Головы"));
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        switch (item.getType()) {
            case DRAGON_EGG: {
                player.openInventory(new DecoSpecialBlocks(player).getInventory());
                break;
            }
            case SKULL_ITEM: {
                player.chat("/hdb");
                break;
            }
        }
    }
}
