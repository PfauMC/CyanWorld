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

public class IfEntityMenu extends CustomMenu {
    public Sign sign;

    public IfEntityMenu(Sign sign) {
        super(server, 3, "Если моб");
        this.sign = sign;
        initItems();
    }

    public void initItems() {
        this.inventory.addItem(new ItemBuilder(Material.MONSTER_EGG).name("§5Тип моба").lore(Arrays.asList("§7Текст")).itemflag(ItemFlag.HIDE_ATTRIBUTES).build());
        this.inventory.addItem(new ItemBuilder(Material.NAME_TAG).name("§aИмя равно").lore(Arrays.asList("§7Текст")).itemflag(ItemFlag.HIDE_ATTRIBUTES).build());
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        if (item == null || item.getType() == Material.AIR) return;
        sign.getLocation().clone().add(0, 1, -1).getBlock().setType(Material.AIR);
        switch (item.getType()) {
            case MONSTER_EGG: {
                sign.setLine(1, "Тип");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Текст");
                /*
                player.sendMessage(" \n§7§lПланировщик - Ждать§r\n" +
                        "0 Слот - Время ожидания в тиках (Число)\n" +
                        "§7§lПодсказка: §r1 секунда = 20 тикам, 2 секунды = 40, полсекунды = 10\n ");
                        */
                break;
            }
            case NAME_TAG: {
                sign.setLine(1, "ИмяРавно");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Текст");
                /*
                player.sendMessage(" \n§7§lПланировщик - Ждать§r\n" +
                        "0 Слот - Время ожидания в тиках (Число)\n" +
                        "§7§lПодсказка: §r1 секунда = 20 тикам, 2 секунды = 40, полсекунды = 10\n ");
                        */
                break;
            }
        }
        sign.update();
        player.closeInventory();
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, Integer.MAX_VALUE, 1);
    }
}
