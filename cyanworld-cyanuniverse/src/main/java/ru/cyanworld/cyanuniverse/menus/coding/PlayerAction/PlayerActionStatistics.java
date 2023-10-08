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

import static ru.cyanworld.cyanuniverse.CyanUniverse.server;

public class PlayerActionStatistics extends CustomMenu {
    public Sign sign;

    public PlayerActionStatistics(Sign sign) {
        super(server, 3, "Сделать игроку - Параметры");
        this.sign = sign;
        initItems();
    }

    public void initItems() {
        this.inventory.addItem(new ItemBuilder(Material.COOKIE).name("§dЗдоровье").build());
        this.inventory.addItem(new ItemBuilder(Material.COOKED_CHICKEN).name("§6Сытость").build());
        this.inventory.addItem(new ItemBuilder(Material.EXP_BOTTLE).name("§5Уровень опыта").build());
        this.inventory.addItem(new ItemBuilder(Material.EMERALD).name("§2Шкала опыта").build());
        this.inventory.addItem(new ItemBuilder(Material.BREWING_STAND_ITEM).name("§aДобавить эффект").build());
        this.inventory.addItem(new ItemBuilder(Material.CAULDRON_ITEM).name("§cУдалить эффект").build());
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        if (item == null || item.getType() == Material.AIR) return;
        sign.getLocation().clone().add(0, 1, -1).getBlock().setType(Material.AIR);
        switch (item.getType()) {
            case COOKIE: {
                sign.setLine(1, "Здоровье");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Число (0-20)");
                player.sendMessage(" \n§7§lСделать игроку - Здоровье§r\n" +
                        "0 Слот - Уровень здоровья от 0 до 20 (Число)\n "
                );
                break;
            }
            case COOKED_CHICKEN: {
                sign.setLine(1, "Сытость");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Число (0-20)");
                player.sendMessage(" \n§7§lСделать игроку - Сытость§r\n" +
                        "0 Слот - Уровень сытости от 0 до 20 (Число)\n "
                );
                break;
            }
            case EXP_BOTTLE: {
                sign.setLine(1, "УровеньОпыта");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Число");
                player.sendMessage(" \n§7§lСделать игроку - Уровень опыта§r\n" +
                        "0 Слот - Уровень опыта (Число)\n "
                );
                break;
            }
            case EMERALD: {
                sign.setLine(1, "ШкалаОпыта");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Число (0-100)");
                player.sendMessage(" \n§7§lСделать игроку - Шкала опыта§r\n" +
                        "0 Слот - Заполненость шкалы от 0 до 100 (Число)\n "
                );
                break;
            }
            case BREWING_STAND_ITEM: {
                sign.setLine(1, "ДобЭффект");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Эффект, Число, Число");
                player.sendMessage(" \n§7§lСделать игроку - Добавить эффект§r\n" +
                        "0 Слот - Эффект или название эффекта (Эффект/Текст)\n" +
                        "1 Слот - Длительность в тиках (Число)\n" +
                        "2 Слот - Уровень эффекта (Число)\n" +
                        "§7§lПодсказка: §r1 секунда = 20 тикам, 2 секунды = 40, полсекунды = 10\n "
                );
                break;
            }
            case CAULDRON_ITEM: {
                sign.setLine(1, "УдалЭффект");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Эффект, Число, Число");
                player.sendMessage(" \n§7§lСделать игроку - Удалить эффект§r\n" +
                        "0 Слот - Эффект или название эффекта (Эффект/Текст)\n "
                );
                break;
            }
        }
        sign.update();
        player.closeInventory();
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, Integer.MAX_VALUE, 1);
    }
}
