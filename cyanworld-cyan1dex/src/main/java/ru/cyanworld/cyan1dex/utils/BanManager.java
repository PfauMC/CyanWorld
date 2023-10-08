package ru.cyanworld.cyan1dex.utils;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import ru.cyanworld.cyan1dex.Cyan1dex;

import java.util.Date;

public class BanManager {
    public static String kick(Player player, String reason, String kickedby) {
        if (player == null || !player.isOnline()) {
            return "Игрок оффлайн";
        }
        if (player.isOp() && !kickedby.equals("§fСервер")) {
            return "Вы не можете кикнуть администратора!";
        }
        Cyan1dex.server.broadcastMessage(kickedby + "§7: " + player.getDisplayName() + " §fкикнут по причине: §r" + reason);
        player.kickPlayer("§lВы кикнуты!\n§rПричина: " + reason + "\n  \n" + kickedby + "§f: §7" + new Date());
        return null;
    }

    public static String ban(String name, String reason, String kickedby) {
        Cyan1dex.server.broadcastMessage(kickedby + "§7: " + name + " §f§lзабанен по причине: §r" + reason);
        Cyan1dex.server.getOnlinePlayers().forEach(players -> players.playSound(players.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN, 2.14748365E9f, 1.0f)
        );
        Player player = Bukkit.getPlayer(name);
        if (player != null && player.isOnline()) {
            Bukkit.getBanList(BanList.Type.IP).addBan(player.getAddress().getAddress().getHostAddress(), "\n §lВы забанены!\n§rПричина: " + reason + "\n \n" + kickedby + "§f: §7" + new Date() + "\n", null, null);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Cyan1dex.instance, () -> {
                        player.kickPlayer("\n §lВы забанены!\n§rПричина: " + reason + "\n \n" + kickedby + "§f: §7" + new Date() + "\n");
                    }
                    , 20);
        }
        Bukkit.getBanList(BanList.Type.NAME).addBan(name, " \n§lВы забанены!\n§rПричина: " + reason + "\n \n" + kickedby + "§f: §7" + new Date() + "\n", null, null);
        return null;
    }
}
