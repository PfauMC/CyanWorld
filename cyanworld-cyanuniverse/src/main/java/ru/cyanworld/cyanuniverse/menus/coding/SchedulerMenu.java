package ru.cyanworld.cyanuniverse.menus.coding;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import ru.cyanworld.cyan1dex.api.CustomMenu;
import ru.cyanworld.cyan1dex.api.ItemBuilder;

import java.util.Arrays;

import static ru.cyanworld.cyanuniverse.CyanUniverse.server;

public class SchedulerMenu extends CustomMenu {
    public Sign sign;

    public SchedulerMenu(Sign sign) {
        super(server, 1, "Планировщик");
        this.sign = sign;
        initItems();
    }

    public void initItems() {
        this.inventory.addItem(new ItemBuilder(Material.WATCH).name("§aЖдать").lore(Arrays.asList("§7Число")).itemflag(ItemFlag.HIDE_ATTRIBUTES).build());
        this.inventory.addItem(new ItemBuilder(Material.COMPASS).name("§aЦикл").lore(Arrays.asList("§7Число")).itemflag(ItemFlag.HIDE_ATTRIBUTES).build());
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        if (item == null || item.getType() == Material.AIR) return;
        sign.getLocation().clone().add(0, 1, -1).getBlock().setType(Material.AIR);
        switch (item.getType()) {
            case WATCH: {
                sign.setLine(1, "Ждать");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Число");
                player.sendMessage(" \n§7§lПланировщик - Ждать§r\n" +
                        "0 Слот - Время ожидания в тиках (Число)\n" +
                        "§7§lПодсказка: §r1 секунда = 20 тикам, 2 секунды = 40, полсекунды = 10\n ");
                break;
            }
            case COMPASS: {
                sign.setLine(1, "Цикл");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Число");
                player.sendMessage(" \n§7§lПланировщик - Цикл§r\n" +
                        "0 Слот - Время интервала в тиках (Число)\n" +
                        "§7§lПодсказка: §r1 секунда = 20 тикам, 2 секунды = 40, полсекунды = 10\n ");
                break;
            }
        }
        sign.update();
        player.closeInventory();
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, Integer.MAX_VALUE, 1);
    }
}
