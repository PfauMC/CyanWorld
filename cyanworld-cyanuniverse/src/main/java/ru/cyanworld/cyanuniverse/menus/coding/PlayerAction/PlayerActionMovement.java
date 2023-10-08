package ru.cyanworld.cyanuniverse.menus.coding.PlayerAction;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.cyanworld.cyan1dex.api.CustomMenu;
import ru.cyanworld.cyan1dex.api.ItemBuilder;

import java.util.Arrays;

import static ru.cyanworld.cyanuniverse.CyanUniverse.server;

public class PlayerActionMovement extends CustomMenu {
    public Sign sign;

    public PlayerActionMovement(Sign sign) {
        super(server, 3, "Сделать игроку - Перемещение");
        this.sign = sign;
        initItems();
    }

    public void initItems() {
        this.inventory.addItem(new ItemBuilder(Material.ENDER_PEARL).name("§5Телепортация").lore(Arrays.asList("§7Местоположение")).build());
        this.inventory.addItem(new ItemBuilder(Material.SLIME_BLOCK).name("§dЗапустить вверх").lore(Arrays.asList("§7Число")).build());
        this.inventory.addItem(new ItemBuilder(Material.PISTON_BASE).name("§dЗапустить вперёд").lore(Arrays.asList("§7Число")).build());
        this.inventory.addItem(new ItemBuilder(Material.PISTON_STICKY_BASE).name("§dЗапустить по траектории").lore(Arrays.asList("§7Число, Число, Число")).build());
        this.inventory.addItem(new ItemBuilder(Material.BARRIER).name("§dОтправить на спавн").build());
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        if (item == null || item.getType() == Material.AIR) return;
        sign.getLocation().clone().add(0, 1, -1).getBlock().setType(Material.AIR);
        switch (item.getType()) {
            case ENDER_PEARL: {
                sign.setLine(1, "Телепортация");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Местоположение");
                break;
            }
            case SLIME_BLOCK: {
                sign.setLine(1, "ЗапусВверх");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Число");
                break;
            }
            case PISTON_BASE: {
                sign.setLine(1, "ЗапусВперёд");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Число");
                break;
            }
            case PISTON_STICKY_BASE: {
                sign.setLine(1, "ЗапусПоТраектории");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Число, Число, Число");
                break;
            }
            case BARRIER: {
                sign.setLine(1, "НаCпавн");
                break;
            }
        }
        sign.update();
        player.closeInventory();
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, Integer.MAX_VALUE, 1);
    }
}
