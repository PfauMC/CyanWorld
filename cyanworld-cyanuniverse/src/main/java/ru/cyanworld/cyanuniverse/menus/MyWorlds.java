package ru.cyanworld.cyanuniverse.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.cyanworld.cyan1dex.Cyan1dex;
import ru.cyanworld.cyan1dex.Utils;
import ru.cyanworld.cyan1dex.api.CustomMenu;
import ru.cyanworld.cyan1dex.api.ItemBuilder;
import ru.cyanworld.cyan1dex.menus.BuyingMenu;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static ru.cyanworld.cyanuniverse.CyanUniverse.server;

public class MyWorlds extends CustomMenu {
    Player player;

    public MyWorlds(Player player) {
        super(server, 3, "Мои миры");
        this.player = player;
        setupItems();
    }

    public void setupItems() {
        List<String> join = Arrays.asList("", "§aКликни, чтобы зайти в мир");
        List<String> needdoantor = Arrays.asList("", "§cТребуется привелегия Донатер");
        List<String> notbuy = Arrays.asList("", "§cНе приобретено!", "§bЦена: 500 монеток");

        String uuid = player.getUniqueId().toString();
        List<String> boughtlist = Cyan1dex.cfgplayers.getStringList(uuid + ".bought");


        //Миры для всех


        inventory.addItem(new ItemBuilder(Material.GRASS).name("Мир #0").locname(uuid + "#0").lore(join).build());

        inventory.addItem(new ItemBuilder(Material.GRASS).name("Мир #1").locname(uuid + "#1").lore(join).build());

        inventory.addItem(new ItemBuilder(Material.GRASS).name("Мир #2").locname(uuid + "#2").lore(join).build());


        //Миры за монетки


        if (boughtlist.contains("ru.cyanworld.cyanuniverse.world1") || Utils.playerIsDonator(player))
            inventory.addItem(new ItemBuilder(Material.GRASS).name("Мир #3").lore(join).locname(uuid + "#3").build());
        else
            inventory.addItem(new ItemBuilder(Material.GRASS).name("Мир #3").lore(notbuy).locname(uuid + "#3").build());

        if (boughtlist.contains("ru.cyanworld.cyanuniverse.world2") || Utils.playerIsDonator(player))
            inventory.addItem(new ItemBuilder(Material.GRASS).name("Мир #4").lore(join).locname(uuid + "#4").build());
        else
            inventory.addItem(new ItemBuilder(Material.GRASS).name("Мир #4").lore(notbuy).locname(uuid + "#4").build());

        if (boughtlist.contains("ru.cyanworld.cyanuniverse.world3") || Utils.playerIsDonator(player))
            inventory.addItem(new ItemBuilder(Material.GRASS).name("Мир #5").lore(join).locname(uuid + "#5").build());
        else
            inventory.addItem(new ItemBuilder(Material.GRASS).name("Мир #5").lore(notbuy).locname(uuid + "#5").build());


        //Миры донатеров


        if (Utils.playerIsDonator(player))
            inventory.addItem(new ItemBuilder(Material.GRASS).name("Мир #6").lore(join).locname(uuid + "#6").build());
        else
            inventory.addItem(new ItemBuilder(Material.GRASS).name("Мир #6").lore(needdoantor).locname(uuid + "#6").build());

        if (Utils.playerIsDonator(player))
            inventory.addItem(new ItemBuilder(Material.GRASS).name("Мир #7").lore(join).locname(uuid + "#7").build());
        else
            inventory.addItem(new ItemBuilder(Material.GRASS).name("Мир #7").lore(needdoantor).locname(uuid + "#7").build());

        if (Utils.playerIsDonator(player))
            inventory.addItem(new ItemBuilder(Material.GRASS).name("Мир #8").lore(join).locname(uuid + "#8").build());
        else
            inventory.addItem(new ItemBuilder(Material.GRASS).name("Мир #8").lore(needdoantor).locname(uuid + "#8").build());
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        if (item == null) return;
        if (item.getType() == Material.STAINED_GLASS_PANE) return;
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();

        List<String> boughtlist = Cyan1dex.cfgplayers.getStringList(uuid + ".bought");

        if (item.getType() == Material.GRASS) {
            switch (item.getItemMeta().getDisplayName()) {
                case "§fМир #0": {
                    player.openInventory(new WorldType(uuid + "#0").getInventory());
                    break;
                }
                case "§fМир #1": {
                    player.openInventory(new WorldType(uuid + "#1").getInventory());
                    break;
                }
                case "§fМир #2": {
                    player.openInventory(new WorldType(uuid + "#2").getInventory());
                    break;
                }
                case "§fМир #3": {
                    if (boughtlist.contains("ru.cyanworld.cyanuniverse.world1") || Utils.playerIsDonator(player)) {
                        player.openInventory(new WorldType(uuid + "#3").getInventory());
                    } else {
                        player.openInventory(new BuyingMenu(new ItemBuilder(Material.GRASS).name("§rМир #3").locname("ru.cyanworld.cyanuniverse.world1").build(), 500).getInventory());
                    }
                    break;
                }
                case "§fМир #4": {
                    if (boughtlist.contains("ru.cyanworld.cyanuniverse.world2") || Utils.playerIsDonator(player)) {
                        player.openInventory(new WorldType(uuid + "#4").getInventory());
                    } else {
                        player.openInventory(new BuyingMenu(new ItemBuilder(Material.GRASS).name("§rМир #4").locname("ru.cyanworld.cyanuniverse.world2").build(), 500).getInventory());
                    }
                    break;
                }
                case "§fМир #5": {
                    if (boughtlist.contains("ru.cyanworld.cyanuniverse.world3") || Utils.playerIsDonator(player)) {
                        player.openInventory(new WorldType(uuid + "#5").getInventory());
                    } else {
                        player.openInventory(new BuyingMenu(new ItemBuilder(Material.GRASS).name("§rМир #5").locname("ru.cyanworld.cyanuniverse.world3").build(), 500).getInventory());
                    }
                    break;
                }
                case "§fМир #6": {
                    if (Utils.playerIsDonator(player)) {
                        player.openInventory(new WorldType(uuid + "#6").getInventory());
                    } else {
                        Utils.playCustomSound("deny", player);
                    }
                    break;
                }
                case "§fМир #7": {
                    if (Utils.playerIsDonator(player)) {
                        player.openInventory(new WorldType(uuid + "#7").getInventory());
                    } else {
                        Utils.playCustomSound("deny", player);
                    }
                    break;
                }
                case "§fМир #8": {
                    if (Utils.playerIsDonator(player)) {
                        player.openInventory(new WorldType(uuid + "#8").getInventory());
                    } else {
                        Utils.playCustomSound("deny", player);
                    }
                    break;
                }
            }
        }
    }
}
