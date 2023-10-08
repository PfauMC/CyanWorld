package ru.cyanworld.cyan1dex;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import ru.cyanworld.cyan1dex.messages.Msg;

public class Utils {
    public static Vector noVelocity = new Vector(0, 0, 0);

    public static boolean playerIsDonator(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            return Utils.playerIsDonator(player);
        }
        return true;
    }

    public static boolean playerIsDonator(Player player) {
        if (player.isOp()) {
            return true;
        }
        return Utils.playerIsDonator(player.getUniqueId().toString());
    }

    public static Boolean playerIsDonator(String uuid) {
        String group = Cyan1dex.cfgplayers.getString(uuid + ".group");
        if (group == null) {
            return false;
        }
        return !group.equals("player");
    }

    public static boolean playerIsModer(CommandSender sender) {
        if (sender.isOp()) {
            return true;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            return Utils.playerIsModer(player);
        }
        return true;
    }

    public static boolean playerIsModer(Player player) {
        if (player.isOp()) {
            return true;
        }
        return Utils.playerIsModer(player.getUniqueId().toString());
    }

    public static boolean playerIsModer(String uuid) {
        String group = Cyan1dex.cfgplayers.getString(uuid + ".group");
        if (group == null) {
            return false;
        }
        return group.equals("moder");
    }

    public static void teleportPlayer(Player player, Location location, Boolean timer) {
        if (timer) {
            player.teleport(location);
        } else {
            player.teleport(location);
        }
        player.sendMessage(Msg.teleporting);
    }

    public static void setPlayerPrefix(Player player) {
        String playerName = player.getName();
        String uuid = player.getUniqueId().toString();
        if (Cyan1dex.cfgplayers.getString(uuid + ".group") == null) {
            Cyan1dex.cfgplayers.set(uuid + ".group", "player");
        }
        switch (Cyan1dex.cfgplayers.getString(uuid + ".group")) {
            case "donator": {
                player.setDisplayName("§b[D] §r" + playerName);
                break;
            }
            case "moder": {
                player.setDisplayName("§9[M] §r" + playerName);
                break;
            }
            case "dev": {
                player.setDisplayName("§4[Dev] §r" + playerName);
                break;
            }
            default: {
                player.setDisplayName("§7" + playerName);
            }
        }
        player.setPlayerListName(player.getDisplayName());
        Cyan1dex.cfgplayers.set(player.getUniqueId().toString() + ".displayname", player.getDisplayName());
    }

    public static void updatePerWorldTab(World world) {
        if (!Config.perworldtab_enable) {
            return;
        }
        world.getPlayers().forEach(worldplayer -> {
                    Cyan1dex.server.getOnlinePlayers().forEach(onlineplayer -> {
                                if (onlineplayer.getWorld() == worldplayer.getWorld()) {
                                    worldplayer.showPlayer(onlineplayer);
                                } else {
                                    worldplayer.hidePlayer(onlineplayer);
                                }
                            }
                    );
                }
        );
    }

    public static void updatePerWorldTab(Player player) {
        Cyan1dex.server.getOnlinePlayers().forEach(players -> {
                    if (!Utils.playerIsModer(players)) {
                        players.hidePlayer(player);
                    }
                    if (players.getWorld() == player.getWorld()) {
                        player.showPlayer(players);
                    } else {
                        player.hidePlayer(players);
                    }
                }
        );
    }

    public static ArmorStand createHolo(String name, Location loc) {
        ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        as.setVisible(false);
        as.setMarker(true);
        as.setCustomName(name);
        as.setCustomNameVisible(true);
        as.setRemoveWhenFarAway(false);
        as.setGravity(false);
        return as;
    }

    public static void healthUpPlayer(Player player) {
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setFallDistance(0.0f);
        player.setFireTicks(0);
        Cyan1dex.server.getScheduler().scheduleSyncDelayedTask(Cyan1dex.instance, () -> {
                    player.setHealth(20.0);
                    player.setVelocity(noVelocity);
                    player.setFireTicks(0);
                    player.getActivePotionEffects().forEach(PotionEffect2 -> {
                                player.removePotionEffect(PotionEffect2.getType());
                            }
                    );
                }
                , 10);
    }

    public static String readLongString(String[] args, int startfrom) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = startfrom; i < args.length; ++i) {
            stringBuilder.append(args[i]);
            if (i == args.length - 1) continue;
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    public static void playCustomSound(String name, Player player) {
        switch (name) {
            case "fail":
            case "deny": {
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 2.14748365E9f, 0.5f);
                break;
            }
            case "ok":
            case "success": {
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2.14748365E9f, 1.0f);
            }
        }
    }

    public static void tempHoloCyans(Location location, String displayname, int eco) {
        Cyan1dex.tempHolo.add(Utils.createHolo("§b+" + eco + " монеток", location));
        Cyan1dex.tempHolo.add(Utils.createHolo(displayname, location.clone().add(0.0, -0.25, 0.0)));
    }
}
