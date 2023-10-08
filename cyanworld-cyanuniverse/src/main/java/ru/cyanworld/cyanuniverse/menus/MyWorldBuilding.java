package ru.cyanworld.cyanuniverse.menus;

import com.google.common.collect.Maps;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.cyanworld.cyan1dex.api.CustomItem;
import ru.cyanworld.cyan1dex.api.CustomMenu;

import java.util.Map;
import java.util.UUID;

import static ru.cyanworld.cyanuniverse.CyanUniverse.server;

public class MyWorldBuilding extends CustomMenu {
    public static Map<UUID, MyWorldBuilding> map = Maps.newHashMap();

    String worldname;
    Player player;

    public MyWorldBuilding(Player player) {
        super(server, 3, "Управление строительством");
        this.player = player;
        this.worldname = player.getWorld().getName();
        setupItems();
    }

    private void setupItems() {
        player.getWorld().getPlayers().forEach(Player -> {
            if (Player == player) return;
            String status = Player.getGameMode() == GameMode.CREATIVE ? "§aРазрешено" : "§cЗапрещено";
            String uuid = Player.getUniqueId().toString();
            inventory.addItem(new CustomItem(new ItemStack(Material.SKULL_ITEM, 1, (short) 3), Player.getDisplayName(), uuid).skull(Player).lore(new String[]{status}));
        });
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        switch (item.getType()) {
            case SKULL_ITEM: {
                String uuid = item.getItemMeta().getLocalizedName();
                Player subject = server.getPlayer(UUID.fromString(uuid));
                if (subject == null || subject.getWorld() != player.getWorld()) {
                    player.openInventory(new MyWorldBuilding(player).getInventory());
                    return;
                }
                if (subject.getGameMode() == GameMode.CREATIVE) {
                    subject.setGameMode(GameMode.ADVENTURE);
                    subject.sendTitle(" ", "§cВам запретили строить", 5, 60, 10);
                } else {
                    subject.setGameMode(GameMode.CREATIVE);
                    subject.sendTitle(" ", "§aВам разрешили строить", 5, 60, 10);
                }
                player.openInventory(new MyWorldBuilding(player).getInventory());
                break;
            }
        }
    }
}