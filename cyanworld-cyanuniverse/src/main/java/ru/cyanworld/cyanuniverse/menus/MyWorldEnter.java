package ru.cyanworld.cyanuniverse.menus;

import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import ru.cyanworld.cyan1dex.api.CustomItem;
import ru.cyanworld.cyan1dex.api.CustomMenu;
import ru.cyanworld.cyanuniverse.CyanUniverse;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import static ru.cyanworld.cyanuniverse.CyanUniverse.server;

public class MyWorldEnter extends CustomMenu {
    public static Map<UUID, MyWorldEnter> map = Maps.newHashMap();
    public World world;

    public MyWorldEnter(Player player) {
        super(server, 3, "Управление входом");
        this.world = player.getWorld();
        setupItems();
    }

    private void setupItems() {
        //.ench("LUCK-1")
        switch (CyanUniverse.worldscfg.getString(world.getName() + ".allowenter")) {
            case "all": {
                inventory.setItem(11, new CustomItem(new ItemStack(Material.WOOD_DOOR), "Разрешить всем входить").lore(Arrays.asList("§aВыбранно")).ench(Enchantment.LUCK, 1).hideFlags(ItemFlag.HIDE_ENCHANTS));
                inventory.setItem(13, new CustomItem(new ItemStack(Material.NOTE_BLOCK), "Разрешить входить донатерам"));
                inventory.setItem(15, new CustomItem(new ItemStack(Material.BARRIER), "Запретить всем входить"));
                break;
            }
            case "nobody": {
                inventory.setItem(11, new CustomItem(new ItemStack(Material.WOOD_DOOR), "Разрешить всем входить"));
                inventory.setItem(13, new CustomItem(new ItemStack(Material.NOTE_BLOCK), "Разрешить входить донатерам"));
                inventory.setItem(15, new CustomItem(new ItemStack(Material.BARRIER), "Запретить всем входить").lore(Arrays.asList("§aВыбранно")).ench(Enchantment.LUCK, 1).hideFlags(ItemFlag.HIDE_ENCHANTS));
                break;
            }
            case "donator": {
                inventory.setItem(11, new CustomItem(new ItemStack(Material.WOOD_DOOR), "Разрешить всем входить"));
                inventory.setItem(13, new CustomItem(new ItemStack(Material.NOTE_BLOCK), "Разрешить входить донатерам").lore(Arrays.asList("§aВыбранно")).ench(Enchantment.LUCK, 1).hideFlags(ItemFlag.HIDE_ENCHANTS));
                inventory.setItem(15, new CustomItem(new ItemStack(Material.BARRIER), "Запретить всем входить"));
                break;
            }
        }
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        if (item.getType() == Material.STAINED_GLASS_PANE) return;
        Player player = (Player) event.getWhoClicked();
        World world = player.getWorld();
        switch (item.getType()) {
            case WOOD_DOOR: {
                CyanUniverse.worldscfg.set(world.getName() + ".allowenter", "all");
                break;
            }
            case BARRIER: {
                CyanUniverse.worldscfg.set(world.getName() + ".allowenter", "nobody");
                break;
            }
            case NOTE_BLOCK: {
                CyanUniverse.worldscfg.set(world.getName() + ".allowenter", "donator");
                break;
            }
        }
        player.openInventory(new MyWorldEnter(player).getInventory());
    }
}
