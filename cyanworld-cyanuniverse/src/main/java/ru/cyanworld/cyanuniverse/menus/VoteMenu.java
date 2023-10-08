package ru.cyanworld.cyanuniverse.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.cyanworld.cyan1dex.api.CustomItem;
import ru.cyanworld.cyan1dex.api.CustomMenu;
import ru.cyanworld.cyanuniverse.WorldManager;

import java.util.List;

import static ru.cyanworld.cyan1dex.listeners.EventListener.decimalFormat;
import static ru.cyanworld.cyanuniverse.CyanUniverse.server;
import static ru.cyanworld.cyanuniverse.CyanUniverse.worldscfg;

public class VoteMenu extends CustomMenu {
    public VoteMenu() {
        super(server, 3, "Голосование за мир");
        setupItems();
    }

    private void setupItems() {
        inventory.setItem(11, new CustomItem(Material.STAINED_CLAY).durability(14).name("§cУжасно"));
        inventory.setItem(12, new CustomItem(Material.STAINED_CLAY).durability(1).name("§6Плохо"));
        inventory.setItem(13, new CustomItem(Material.STAINED_CLAY).durability(4).name("§eНормально"));
        inventory.setItem(14, new CustomItem(Material.STAINED_CLAY).durability(5).name("§aХорошо"));
        inventory.setItem(15, new CustomItem(Material.STAINED_CLAY).durability(3).name("§bОтлично"));
        inventory.setItem(26, new CustomItem(Material.MAGMA_CREAM).name("Не голосовать"));
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        String playeruuid = player.getUniqueId().toString();
        String worldname = player.getWorld().getName();
        if (item.getType() == Material.MAGMA_CREAM) {
            WorldManager.teleportToLobby(player);
            return;
        }
        List<String> list = worldscfg.getStringList(worldname + ".votes");
        list.remove(playeruuid + "#0");
        switch (item.getDurability()) {
            case 14: {
                list.add(playeruuid + "#1");
                break;
            }
            case 1: {
                list.add(playeruuid + "#2");
                break;
            }
            case 4: {
                list.add(playeruuid + "#3");
                break;
            }
            case 5: {
                list.add(playeruuid + "#4");
                break;
            }
            case 3: {
                list.add(playeruuid + "#5");
                break;
            }
        }
        player.getWorld().getPlayers().forEach(Player -> {
            Player.sendMessage(player.getDisplayName() + " §7проголосовал за мир оценкой " + item.getItemMeta().getDisplayName());
        });
        WorldManager.teleportToLobby(player);
        worldscfg.set(worldname + ".votes", list);
        final int[] votecount = {0};
        final double[] votescore = {0};
        list.forEach(String -> {
            int vote = Integer.parseInt(String.split("#")[1]);
            if (vote == 0) return;
            votecount[0]++;
            votescore[0] = votescore[0] + vote;
        });
        votescore[0] /= votecount[0];
        String formattedDouble = decimalFormat.format(votescore[0]);
        worldscfg.set(worldname + ".votecount", votecount[0]);
        worldscfg.set(worldname + ".votescore", formattedDouble.replace(",", "."));
    }
}
