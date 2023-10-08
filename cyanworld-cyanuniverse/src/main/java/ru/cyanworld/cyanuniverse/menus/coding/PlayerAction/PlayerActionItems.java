package ru.cyanworld.cyanuniverse.menus.coding.PlayerAction;

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

public class PlayerActionItems extends CustomMenu {
    public Sign sign;

    public PlayerActionItems(Sign sign) {
        super(server, 3, "Сделать игроку - Управление предметами");
        this.sign = sign;
        initItems();
    }

    public void initItems() {
        this.inventory.addItem(new ItemBuilder(Material.BOOK).name("§6Выдать предметы").lore(Arrays.asList("§7Предмет(ы)")).build());
        this.inventory.addItem(new ItemBuilder(Material.ENDER_CHEST).name("§5Поставить предмет в слот").lore(Arrays.asList("§7Предмет, Число")).build());
        this.inventory.addItem(new ItemBuilder(Material.IRON_CHESTPLATE).name("§6Надеть броню").itemflag(ItemFlag.HIDE_ATTRIBUTES).lore(Arrays.asList("§7Верхняя строка, с лево на право:", "Шлем, нагрудник, поножи, ботинки")).build());
        this.inventory.addItem(new ItemBuilder(Material.WEB).name("§eУдалить предмет(ы)").build());
        this.inventory.addItem(new ItemBuilder(Material.GLASS).name("§eОчистить инвентарь").build());
        this.inventory.addItem(new ItemBuilder(Material.END_BRICKS).name("§aСохранить инвентарь").build());
        this.inventory.addItem(new ItemBuilder(Material.ENDER_STONE).name("§eЗагрузить инвентарь").build());
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
                sign.setLine(1, "ВыдатьПредмет");

                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Предмет(ы)");

                break;
            }
            case ENDER_CHEST: {
                sign.setLine(1, "ПредметВСлот");

                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Предмет, Число");
                player.sendMessage(" \n§7§lСделать игроку - Поставить предмет в слот§r\n" +
                        "0 Слот - Предмет который будет помещён (Предмет)\n" +
                        "1 Слот - Номер слота от 0 до 40 (Число)\n" +
                        "§7§lПодсказка: §r0-8 - пояс игрока. 9-35 - инвентарь игрока. 36-39 - броня. 40 - вторая рука. Так же можно оставить предмет пустым, чтобы удалить предмет в слоте\n "
                );
                break;
            }
            case IRON_CHESTPLATE: {
                sign.setLine(1, "НадетьБроню");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Шлем, нагрудник, поножи, ботинки");
                player.sendMessage(" \n§7§lСделать игроку - Надеть броню§r\n" +
                        "0 Слот - Шлем (Предмет)\n" +
                        "1 Слот - Нагрудник (Предмет)\n" +
                        "2 Слот - Поножи (Предмет)\n" +
                        "3 Слот - Ботинки (Предмет)\n" +
                        "§7§lПодсказка: §rВ слот шлема можно ставить любые предметы, начиная от блоков и заканчивая флагами\n "
                );
                break;
            }
            case WEB: {
                sign.setLine(1, "УдалитьПредмет");
                Block chestblock = sign.getLocation().clone().add(0, 1, -1).getBlock();
                chestblock.setType(Material.CHEST);
                Chest chest = (Chest) chestblock.getState();
                chest.setCustomName("Предмет(ы)");
                break;
            }
            case GLASS: {
                sign.setLine(1, "ОчиститьИнв");
                break;
            }
            case END_BRICKS: {
                sign.setLine(1, "СохранитьИнв");
                player.sendMessage(" \n§7§lСделать игроку - Сохранить инвентарь§r\n" +
                        "§7§lПодсказка: §rСохраняет инвентарь в буфер мира. У каждого игрока может быть только 1 сохранённый инвентарь\n"
                );
                break;
            }
            case ENDER_STONE: {
                sign.setLine(1, "ЗагрузитьИнв");
                player.sendMessage(" \n§7§lСделать игроку - Загрузить инвентарь§r\n" +
                        "§7§lПодсказка: §rЗагружает инвентарь из буфера мира. У каждого игрока может быть только 1 сохранённый инвентарь\n"
                );
                break;
            }
        }
        sign.update();
        player.closeInventory();
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, Integer.MAX_VALUE, 1);
    }
}
