package ru.cyanworld.cyan1dex.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.cyanworld.cyan1dex.BungeeBridge;
import ru.cyanworld.cyan1dex.Config;
import ru.cyanworld.cyan1dex.Cyan1dex;
import ru.cyanworld.cyan1dex.api.CustomItem;
import ru.cyanworld.cyan1dex.api.CustomMenu;
import ru.cyanworld.cyan1dex.api.ItemBuilder;
import ru.cyanworld.cyan1dex.query.QueryResponse;

import java.util.Arrays;

public class BedWarsMenu
        extends CustomMenu {
    public String animjoin = "§a▶ Нажмите, чтобы";
    public int animframe = 1;

    public BedWarsMenu() {
        super(Cyan1dex.server, 1, "BedWars - Выбор арены");
        Cyan1dex.server.getScheduler().runTaskTimer(Cyan1dex.instance, () -> {
                    --this.animframe;
                    if (this.animframe < 0) {
                        this.animframe = 1;
                    }
                    if (this.animframe == 1) {
                        this.animjoin = "§a▶ Нажмите, чтобы";
                    }
                    if (this.animframe == 0) {
                        this.animjoin = "§a  Нажмите, чтобы";
                    }
                    this.updateItems();
                }
                , 0, 20);
    }

    public void updateItems() {
        this.inventory.setItem(0, this.getArena("bw1"));
        this.inventory.setItem(1, this.getArena("bw2"));
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        if (!CustomItem.isHasLocName(item)) {
            return;
        }
        String arena = item.getItemMeta().getLocalizedName();
        BungeeBridge.connectPlayer(player, arena);
        if (Config.isMainServer) {
            String uuid = player.getUniqueId().toString();
            Cyan1dex.rcon(arena, "cyan1dex setdata " + uuid + " " + Cyan1dex.cfgplayers.getString(uuid + ".group") + " " + Cyan1dex.cfgplayers.getInt(uuid + ".eco"));
        }
    }

    private ItemStack getArena(String arena) {
        String status;
        QueryResponse response = Cyan1dex.queryResponseMap.get(arena);
        if (response == null) {
            return null;
        }
        int online = response.getOnlinePlayers();
        String worldname = response.getMapName();
        switch (status = response.getMOTD().split("#")[1]) {
            case "ingame": {
                return new ItemBuilder(Material.BED).dura(15).locname(arena).name("Арена " + arena).lore(Arrays.asList("§7Карта: " + worldname, "§7Идёт игра", " ", this.animjoin + " наблюдать", "§7Сейчас играют: " + online)).build();
            }
            case "waiting": {
                return new ItemBuilder(Material.BED).dura(5).locname(arena).name("Арена " + arena).lore(Arrays.asList("§7Карта: " + worldname, "§aОжидание игроков", " ", this.animjoin + " играть", "§7Сейчас играют: " + online)).build();
            }
        }
        return new ItemBuilder(Material.BARRIER).name("Ошибка пингования, сообщите админу").build();
    }
}
