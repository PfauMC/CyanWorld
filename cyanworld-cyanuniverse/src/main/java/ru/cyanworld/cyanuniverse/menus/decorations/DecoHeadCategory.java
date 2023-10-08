package ru.cyanworld.cyanuniverse.menus.decorations;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.cyanworld.cyan1dex.Cyan1dex;
import ru.cyanworld.cyan1dex.CyanEcoManager;
import ru.cyanworld.cyan1dex.api.CustomItem;
import ru.cyanworld.cyan1dex.api.CustomMenu;
import ru.cyanworld.cyan1dex.menus.BuyingMenu;
import ru.cyanworld.cyanuniverse.CyanUniverse;

import java.util.List;

import static ru.cyanworld.cyan1dex.Cyan1dex.cfgplayers;
import static ru.cyanworld.cyan1dex.Cyan1dex.saveCfg;
import static ru.cyanworld.cyanuniverse.CyanUniverse.server;

public class DecoHeadCategory extends CustomMenu {
    public DecoHeadCategory(String name) {
        super(server, 6, name);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        String uuid = player.getUniqueId().toString();
        ItemStack item = event.getCurrentItem();
        ItemMeta meta = item.getItemMeta();

        if (!CustomItem.isHasLocName(item)) return;
        String id = meta.getLocalizedName();
        List<String> list = Cyan1dex.cfgplayers.getStringList(uuid + ".bought");

        if (list.contains(id)) {
            player.closeInventory();
            player.getInventory().addItem(item);
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.5f, 1.5f);
        } else {
            int price = CyanUniverse.itemPrice.get(id);
            if (event.getClick().name().startsWith("SHIFT")) {
                int balance = CyanEcoManager.getEco(player);
                if (balance < price) {
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, Integer.MAX_VALUE, 1);
                    player.sendTitle("§cНедостаточно монеток!", "Цена: §b" + price + " §rНужно еще: §b" + (price - balance), 0, 60, 10);
                    return;
                }
                list.add(id);
                cfgplayers.set(uuid + ".bought", list);
                saveCfg(cfgplayers, "players.yml");
                player.sendTitle("§aКупленно!", meta.getDisplayName(), 10, 60, 10);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, Integer.MAX_VALUE, 0.5f);
                CyanEcoManager.addEco(player, price * -1, false);

                player.openInventory(DecoHeads.getMenu(player, id.split("\\.")[2]));
            } else player.openInventory(new BuyingMenu(item, price).getInventory());
        }
    }
}