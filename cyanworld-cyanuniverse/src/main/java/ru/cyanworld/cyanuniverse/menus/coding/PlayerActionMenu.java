package ru.cyanworld.cyanuniverse.menus.coding;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import ru.cyanworld.cyan1dex.api.CustomMenu;
import ru.cyanworld.cyan1dex.api.ItemBuilder;
import ru.cyanworld.cyanuniverse.menus.coding.PlayerAction.*;

import java.util.Arrays;

import static ru.cyanworld.cyanuniverse.CyanUniverse.server;

public class PlayerActionMenu extends CustomMenu {
    public Sign sign;

    public PlayerActionMenu(Sign sign) {
        super(server, 3, "Сделать игроку");
        this.sign = sign;
        initItems();
    }

    public void initItems() {
        this.inventory.addItem(new ItemBuilder(Material.CHEST).name("§6Управление инвентарём").lore(Arrays.asList("§7Всё что связанно с", "§7инвентарями и предметами")).build());
        this.inventory.addItem(new ItemBuilder(Material.BEACON).name("§cКоммуникация").lore(Arrays.asList("§7Отправить игроку сообщение,", "§7проиграть звук и другое")).build());
        this.inventory.addItem(new ItemBuilder(Material.LEATHER_BOOTS).name("§7Перемещение").itemflag(ItemFlag.HIDE_ATTRIBUTES).lore(Arrays.asList("§7Телепортация, энерция,", "§7кинетическая энергия,", "§7§m§o§lквантовое тунелирование", "§7и всё то, что заставляет игрока", "§7перемещаться в пространстве")).build());
        this.inventory.addItem(new ItemBuilder(Material.APPLE).name("§aПараметры").lore(Arrays.asList("§7Изменить различные параметры игрока", "§7например здоровье, сытость, опыт, эффекты")).build());
        this.inventory.addItem(new ItemBuilder(Material.ANVIL).name("§dНастройки").lore(Arrays.asList("§7Базовые настройки для игрока", "§7например режим игры, полёт и другое")).build());
    }

    //коммуникация
    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        if (item == null || item.getType() == Material.AIR) return;
        switch (item.getType()) {
            case CHEST: {
                player.openInventory(new PlayerActionItems(sign).getInventory());
                break;
            }
            case BEACON: {
                player.openInventory(new PlayerActionCommunication(sign).getInventory());
                break;
            }
            case LEATHER_BOOTS: {
                player.openInventory(new PlayerActionMovement(sign).getInventory());
                break;
            }
            case APPLE: {
                player.openInventory(new PlayerActionStatistics(sign).getInventory());
                break;
            }
            case ANVIL: {
                player.openInventory(new PlayerActionSettings(sign).getInventory());
                break;
            }
        }
    }
}
