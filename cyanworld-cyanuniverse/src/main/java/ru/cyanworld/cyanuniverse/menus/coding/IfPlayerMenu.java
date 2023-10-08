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

public class IfPlayerMenu extends CustomMenu {
    public Sign sign;

    public IfPlayerMenu(Sign sign) {
        super(server, 3, "Если игрок");
        this.sign = sign;
        initItems();
    }

    public void initItems() {
        this.inventory.addItem(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).name("§5Крадётся").itemflag(ItemFlag.HIDE_ATTRIBUTES).lore(Arrays.asList("§7Если игрок идёт на шифте")).build());
        this.inventory.addItem(new ItemBuilder(Material.CHEST).name("§eИмеет предмет").lore(Arrays.asList("§7Предмет")).build());
        this.inventory.addItem(new ItemBuilder(Material.ENDER_CHEST).name("§eИмеет предметы").lore(Arrays.asList("§7Предметы")).build());
        this.inventory.addItem(new ItemBuilder(Material.IRON_INGOT).name("§6Держит в правой руке").lore(Arrays.asList("§7Предмет")).build());
        this.inventory.addItem(new ItemBuilder(Material.CLAY_BRICK).name("§6Держит в левой руке").lore(Arrays.asList("§7Предмет")).build());
        this.inventory.addItem(new ItemBuilder(Material.IRON_CHESTPLATE).name("§6Надето").itemflag(ItemFlag.HIDE_ATTRIBUTES).lore(Arrays.asList("§7Предмет")).build());
        this.inventory.addItem(new ItemBuilder(Material.SHIELD).name("§2Держит щит").itemflag(ItemFlag.HIDE_ATTRIBUTES).lore(Arrays.asList("§7Если игрок держит щит")).build());
        this.inventory.addItem(new ItemBuilder(Material.ELYTRA).name("§aЛетает на элитрах").lore(Arrays.asList("§7Если игрок летает на элитрах")).build());
        this.inventory.addItem(new ItemBuilder(Material.GOLD_BOOTS).name("§eБежит").itemflag(ItemFlag.HIDE_ATTRIBUTES).lore(Arrays.asList("§7Если игрок бежит")).build());
        this.inventory.addItem(new ItemBuilder(Material.CHAINMAIL_BOOTS).name("§eЛетает").itemflag(ItemFlag.HIDE_ATTRIBUTES).lore(Arrays.asList("§7Если игрок летает")).build());
        this.inventory.addItem(new ItemBuilder(Material.NAME_TAG).name("§aИмя равно").itemflag(ItemFlag.HIDE_ATTRIBUTES).lore(Arrays.asList("§7Текст")).build());
        this.inventory.addItem(new ItemBuilder(Material.WORKBENCH).name("§dПредмет равен").itemflag(ItemFlag.HIDE_ATTRIBUTES).lore(Arrays.asList("§7Предмет", "§dРаботает на событиях:", "§7Событие игрока: Поднятие предмета", "§7Событие игрока: Выбрасывание предмета")).build());
        this.inventory.addItem(new ItemBuilder(Material.PAPER).name("§aНа координатах").itemflag(ItemFlag.HIDE_ATTRIBUTES).lore(Arrays.asList("§7Координаты")).build());
        this.inventory.addItem(new ItemBuilder(Material.GRASS).name("§aБлок равен").itemflag(ItemFlag.HIDE_ATTRIBUTES).lore(Arrays.asList("§7Координаты и блок")).build());
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        if (item == null || item.getType() == Material.AIR) return;
        sign.getLocation().clone().add(0, 1, -1).getBlock().setType(Material.AIR);
        switch (item.getType()) {
            case CHAINMAIL_LEGGINGS: {
                sign.setLine(1, "Крадётся");
                break;
            }
            case CHEST: {
                sign.setLine(1, "ИмеетПредмет");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Предмет");
                break;
            }
            case ENDER_CHEST: {
                sign.setLine(1, "ИмеетПредметы");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Предмет");
                break;
            }
            case IRON_INGOT: {
                sign.setLine(1, "ДержВПравойРуке");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Предмет");
                break;
            }
            case CLAY_BRICK: {
                sign.setLine(1, "ДержВЛевойРуке");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Предмет");
                break;
            }
            case IRON_CHESTPLATE: {
                sign.setLine(1, "Надето");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Предмет");
                break;
            }
            case SHIELD: {
                sign.setLine(1, "ДержитЩит");
                break;
            }
            case ELYTRA: {
                sign.setLine(1, "НаЭлитрах");
                break;
            }
            case GOLD_BOOTS: {
                sign.setLine(1, "Бежит");
                break;
            }
            case CHAINMAIL_BOOTS: {
                sign.setLine(1, "Летает");
                break;
            }
            case NAME_TAG: {
                sign.setLine(1, "ИмяРавно");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Текст");
                break;
            }
            case WORKBENCH: {
                sign.setLine(1, "ПредметРавен");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Предмет");
                break;
            }
            case PAPER: {
                sign.setLine(1, "НаКоординатах");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Координаты");
                break;
            }
            case GRASS: {
                sign.setLine(1, "БлокРавен");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Координаты, блок");
                break;
            }

        }
        sign.update();
        player.closeInventory();
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, Integer.MAX_VALUE, 1);
    }
}
