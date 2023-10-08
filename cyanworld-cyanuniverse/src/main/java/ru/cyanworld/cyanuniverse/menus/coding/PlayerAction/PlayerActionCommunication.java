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

public class PlayerActionCommunication extends CustomMenu {
    public Sign sign;

    public PlayerActionCommunication(Sign sign) {
        super(server, 3, "Сделать игроку - Коммуникация");
        this.sign = sign;
        initItems();
    }

    public void initItems() {
        this.inventory.addItem(new ItemBuilder(Material.BOOK).name("§eОтправить сообщение").lore(Arrays.asList("§7Текст")).build());
        this.inventory.addItem(new ItemBuilder(Material.GLASS).name("§cОчистить чат").build());
        this.inventory.addItem(new ItemBuilder(Material.TORCH).name("§eСообщение на ЭкшнБар").lore(Arrays.asList("§7Текст")).build());
        this.inventory.addItem(new ItemBuilder(Material.SIGN).name("§eСообщение на экран (Титл)").lore(Arrays.asList("§7Текст, Текст,", "§7Число (Появление в тиках),", "§7Число (Задержка в тиках),", "§7Число (Затухание в тиках)")).build());
        this.inventory.addItem(new ItemBuilder(Material.RECORD_3).name("§aЗвук").lore(Arrays.asList("§7Текст, §7Число")).build());
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        if (item == null || item.getType() == Material.AIR) return;
        sign.getLocation().clone().add(0, 1, -1).getBlock().setType(Material.AIR);
        switch (item.getType()) {
            case BOOK: {
                sign.setLine(1, "ОтпрСообщение");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Текст");

                player.sendMessage(" \n§7§lСделать игроку - Отправить сообщение§r\n" +
                        "0 Слот - Сообщение (Текст)\n "
                );
                break;
            }
            case GLASS: {
                sign.setLine(1, "ОчиститьЧат");
                player.sendMessage(" \n§7§lСделать игроку - Очистить чат§r\n" +
                        "§7§lПодсказка: §rпросто очищает чат и ничего более\n "
                );
                break;
            }
            case TORCH: {
                sign.setLine(1, "ЭкшнБар");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Текст");

                player.sendMessage(" \n§7§lСделать игроку - Сообщение на ЭкшнБар§r\n" +
                        "0 Слот - Сообщение (Текст)\n "
                );
                break;
            }
            case SIGN: {
                sign.setLine(1, "Титл");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Текст, Текст, Появление, Задержка, Затухание");

                player.sendMessage(" \n§7§lСделать игроку - Сообщение на экран (Титл)§r\n" +
                        "0 Слот - Главный текст (Текст)\n" +
                        "1 Слот - Нижний текст (Текст)\n" +
                        "2 Слот - Время появления в тиках (Число)\n" +
                        "3 Слот - Время задержки в тиках (Число)\n" +
                        "4 Слот - Время затухания в тиках (Число)\n" +
                        "§7§lПодсказка: §r1 секунда = 20 тикам, 2 секунды = 40, полсекунды = 10\n "
                );
                break;
            }
            case RECORD_3: {
                sign.setLine(1, "Звук");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Название, Тональность");

                player.sendMessage(" \n§7§lСделать игроку - Звук§r\n" +
                        "0 Слот - Название звука (Текст)\n" +
                        "1 Слот - Тональность (Число)\n" +
                        "§7§lПодсказка: §rТональность - число в пределах от 0.5 до 2.0, Список звуков можно посмотреть тут: https://vk.cc/6NoOCh\n ");
                break;
            }
        }
        sign.update();
        player.closeInventory();
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, Integer.MAX_VALUE, 1);
    }
}
