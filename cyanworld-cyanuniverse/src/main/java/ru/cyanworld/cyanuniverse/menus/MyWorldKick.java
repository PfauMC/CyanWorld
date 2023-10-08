package ru.cyanworld.cyanuniverse.menus;

import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.cyanworld.cyan1dex.api.CustomItem;
import ru.cyanworld.cyan1dex.api.CustomMenu;
import ru.cyanworld.cyanuniverse.MenusList;
import ru.cyanworld.cyanuniverse.WorldManager;

import java.util.Map;
import java.util.UUID;

import static ru.cyanworld.cyanuniverse.CyanUniverse.server;

public class MyWorldKick extends CustomMenu {
    public static Map<UUID, MyWorldKick> map = Maps.newHashMap();

    String worldname;
    Player player;

    public MyWorldKick(Player player) {
        super(server, 3, "Выгнать игрока");
        this.player = player;
        this.worldname = player.getWorld().getName();
        setupItems();
    }

    private void setupItems() {
        player.getWorld().getPlayers().forEach(Player -> {
            if (Player == player) return;
            if (Player.isOp()) return;
            String uuid = Player.getUniqueId().toString();
            inventory.addItem(new CustomItem(new ItemStack(Material.SKULL_ITEM, 1, (short) 3), Player.getDisplayName(), uuid).skull(Player));
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
                    player.openInventory(new MyWorldKick(player).getInventory());
                    return;
                }
                if (subject.isOp()) {
                    player.closeInventory();
                    player.sendTitle("", "Админа нельзя выгнать >:D", 0, 60, 20);
                    subject.sendMessage(player.getDisplayName() + " хотел тебя выгнать, вот засранец да?");
                    return;
                }
                player.getWorld().getPlayers().forEach(Player -> Player.sendMessage(subject.getDisplayName() + " §7выгнали из мира"));
                WorldManager.teleportToLobby(subject);
                subject.sendTitle("", "Вас выгнали из мира", 5, 60, 10);
                player.openInventory(MenusList.myWorldMenu.inventory);
                break;
            }
        }
    }
}
