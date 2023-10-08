package ru.cyanworld.cyanuniverse.menus.coding;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import ru.cyanworld.cyan1dex.api.CustomMenu;
import ru.cyanworld.cyan1dex.api.ItemBuilder;

import java.util.Arrays;

import static ru.cyanworld.cyanuniverse.CyanUniverse.server;

public class PlayerEventMenu extends CustomMenu {
    public Sign sign;

    public PlayerEventMenu(Sign sign) {
        super(server, 3, "Событие игрока");
        this.sign = sign;
        initItems();
    }

    public void initItems() {
        this.inventory.addItem(new ItemBuilder(Material.POTATO_ITEM).name("§aВход игрока").lore(Arrays.asList("§7Когда игрок заходит в мир")).build());
        this.inventory.addItem(new ItemBuilder(Material.POISONOUS_POTATO).name("§cВыход игрока").lore(Arrays.asList("§7Когда игрок выходит из мира")).build());
        this.inventory.addItem(new ItemBuilder(Material.DIAMOND_PICKAXE).name("§bПравый клик").lore(Arrays.asList("§7Когда игрок кликает", "§7правой кнопкой мыши")).itemflag(ItemFlag.HIDE_ATTRIBUTES).build());
        this.inventory.addItem(new ItemBuilder(Material.IRON_PICKAXE).name("§3Левый клик").lore(Arrays.asList("§7Когда игрок кликает", "§7левой кнопкой мыши")).itemflag(ItemFlag.HIDE_ATTRIBUTES).build());
        this.inventory.addItem(new ItemBuilder(Material.SKULL_ITEM).name("§cУбийство игрока").lore(Arrays.asList("§7Когда игрок убивает игрока")).build());
        this.inventory.addItem(new ItemBuilder(Material.REDSTONE).name("§cСмерть игрока").lore(Arrays.asList("§7Когда игрок умирает")).build());
        this.inventory.addItem(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).name("§5Приседание игрока").lore(Arrays.asList("§7Когда игрок приседает")).build());
        // this.inventory.addItem(new ItemBuilder(Material.MONSTER_EGG).name("§5Игрок убил моба").lore(Arrays.asList("§7Когда игрок убивает моба")).build());
        this.inventory.addItem(new ItemBuilder(Material.STONE_SWORD).name("§cИгрок нанёс урон мобу").lore(Arrays.asList("§7Когда игрок наносит урон мобу")).build());
        this.inventory.addItem(new ItemBuilder(Material.PORK).name("§dКлик по мобу").lore(Arrays.asList("§7Когда игрок кликает по мобу")).build());
        this.inventory.addItem(new ItemBuilder(Material.GLOWSTONE_DUST).name("§dПоднятие предмета").lore(Arrays.asList("§7Когда игрок поднимает предмет")).build());
        this.inventory.addItem(new ItemBuilder(Material.SUGAR).name("§2Выбрасывание предмета").lore(Arrays.asList("§7Когда игрок выбрасывает предмет")).build());
        this.inventory.addItem(new ItemBuilder(Material.STONE).name("§bПоставил блок").lore(Arrays.asList("§7Когда игрок ставит блок")).build());
        this.inventory.addItem(new ItemBuilder(Material.COBBLESTONE).name("§3Сломал блок").lore(Arrays.asList("§7Когда игрок ломает блок")).build());

    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        if (item == null || item.getType() == Material.AIR) return;
        switch (item.getType()) {
            case POTATO_ITEM: {
                sign.setLine(1, "Вход");
                break;
            }
            case POISONOUS_POTATO: {
                sign.setLine(1, "Выход");
                break;
            }
            case DIAMOND_PICKAXE: {
                sign.setLine(1, "ПравыйКлик");
                break;
            }
            case IRON_PICKAXE: {
                sign.setLine(1, "ЛевыйКлик");
                break;
            }
            case REDSTONE: {
                sign.setLine(1, "Смерть");
                break;
            }
            case SKULL_ITEM: {
                sign.setLine(1, "УбийствоИгрока");
                break;
            }
            case CHAINMAIL_LEGGINGS: {
                sign.setLine(1, "Приседание");
                break;
            }
            case MONSTER_EGG: {
                sign.setLine(1, "УбийствоМоба");
                break;
            }
            case STONE_SWORD: {
                sign.setLine(1, "УронМобу");
                break;
            }
            case GLOWSTONE_DUST: {
                sign.setLine(1, "ПоднятиеПредм");
                break;
            }
            case SUGAR: {
                sign.setLine(1, "ДропПредм");
                break;
            }
            case PORK: {
                sign.setLine(1, "КликПоМобу");
                break;
            }
            case STONE: {
                sign.setLine(1, "ПоставилБлок");
                break;
            }
            case COBBLESTONE: {
                sign.setLine(1, "СломалБлок");
                break;
            }

        }
        sign.update();
        player.closeInventory();
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, Integer.MAX_VALUE, 1);
    }
}
