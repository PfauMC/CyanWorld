package ru.cyanworld.cyan1dex.menus;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.cyanworld.cyan1dex.BungeeBridge;
import ru.cyanworld.cyan1dex.Config;
import ru.cyanworld.cyan1dex.Cyan1dex;
import ru.cyanworld.cyan1dex.api.CustomItem;
import ru.cyanworld.cyan1dex.api.CustomMenu;
import ru.cyanworld.cyan1dex.query.QueryResponse;

import java.util.Arrays;
import java.util.List;

public class ServerMenu
        extends CustomMenu {
    public static List<String> bwArenas = Arrays.asList("bw1", "bw2");

    public ServerMenu() {
        super(Cyan1dex.server, 3, "Выбор сервера");
    }

    public void updateItems(String lore) {
        this.inventory.setItem(4, new CustomItem(new ItemStack(Material.BED), "BedWars").lore(Arrays.asList("§7Защищай свою кровать", "§7и сломай кровать противника", " ", lore, "§7Сейчас играют: " + this.getSummOnline(bwArenas))));
        this.inventory.setItem(12, new CustomItem(new ItemStack(Material.WORKBENCH), "Выживание").lore(Arrays.asList("§7Ванильное выживание", "§7без команд, телепортаций", "§7и приватов", "§cРазрешено гриферство!", "§cРежим не рекомендуется для новичков!", " ", lore, this.getOnlineStatus("survival"))));
        this.inventory.setItem(14, new CustomItem(new ItemStack(Material.GRASS), "Миры игроков").lore(Arrays.asList("§7У каждого игрока - свой мир", " ", "§aНажмите, чтобы открыть", "§aсписок миров", "§7Миров онлайн: " + (Cyan1dex.server.getWorlds().size() - 1))));
        this.inventory.setItem(22, new CustomItem(new ItemStack(Material.SLIME_BLOCK), "MissileWars").lore(Arrays.asList("§7Взорви портал противника", " ", lore, this.getOnlineStatus("mw"))));
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        switch (item.getType()) {
            case SLIME_BLOCK: {
                player.setPlayerListHeaderFooter(new TextComponent("§bCyan§3World"), new TextComponent("MissileWars"));
                BungeeBridge.connectPlayer(player, "mw");
                if (!Config.isMainServer) break;
                String uuid = player.getUniqueId().toString();
                Cyan1dex.rcon("mw", "cyan1dex setdata " + uuid + " " + Cyan1dex.cfgplayers.getString(uuid + ".group") + " " + Cyan1dex.cfgplayers.getInt(uuid + ".eco"));
                break;
            }
            case GRASS: {
                if (Cyan1dex.server.getPluginManager().isPluginEnabled("CyanUniverse")) {
                    // player.openInventory(ru.cyanworld.cyanuniverse.MenusList.worldsMenu.inventory);
                    break;
                }
                BungeeBridge.connectPlayer(player, "hub");
                break;
            }
            case WORKBENCH: {
                player.setPlayerListHeaderFooter(new TextComponent("§bCyan§3World"), new TextComponent("Выживание"));
                BungeeBridge.connectPlayer(player, "survival");
                if (!Config.isMainServer) break;
                String uuid = player.getUniqueId().toString();
                Cyan1dex.rcon("survival", "cyan1dex setdata " + uuid + " " + Cyan1dex.cfgplayers.getString(uuid + ".group") + " " + Cyan1dex.cfgplayers.getInt(uuid + ".eco"));
                break;
            }
            case BED: {
                player.openInventory(MenusList.bedWarsMenu.getInventory());
                break;
            }
            case WOOL: {
                BungeeBridge.connectPlayer(player, "pgm");
                break;
            }
            default: {
                player.setPlayerListHeaderFooter(new TextComponent("§bCyan§3World"), new TextComponent("SkyWars"));
                BungeeBridge.connectPlayer(player, "sw");
                if (!Config.isMainServer) break;
                String uuid = player.getUniqueId().toString();
                Cyan1dex.rcon("mw", "cyan1dex setdata " + uuid + " " + Cyan1dex.cfgplayers.getString(uuid + ".group") + " " + Cyan1dex.cfgplayers.getInt(uuid + ".eco"));
            }
        }
    }

    private int getSummOnline(List<String> names) {
        int[] summ = new int[]{0};
        names.forEach(name -> {
                    QueryResponse response = Cyan1dex.queryResponseMap.get(name);
                    if (response == null) {
                        return;
                    }
                    summ[0] = summ[0] + response.getOnlinePlayers();
                }
        );
        return summ[0];
    }

    private String getOnlineStatus(String name) {
        QueryResponse response = Cyan1dex.queryResponseMap.get(name);
        if (response == null) {
            return "§7Сервер перезагружается...";
        }
        return "§7Сейчас играют: " + response.getOnlinePlayers();
    }

}
