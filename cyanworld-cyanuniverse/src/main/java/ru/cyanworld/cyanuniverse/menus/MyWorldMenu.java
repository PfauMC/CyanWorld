package ru.cyanworld.cyanuniverse.menus;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.cyanworld.cyan1dex.api.CustomItem;
import ru.cyanworld.cyan1dex.api.CustomMenu;
import ru.cyanworld.cyanuniverse.ItemsList;
import ru.cyanworld.cyanuniverse.MenusList;
import ru.cyanworld.cyanuniverse.WorldManager;

import static ru.cyanworld.cyanuniverse.CyanUniverse.server;

public class MyWorldMenu extends CustomMenu {
    public MyWorldMenu() {
        super(server, 3, "Настройки мира");
        setupItems();
    }

    private void setupItems() {
        inventory.addItem(ItemsList.myworld_building);
        inventory.addItem(ItemsList.myworld_time);
        inventory.addItem(ItemsList.myworld_spawn);
        inventory.addItem(ItemsList.myworld_kick);
        inventory.setItem(4, new CustomItem(new ItemStack(Material.WOOD_DOOR), "Управление входом"));
        inventory.setItem(26, new CustomItem(new ItemStack(Material.IRON_DOOR), "Сохранить и выйти"));
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        switch (item.getType()) {
            case BRICK: {
                player.openInventory(new MyWorldBuilding(player).getInventory());
                break;
            }
            case SKULL_ITEM: {
                player.openInventory(new MyWorldKick(player).getInventory());
                break;
            }
            case ENDER_PEARL: {
                Location loc = player.getLocation();
                World world = loc.getWorld();
                switch (event.getClick()) {
                    case LEFT: {
                        world.setSpawnLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
                        break;
                    }
                    case RIGHT: {
                        world.setSpawnLocation(0, world.getHighestBlockYAt(0, 0), 0);
                        break;
                    }
                }
                player.closeInventory();
                Location spawnl = world.getSpawnLocation().add(0.5, 0, 0.5);
                player.sendTitle("", "Точка спауна установлена на: " + spawnl.getBlockX() + "/" + spawnl.getBlockY() + "/" + spawnl.getBlockZ(), 0, 20, 10);
                break;
            }
            case WATCH: {
                player.openInventory(MenusList.myWorldTime.inventory);
                break;
            }
            case WOOD_DOOR: {
                player.openInventory(new MyWorldEnter(player).getInventory());
                break;
            }
            case IRON_DOOR: {
                player.closeInventory();
                WorldManager.teleportToLobby(player);
                break;
            }
        }
    }
}

