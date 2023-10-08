package ru.cyanworld.cyanuniverse.menus;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.cyanworld.cyan1dex.api.CustomMenu;
import ru.cyanworld.cyan1dex.api.ItemBuilder;
import ru.cyanworld.cyanuniverse.CyanUniverse;
import ru.cyanworld.cyanuniverse.MenusList;
import ru.cyanworld.cyanuniverse.WorldManager;

import java.util.Arrays;
import java.util.UUID;

import static ru.cyanworld.cyanuniverse.CyanUniverse.*;

public class WorldType extends CustomMenu {
    String worldname;
    String playername;

    public WorldType(String worldname) {
        super(server, 3, "Режим мира");
        this.worldname = worldname;
        this.playername = CyanUniverse.worldscfg.getString(worldname + ".owner", server.getPlayer(UUID.fromString(worldname.split("#")[0])).getDisplayName());
        setupItems();
    }

    private void setupItems() {
        inventory.setItem(4, new ItemBuilder(Material.GRASS).name(worldscfg.getString(worldname + ".name", "Мир #" + worldname.split("#")[1] + " " + playername)).lore(Arrays.asList("§7Голоса: " + worldscfg.getInt(worldname + ".votecount"))).build());
        inventory.setItem(10, new ItemBuilder(Material.DIAMOND).name("§aИгра").build());
        inventory.setItem(12, new ItemBuilder(Material.BRICK).name("§eСтроительство").build());
        inventory.setItem(14, new ItemBuilder(Material.COMMAND).name("§bКодинг §7§l[Бета v" + CyanUniverse.pluginDescription.getVersion() + "]").lore(Arrays.asList("§7Этот режим на стадии тестирования,", "§7Об о всех багах писать в лс группы ВК:", "§7vk.me/cyanworld")).build());
        inventory.setItem(16, new ItemBuilder(Material.LAVA_BUCKET).name("§cУдалить мир").build());
        inventory.setItem(22, new ItemBuilder(Material.IRON_SWORD).name("§oKitPvP (InDev)").build());
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        if (item.getType() == Material.STAINED_GLASS_PANE) return;
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        World worldtoteleport = null;
        switch (item.getType()) {
            case LAVA_BUCKET: {
                player.openInventory(MenusList.deleteWorld.getInv(uuid, worldname));
                break;
            }
            case BRICK: {
                if (CyanUniverse.worldscfg.getString(worldname + ".type") != null) {
                    worldscfg.set(worldname + ".type", "building");
                    worldscfg.set(worldname + ".owner", player.getDisplayName());
                    player.closeInventory();
                    player.sendTitle("Телепортируем...", "", 0, 20, 5);
                    worldtoteleport = WorldManager.teleportToWorld(player, worldname);
                } else {
                    player.openInventory(MenusList.buildingType.getInv(uuid, worldname));
                }
                break;
            }
            case IRON_SWORD: {
                if (player.isOp() == false) {
                    player.sendMessage("§7[§bCyan§3World§7] §fВ разработке");
                }
                if (player.isOp() == true) {
                    if (CyanUniverse.worldscfg.getString(worldname + ".type") != null) {
                        worldscfg.set(worldname + ".type", "kitpvp");
                        worldscfg.set(worldname + ".owner", player.getDisplayName());
                        player.closeInventory();
                        player.sendTitle("Телепортируем...", "", 0, 20, 5);
                        worldtoteleport = WorldManager.teleportToWorld(player, worldname);
                        player.openInventory(MenusList.kitPvpMenu.getInv(uuid, worldname));
                    } else {

                    }
                }
            }
            case COMMAND: {
                if (CyanUniverse.worldscfg.getString(worldname + ".type") != null) {
                    worldscfg.set(worldname + ".type", "coding");
                    worldscfg.set(worldname + ".owner", player.getDisplayName());
                    player.closeInventory();
                    player.sendTitle("Телепортируем...", "", 0, 20, 5);
                    worldtoteleport = WorldManager.teleportToWorld(player, worldname);
                } else {

                }
                break;
            }
            case DIAMOND: {
                if (CyanUniverse.worldscfg.getString(worldname + ".type") != null) {
                    worldscfg.set(worldname + ".type", "playing");
                    worldscfg.set(worldname + ".owner", player.getDisplayName());
                    player.closeInventory();
                    player.sendTitle("Телепортируем...", "", 0, 20, 5);
                    worldtoteleport = WorldManager.teleportToWorld(player, worldname);
                } else player.playSound(player.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, Integer.MAX_VALUE, 0.5F);
            }
        }
        server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (player.isOnline()) {
                String worldname = player.getWorld().getName();
                if (worldname.startsWith(player.getUniqueId().toString()) && worldscfg.getString(worldname + ".name", "Мир #").startsWith("Мир #") && worldscfg.getInt(worldname + "votecount") >= 25) {
                    player.sendMessage(" \nПодсказка: Название мира можно ставить с помощью §b/world name Название\n ");
                }
            }
        }, 300);
    }
}

