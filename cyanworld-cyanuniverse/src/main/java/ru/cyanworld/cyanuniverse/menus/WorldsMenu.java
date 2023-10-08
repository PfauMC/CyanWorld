package ru.cyanworld.cyanuniverse.menus;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.cyanworld.cyan1dex.Utils;
import ru.cyanworld.cyan1dex.api.CustomItem;
import ru.cyanworld.cyan1dex.api.CustomMenu;
import ru.cyanworld.cyan1dex.api.ItemBuilder;
import ru.cyanworld.cyanuniverse.CyanUniverse;
import ru.cyanworld.cyanuniverse.ItemsList;
import ru.cyanworld.cyanuniverse.WorldManager;

import java.util.Arrays;
import java.util.UUID;

import static ru.cyanworld.cyanuniverse.CyanUniverse.*;

public class WorldsMenu extends CustomMenu {
    private ItemStack[] defaultContents;

    public WorldsMenu() {
        super(server, 7, "Миры");
        setupItems();
        server.getScheduler().scheduleSyncRepeatingTask(CyanUniverse.plugin, this::updateItems, 0, 20);
    }

    public void setupItems() {
        for (int i = 54; i < 63; i++) {
            inventory.setItem(i, GLASS);
        }
        inventory.setItem(58, ItemsList.myworlds);
        inventory.setItem(62, ItemsList.lobby);
        defaultContents = inventory.getContents();
    }

    public void updateItems() {
        inventory.setContents(defaultContents);
        server.getWorlds().forEach(World -> {
            if (World == CyanUniverse.lobby) return;
            if (World.getPlayers().size() == 0) {
                server.unloadWorld(World, true);
                return;
            }
            String worldname = World.getName();
            String playername = CyanUniverse.worldscfg.getString(worldname + ".owner", "null");
            String worlddisplayname = CyanUniverse.worldscfg.getString(worldname + ".name", "Мир #" + worldname.split("#")[1] + " " + playername);
            int votecount = worldscfg.getInt(worldname + ".votecount");
            switch (CyanUniverse.worldscfg.getString(worldname + ".type")) {
                case "building": {
                    switch (CyanUniverse.worldscfg.getString(worldname + ".allowenter")) {
                        case "all": {
                            inventory.addItem(new ItemBuilder(Material.BRICK).name(worlddisplayname).locname(worldname).lore(Arrays.asList("§7Строительство", " ", "§7Автор: " + playername, "§7Голоса: " + votecount)).build());
                            break;
                        }
                        case "nobody": {
                            inventory.addItem(new ItemBuilder(Material.BARRIER).name(worlddisplayname).locname(worldname).lore(Arrays.asList("§7Мир закрыт для игроков", " ", "§7Автор: " + playername, "§7Голоса: " + votecount)).build());
                            break;
                        }
                        case "donator": {
                            inventory.addItem(new ItemBuilder(Material.NOTE_BLOCK).name(worlddisplayname).locname(worldname).lore(Arrays.asList("§7Вход для донатеров", " ", "§7Автор: " + playername, "§7Голоса: " + votecount)).build());
                            break;
                        }
                    }
                    break;
                }
                case "coding": {
                    inventory.addItem(new ItemBuilder(Material.COMMAND).amount(World.getPlayerCount()).name(worlddisplayname).locname(worldname).lore(Arrays.asList("§7Кодинг", " ", "§7Автор: " + playername, "§7Голоса: " + votecount)).build());
                    break;
                }
                case "playing": {
                    inventory.addItem(new ItemBuilder(Material.DIAMOND).amount(World.getPlayerCount()).name(worlddisplayname).locname(worldname).lore(Arrays.asList("§7Игра", " ", "§7Автор: " + playername, "§7Голоса: " + votecount)).build());
                    break;
                }
            }
        });
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        if (item.getType() == Material.STAINED_GLASS_PANE) return;
        Player player = (Player) event.getWhoClicked();
        switch (item.getType()) {
            case BARRIER: {
                if (Utils.playerIsModer(player) && event.getClick() == ClickType.SHIFT_LEFT) {
                    Player subject = server.getPlayer(UUID.fromString(item.getItemMeta().getLocalizedName().split("#")[0]));
                    if (subject != null && subject.isOnline()) {
                        player.setGameMode(GameMode.SPECTATOR);
                        player.teleport(subject);
                        Utils.updatePerWorldTab(player);
                    }
                } else player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, Integer.MAX_VALUE, 1);
                break;
            }
            case MAP: {
                player.openInventory(new MyWorlds(player).getInventory());
                break;
            }
            case MAGMA_CREAM: {
                WorldManager.teleportToLobby(player);
                break;
            }
            case NOTE_BLOCK: {
                if (Utils.playerIsModer(player) && event.getClick() == ClickType.SHIFT_LEFT) {
                    Player subject = server.getPlayer(UUID.fromString(item.getItemMeta().getLocalizedName().split("#")[0]));
                    if (subject != null && subject.isOnline()) {
                        player.setGameMode(GameMode.SPECTATOR);
                        player.teleport(subject);
                        Utils.updatePerWorldTab(player);
                    }
                } else if (Utils.playerIsDonator(player)) {
                    WorldManager.teleportToWorld(player, item.getItemMeta().getLocalizedName());
                } else player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, Integer.MAX_VALUE, 1);

                break;
            }
            default: {
                if (!CustomItem.isHasLocName(item)) return;
                if (Utils.playerIsModer(player) && event.getClick() == ClickType.SHIFT_LEFT) {
                    Player subject = server.getPlayer(UUID.fromString(item.getItemMeta().getLocalizedName().split("#")[0]));
                    if (subject != null && subject.isOnline()) {
                        player.setGameMode(GameMode.SPECTATOR);
                        player.teleport(subject);
                        Utils.updatePerWorldTab(player);
                    }
                } else {
                    String worldname = item.getItemMeta().getLocalizedName();
                    World world = WorldManager.teleportToWorld(player, worldname);

                    if (player.getGameMode() != GameMode.SPECTATOR)
                        server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            if (player.isOnline() && player.getWorld() == world) {
                                if (!worldscfg.getStringList(worldname + ".votes").contains(player.getUniqueId().toString())) {
                                    player.sendMessage(" \n§aПонравился мир? Проголосуй за него! /vote\n ");
                                    player.playSound(player.getLocation(), Sound.ENTITY_CAT_PURREOW, Integer.MAX_VALUE, 1);
                                }
                            }
                        }, 600);
                }
                break;
            }
        }
    }
}
