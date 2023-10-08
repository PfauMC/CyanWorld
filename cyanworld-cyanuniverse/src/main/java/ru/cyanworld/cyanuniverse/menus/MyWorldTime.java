package ru.cyanworld.cyanuniverse.menus;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.cyanworld.cyan1dex.api.CustomItem;
import ru.cyanworld.cyan1dex.api.CustomMenu;

import static ru.cyanworld.cyanuniverse.CyanUniverse.server;

public class MyWorldTime extends CustomMenu {
    public MyWorldTime() {
        super(server, 3, "Управление временем");
        setupItems();
    }

    private void setupItems() {
        inventory.addItem(new CustomItem(new ItemStack(Material.STAINED_CLAY, 1, (short) 4), "Утро").lore(new String[]{"§7Сделать Утро"}));
        inventory.addItem(new CustomItem(new ItemStack(Material.STAINED_CLAY, 1, (short) 13), "День").lore(new String[]{"§7Сделать День"}));
        inventory.addItem(new CustomItem(new ItemStack(Material.STAINED_CLAY, 1, (short) 14), "Вечер").lore(new String[]{"§7Сделать Вечер"}));
        inventory.addItem(new CustomItem(new ItemStack(Material.STAINED_CLAY, 1, (short) 11), "Ночь").lore(new String[]{"§7Сделать Ночь"}));

    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        if (item.getType() == Material.STAINED_GLASS_PANE) return;
        Player player = (Player) event.getWhoClicked();
        World world = player.getWorld();
        if (item.getType() == Material.STAINED_CLAY) {
            switch (item.getDurability()) {
                case 4: {
                    //Утро
                    world.setTime(0);
                    break;
                }
                case 13: {
                    //День
                    world.setTime(6000);
                    break;
                }
                case 14: {
                    //Вечер
                    world.setTime(12500);
                    break;
                }
                case 11: {
                    //Ночь
                    world.setTime(18000);
                    break;
                }
            }
            player.closeInventory();
            player.sendTitle("", "Установлено время: " + item.getItemMeta().getDisplayName(), 0, 20, 10);
        }
    }
}