package ru.cyanworld.cyanuniverse.menus;

import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.cyanworld.cyan1dex.CyanEcoManager;
import ru.cyanworld.cyan1dex.api.CustomItem;
import ru.cyanworld.cyan1dex.api.CustomMenu;
import ru.cyanworld.cyanuniverse.CyanUniverse;

import java.io.File;
import java.util.Map;
import java.util.UUID;

import static ru.cyanworld.cyanuniverse.CyanUniverse.server;
import static ru.cyanworld.cyanuniverse.CyanUniverse.worldscfg;

public class DeleteWorld extends CustomMenu {
    public static Map<UUID, String> map = Maps.newHashMap();

    public DeleteWorld() {
        super(server, 3, "Удаление мира");
        setupItems();
    }

    private void setupItems() {
        for (int i = 0; i < 27; i++) {
            if (i == 11 || i == 15) continue;
            inventory.setItem(i, GLASS);
        }
        inventory.addItem(new CustomItem(new ItemStack(Material.GRASS), "Не удалять").lore(new String[]{"§7Закрыть меню удаления"}));
        inventory.addItem(new CustomItem(new ItemStack(Material.LAVA_BUCKET), "Удалить мир").lore(new String[]{"§7Востановить мир будет невозможно!"}));
    }

    public Inventory getInv(UUID uuid, String worldname) {
        map.put(uuid, worldname);
        return inventory;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        if (item.getType() == Material.STAINED_GLASS_PANE) return;
        Player player = (Player) event.getWhoClicked();
        String worldname = map.get(player.getUniqueId());
        Integer RemCyans = (Integer) worldscfg.get(worldname + ".votecount");
        Integer RemCyans1 = (RemCyans * 5) + RemCyans;
        switch (item.getType()) {
            case LAVA_BUCKET: {
                CyanUniverse.worldscfg.set(worldname, null);
                World world = server.getWorld(worldname);
                try {
                    if (world != null) server.unloadWorld(world, true);
                    CyanEcoManager.addEco(player, -RemCyans1, true);
                    player.sendMessage("§b-" + RemCyans1 + " монеток");
                    FileUtils.deleteDirectory(new File(server.getWorldContainer(), worldname));
                } catch (Exception ignored) {
                }
                player.playSound(player.getLocation(), Sound.ENTITY_SPIDER_DEATH, Integer.MAX_VALUE, 1);
                player.openInventory(new MyWorlds(player).getInventory());
                break;
            }
            case GRASS: {
                player.openInventory(new MyWorlds(player).getInventory());
                break;
            }
        }
    }
}
