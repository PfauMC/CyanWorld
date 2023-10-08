package ru.cyanworld.cyanuniverse;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.cyanworld.cyan1dex.api.CustomItem;
import ru.cyanworld.cyan1dex.api.ItemBuilder;

public class ItemsList {
    public static ItemStack lobby = new CustomItem(new ItemStack(Material.MAGMA_CREAM), "Выйти в лобби");
    public static ItemStack worldscompass = new CustomItem(new ItemStack(Material.COMPASS), "Меню");
    public static ItemStack deco = new CustomItem(new ItemStack(Material.PRISMARINE_CRYSTALS), "Декорации", "ru.cyanworld.cyanuniverse.deco");
    public static ItemStack myworlds = new ItemBuilder(Material.MAP).dura(25566).name("Мои миры").locname("ru.cyanworld.cyanuniverse.myworlds").build();
    public static ItemStack myworld_building = new CustomItem(new ItemStack(Material.BRICK), "Управление строительством", "ru.cyanworld.cyanuniverse.myworld.build").lore(new String[]{"§7Запретить/Разрешить строить"});
    public static ItemStack myworld_spawn = new CustomItem(new ItemStack(Material.ENDER_PEARL), "Точка спауна", "ru.cyanworld.cyanuniverse.myworld.build").lore(new String[]{"§7ЛКМ - Установить на текущую позицию", "§7ПКМ - Сбросить на стандартную"});
    public static ItemStack myworld_kick = new CustomItem(new ItemStack(Material.SKULL_ITEM, 1, (short) 3), "Выгнать игрока", "ru.cyanworld.cyanuniverse.myworld.build").lore(new String[]{"§7Кто-то мешает?", "§7Выгони его!"});
    public static ItemStack myworld_time = new CustomItem(new ItemStack(Material.WATCH), "Управление временем", "ru.cyanworld.cyanuniverse.myworld.build").lore(new String[]{"§7Сделать Утро/День/Вечер/Ночь"});


}
