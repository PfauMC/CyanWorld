package ru.cyanworld.cyanuniverse.menus.coding.variables;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.cyanworld.cyan1dex.Utils;
import ru.cyanworld.cyan1dex.api.CustomMenu;
import ru.cyanworld.cyan1dex.api.ItemBuilder;

import java.util.Arrays;

import static ru.cyanworld.cyanuniverse.CyanUniverse.server;

public class EffectMenu extends CustomMenu {
    public static ItemStack[] contents;

    public EffectMenu() {
        super(server, 3, "Эффекты");
        initItems();
    }

    public void initItems() {
        if (contents != null) {
            this.inventory.setContents(contents);
        } else {
            this.inventory.addItem(new ItemBuilder(Material.GLASS_BOTTLE).name("§fspeed").lore(Arrays.asList("§7Скорость")).build());
            this.inventory.addItem(new ItemBuilder(Material.GLASS_BOTTLE).name("§fslow").lore(Arrays.asList("§7Замедление")).build());
            this.inventory.addItem(new ItemBuilder(Material.GLASS_BOTTLE).name("§ffast_digging").lore(Arrays.asList("§7Спешка")).build());
            this.inventory.addItem(new ItemBuilder(Material.GLASS_BOTTLE).name("§fslow_digging").lore(Arrays.asList("§7Утомление")).build());
            this.inventory.addItem(new ItemBuilder(Material.GLASS_BOTTLE).name("§fincrease_damage").lore(Arrays.asList("§7Сила")).build());
            this.inventory.addItem(new ItemBuilder(Material.GLASS_BOTTLE).name("§fheal").lore(Arrays.asList("§7Лечение")).build());
            this.inventory.addItem(new ItemBuilder(Material.GLASS_BOTTLE).name("§fharm").lore(Arrays.asList("§7Урон")).build());
            this.inventory.addItem(new ItemBuilder(Material.GLASS_BOTTLE).name("§fjump").lore(Arrays.asList("§7Прыгучесть")).build());
            this.inventory.addItem(new ItemBuilder(Material.GLASS_BOTTLE).name("§fconfusion").lore(Arrays.asList("§7Тошнота")).build());
            this.inventory.addItem(new ItemBuilder(Material.GLASS_BOTTLE).name("§fregeneration").lore(Arrays.asList("§7Регенерация")).build());
            this.inventory.addItem(new ItemBuilder(Material.GLASS_BOTTLE).name("§fdamage_resistance").lore(Arrays.asList("§7Сопротивление урону")).build());
            this.inventory.addItem(new ItemBuilder(Material.GLASS_BOTTLE).name("§ffire_resistance").lore(Arrays.asList("§7Огнестойчивость")).build());
            this.inventory.addItem(new ItemBuilder(Material.GLASS_BOTTLE).name("§fwater_breathing").lore(Arrays.asList("§7Подводное дыхание")).build());
            this.inventory.addItem(new ItemBuilder(Material.GLASS_BOTTLE).name("§finvisibility").lore(Arrays.asList("§7Невидимость")).build());
            this.inventory.addItem(new ItemBuilder(Material.GLASS_BOTTLE).name("§fblindness").lore(Arrays.asList("§7Слепота")).build());
            this.inventory.addItem(new ItemBuilder(Material.GLASS_BOTTLE).name("§fnight_vision").lore(Arrays.asList("§7Ночное зрение")).build());
            this.inventory.addItem(new ItemBuilder(Material.GLASS_BOTTLE).name("§fhunger").lore(Arrays.asList("§7Голод")).build());
            this.inventory.addItem(new ItemBuilder(Material.GLASS_BOTTLE).name("§fweakness").lore(Arrays.asList("§7Слабость")).build());
            this.inventory.addItem(new ItemBuilder(Material.GLASS_BOTTLE).name("§fpoison").lore(Arrays.asList("§7Отравление")).build());
            this.inventory.addItem(new ItemBuilder(Material.GLASS_BOTTLE).name("§fwither").lore(Arrays.asList("§7Иссушение")).build());
            this.inventory.addItem(new ItemBuilder(Material.GLASS_BOTTLE).name("§fhealth_boost").lore(Arrays.asList("§7Прилив здоровья")).build());
            this.inventory.addItem(new ItemBuilder(Material.GLASS_BOTTLE).name("§fabsorption").lore(Arrays.asList("§7Поглощение")).build());
            this.inventory.addItem(new ItemBuilder(Material.GLASS_BOTTLE).name("§fsaturation").lore(Arrays.asList("§7Насыщение")).build());
            this.inventory.addItem(new ItemBuilder(Material.GLASS_BOTTLE).name("§fglowing").lore(Arrays.asList("§7Свечение")).build());
            this.inventory.addItem(new ItemBuilder(Material.GLASS_BOTTLE).name("§flevitation").lore(Arrays.asList("§7Левитация")).build());
            contents = this.inventory.getContents();
        }
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) return;
        if (event.getClickedInventory().getHolder() != this) return;
        Player player = (Player) event.getWhoClicked();
        switch (item.getType()) {
            case GLASS_BOTTLE: {
                player.getInventory().addItem(item);
                Utils.playCustomSound("ok", player);
            }
        }
    }
}