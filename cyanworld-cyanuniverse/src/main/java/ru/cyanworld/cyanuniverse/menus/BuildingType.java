package ru.cyanworld.cyanuniverse.menus;

import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.cyanworld.cyan1dex.api.CustomItem;
import ru.cyanworld.cyan1dex.api.CustomMenu;
import ru.cyanworld.cyanuniverse.WorldManager;

import java.util.Map;
import java.util.UUID;

import static ru.cyanworld.cyanuniverse.CyanUniverse.server;

public class BuildingType extends CustomMenu {
    public static Map<UUID, String> map = Maps.newHashMap();

    public BuildingType() {
        super(server, 3, "Тип мира");
        setupItems();
    }

    private void setupItems() {
        inventory.addItem(new CustomItem(new ItemStack(Material.GRASS), "Плоский мир").lore(new String[]{"§7Обычный плоский мир"}));
        inventory.addItem(new CustomItem(new ItemStack(Material.GLASS), "Пустой мир").lore(new String[]{"§7Для особых видов построек"}));
    }

    public Inventory getInv(UUID uuid, String worldname) {
        map.put(uuid, worldname);
        return inventory;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) return;
        if (item.getType() == Material.STAINED_GLASS_PANE) return;
        Player player = (Player) event.getWhoClicked();

        player.closeInventory();
        player.sendTitle("§bГенерация...", "", 0, 20, 5);

        WorldManager.generateWorld(player, map.get(player.getUniqueId()), item.getType());
    }
}

