package ru.cyanworld.cyanuniverse.menus.decorations;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.cyanworld.cyan1dex.api.CustomItem;
import ru.cyanworld.cyan1dex.api.CustomMenu;
import ru.cyanworld.cyanuniverse.CyanUniverse;

import java.io.File;
import java.util.HashMap;

import static ru.cyanworld.cyanuniverse.CyanUniverse.server;

public class DecoHeads extends CustomMenu {
    public static YamlConfiguration headbase = YamlConfiguration.loadConfiguration(new File(CyanUniverse.pluginFolder, "heads.yml"));
    public static HashMap<String, YamlConfiguration> headcategorymap = new HashMap<>();

    public DecoHeads() {
        super(server, 6, "Декорации - Головы");
        setupItems();
    }

    public static Inventory getMenu(Player player, String categoryid) {
        String uuid = player.getUniqueId().toString();
        String categoryname = headbase.getString(categoryid + ".name");
        DecoHeadCategory menu = new DecoHeadCategory(categoryname);
        Inventory inv = menu.getInventory();
        YamlConfiguration category = headcategorymap.get(categoryid);
        category.getKeys(false).forEach((String head) -> {
            String id = "ru.cyanworld.cyanuniverse.heads." + categoryid + "." + head;
            String name = category.getString(head + ".name");
            String texture = category.getString(head + ".texture");
            int price = category.getInt(head + ".price");
            CyanUniverse.itemPrice.put(id, price);
            inv.addItem(DecoMain.getItem(uuid, new CustomItem(Material.SKULL_ITEM).durability(3).name(name).id(id).skull(texture)));
        });
        return inv;
    }

    public void setupItems() {
        inventory.clear();
        headbase.getKeys(false).forEach(String -> {
            String texture = headbase.getString(String + ".texture");
            String name = headbase.getString(String + ".name");
            String id = String;
            inventory.addItem(new CustomItem(Material.SKULL_ITEM).durability(3).name(name).id(id).skull(texture));
            headcategorymap.put(id, YamlConfiguration.loadConfiguration(new File(CyanUniverse.pluginFolder, "heads/" + id + ".yml")));
        });
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        if (!CustomItem.isHasLocName(item)) return;
        if (item.getType() == Material.SKULL_ITEM) {
            player.openInventory(getMenu(player, item.getItemMeta().getLocalizedName()));
        }
    }
}
