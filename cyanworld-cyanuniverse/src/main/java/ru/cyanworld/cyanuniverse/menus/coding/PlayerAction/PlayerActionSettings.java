package ru.cyanworld.cyanuniverse.menus.coding.PlayerAction;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import ru.cyanworld.cyan1dex.api.CustomMenu;
import ru.cyanworld.cyan1dex.api.ItemBuilder;

import static ru.cyanworld.cyanuniverse.CyanUniverse.server;

public class PlayerActionSettings extends CustomMenu {
    public Sign sign;

    public PlayerActionSettings(Sign sign) {
        super(server, 3, "Сделать игроку - Настройки");
        this.sign = sign;
        initItems();
    }

    public void initItems() {
        this.inventory.addItem(new ItemBuilder(Material.FEATHER).name("§aВключить полёт").build());
        this.inventory.addItem(new ItemBuilder(Material.LEATHER_BOOTS).name("§cВыключить полёт").itemflag(ItemFlag.HIDE_ATTRIBUTES).build());
        this.inventory.addItem(new ItemBuilder(Material.FENCE_GATE).name("§eРежим игры - Приключение").build());
        this.inventory.addItem(new ItemBuilder(Material.ACACIA_FENCE_GATE).name("§cРежим игры - Выживание").build());
        this.inventory.addItem(new ItemBuilder(Material.BIRCH_FENCE_GATE).name("§3Режим игры - Креатив").build());
        this.inventory.addItem(new ItemBuilder(Material.DARK_OAK_FENCE_GATE).name("§3Режим игры - Наблюдатель").build());
        this.inventory.addItem(new ItemBuilder(Material.BEETROOT_SEEDS).name("§cВыключить сохранение инвентаря при смерти").build());
        this.inventory.addItem(new ItemBuilder(Material.MELON_SEEDS).name("§aВключить сохранение инвентаря при смерти").build());
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        if (item == null || item.getType() == Material.AIR) return;
        sign.getLocation().clone().add(0, 1, -1).getBlock().setType(Material.AIR);
        switch (item.getType()) {
            case FEATHER: {
                sign.setLine(1, "ВклПолёт");
                break;
            }
            case LEATHER_BOOTS: {
                sign.setLine(1, "ВыклПолёт");
                break;
            }
            case FENCE_GATE: {
                sign.setLine(1, "Приключение");
                break;
            }
            case ACACIA_FENCE_GATE: {
                sign.setLine(1, "Выживание");
                break;
            }
            case BIRCH_FENCE_GATE: {
                sign.setLine(1, "Креатив");
                break;
            }
            case DARK_OAK_FENCE_GATE: {
                sign.setLine(1, "Наблюдатель");
                break;
            }
            case BEETROOT_SEEDS: {
                sign.setLine(1, "ВыклСохИнвСмерти");
                break;
            }
            case MELON_SEEDS: {
                sign.setLine(1, "ВклСохИнвСмерти");
                break;
            }
        }
        sign.update();
        player.closeInventory();
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, Integer.MAX_VALUE, 1);
    }
}
