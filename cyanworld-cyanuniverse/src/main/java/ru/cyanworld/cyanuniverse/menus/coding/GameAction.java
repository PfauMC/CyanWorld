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

public class GameAction extends CustomMenu {
    public Sign sign;

    public GameAction(Sign sign) {
        super(server, 3, "Параметры игры");
        this.sign = sign;
        initItems();
    }

    public void initItems() {
        this.inventory.addItem(new ItemBuilder(Material.IRON_HELMET).name("§aВключить PvP").itemflag(ItemFlag.HIDE_ATTRIBUTES).build());
        this.inventory.addItem(new ItemBuilder(Material.CHAINMAIL_HELMET).name("§cВыключить PvP").itemflag(ItemFlag.HIDE_ATTRIBUTES).build());
        this.inventory.addItem(new ItemBuilder(Material.IRON_PICKAXE).name("§aВключить выпадание блоков").itemflag(ItemFlag.HIDE_ATTRIBUTES).build());
        this.inventory.addItem(new ItemBuilder(Material.WOOD_PICKAXE).name("§cВыключить выпадание блоков").itemflag(ItemFlag.HIDE_ATTRIBUTES).build());
        this.inventory.addItem(new ItemBuilder(Material.ROTTEN_FLESH).name("§aВключить лут с мобов").build());
        this.inventory.addItem(new ItemBuilder(Material.MUSHROOM_SOUP).name("§cВыключить лут с мобов").build());
        this.inventory.addItem(new ItemBuilder(Material.DEAD_BUSH).name("§cУстановить блок").build());
        this.inventory.addItem(new ItemBuilder(Material.SKULL_ITEM).dura(4).name("§6Сложность").build());
        this.inventory.addItem(new ItemBuilder(Material.BARRIER).name("§eОтменить событие").lore(Arrays.asList("§dРаботает на событиях:", "§7Событие игрока: Правый клик", "§7Событие игрока: Левый клик", "§7Событие игрока: Урон по мобу", "§7Событие игрока: Клик по мобу", "§7Событие игрока: Поднятие предмета", "§7Событие игрока: Выбрасывание предмета")).build());
        this.inventory.addItem(new ItemBuilder(Material.DRAGON_EGG).name("§cЗакрыть мир без сохранения").build());
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        if (item == null || item.getType() == Material.AIR) return;
        sign.getLocation().clone().add(0, 1, -1).getBlock().setType(Material.AIR);
        switch (item.getType()) {
            case IRON_HELMET: {
                sign.setLine(1, "ВклPvP");
                break;
            }
            case CHAINMAIL_HELMET: {
                sign.setLine(1, "ВыклPvP");
                break;
            }
            case IRON_PICKAXE: {
                sign.setLine(1, "ВыпадБлоковВкл");
                break;
            }
            case WOOD_PICKAXE: {
                sign.setLine(1, "ВыпадБлоковВыкл");
                break;
            }
            case ROTTEN_FLESH: {
                sign.setLine(1, "ЛутСМобовВкл");
                break;
            }
            case MUSHROOM_SOUP: {
                sign.setLine(1, "ЛутСМобовВыкл");
                break;
            }
            case DEAD_BUSH: {
                sign.setLine(1, "УстБлок");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Координаты и блок");
                player.sendMessage(" \n§7§lПараметры игры - Установить блок§r\n" +
                        "0 Слот - Координаты \n" +
                        "1 Слот - Блок \n" +
                        "§7§lПодсказка: §rЧтобы сломать блок оставьте слот 1 пустым");
                break;
            }
            case SKULL_ITEM: {
                sign.setLine(1, "Сложность");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Число");
                player.sendMessage(" \n§7§lПараметры игры - Сложность§r\n" +
                        "0 Слот - Сложность (Число)\n" +
                        "§7§lПодсказка: §rСложность - число в пределах от 0 до 3\n0 - мирный, 1 - лёгкий, 2 - нормальный, 3 - сложный\n ");
                break;
            }
            case BARRIER: {
                sign.setLine(1, "ОтменитьСобытие");
                break;
            }
            case DRAGON_EGG: {
                sign.setLine(1, "ЗакрытьБезСохр");
                break;
            }
        }
        sign.update();
        player.closeInventory();
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, Integer.MAX_VALUE, 1);
    }
}
