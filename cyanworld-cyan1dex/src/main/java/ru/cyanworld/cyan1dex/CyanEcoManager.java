package ru.cyanworld.cyan1dex;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CyanEcoManager {
    public static void addEco(Player player, int eco) {
        CyanEcoManager.addEco(player, eco, false);
    }

    public static void addEco(String uuid, int eco) {
        CyanEcoManager.addEco(uuid, eco, true);
    }

    public static void addEco(Player player, int eco, boolean sound) {
        if (Config.isMainServer) {
            Cyan1dex.cfgplayers.set(player.getUniqueId() + ".eco", Cyan1dex.cfgplayers.getInt(player.getUniqueId() + ".eco") + eco);
        } else {
            Cyan1dex.ecoMap.put(player, Cyan1dex.ecoMap.get(player) + eco);
        }
        if (sound) {
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2.14748365E9f, 2.0f);
        }
    }

    public static void addEco(String uuid, int eco, boolean sound) {
        Player player = Cyan1dex.server.getPlayer(UUID.fromString(uuid));
        if (Config.isMainServer) {
            Cyan1dex.cfgplayers.set(uuid + ".eco", Cyan1dex.cfgplayers.getInt(uuid + ".eco") + eco);
        } else if (player != null) {
            Cyan1dex.ecoMap.put(player, Cyan1dex.ecoMap.get(player) + eco);
        }
        if (sound && player != null) {
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2.14748365E9f, 2.0f);
        }
    }

    public static void setEco(String uuid, int eco) {
        if (Config.isMainServer) {
            Cyan1dex.cfgplayers.set(uuid + ".eco", Cyan1dex.cfgplayers.getInt(uuid + ".eco") + eco);
        }
    }

    public static int getEco(Player player) {
        int eco = 0;
        if (Config.isMainServer) {
            eco = Cyan1dex.cfgplayers.getInt(player.getUniqueId() + ".eco");
        }
        return eco;
    }
}
