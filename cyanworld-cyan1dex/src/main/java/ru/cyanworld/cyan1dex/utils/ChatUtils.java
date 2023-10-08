/*
 * Decompiled with CFR 0_123.
 *
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ComponentBuilder
 *  net.md_5.bungee.api.chat.HoverEvent
 *  net.md_5.bungee.api.chat.HoverEvent$Action
 *  net.md_5.bungee.api.chat.TextComponent
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.PlayerChatEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitScheduler
 *  org.bukkit.scheduler.BukkitTask
 */
package ru.cyanworld.cyan1dex.utils;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import ru.cyanworld.cyan1dex.Config;
import ru.cyanworld.cyan1dex.Cyan1dex;
import ru.cyanworld.cyan1dex.Utils;
import ru.cyanworld.cyan1dex.messages.UrlToMoney;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatUtils {
    public static List<Player> chatCooldown = new ArrayList<Player>();
    public static FileConfiguration swearCfg;
    public static List<String> swearLibraryRus;
    public static List<String> whitelistUrls;

    public ChatUtils() {
        ChatUtils.loadLib();
    }

    public static boolean checkForIp(String message) {
        Pattern ipPattern = Pattern.compile("((?<![0-9])(?:(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[ ]?[.,-:; ][ ]?(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[ ]?[., ][ ]?(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[ ]?[., ][ ]?(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2}))(?![0-9]))");
        Matcher regexMatcher = ipPattern.matcher(message);
        while (regexMatcher.find()) {
            if (regexMatcher.group().length() == 0 || !ipPattern.matcher(message).find()) continue;
            return true;
        }
        return false;
    }

    public static void loadLib() {
        swearCfg = YamlConfiguration.loadConfiguration(new File(Cyan1dex.dataFolder, "swear.yml"));
        swearLibraryRus = swearCfg.getStringList("russian");
        whitelistUrls = Arrays.asList("vk.com/cyanworld", "https://vk.com/cyanworld", "cyanworld.ru", "http://cyanworld.ru");
    }

    public static void chat(PlayerChatEvent event) {
        event.setCancelled(true);
        Player player = event.getPlayer();
        if (chatCooldown.contains(player)) {
            player.sendMessage("Вы слишком быстро пишите в чат!");
            return;
        }
        event.setMessage(Utils.playerIsDonator(player) ? "§r" + event.getMessage().replace("&", "§").replace("§§", "&") : "§7" + event.getMessage());
        String parsedchat = ChatUtils.parseChat(event.getMessage() + " ", false, true);
        String finalmsg = player.getDisplayName() + "§7: " + parsedchat;
        String localmsg = "§a[L] " + player.getDisplayName() + "§7: " + parsedchat;
        if (!Config.perworldchat_enable) {
            if (Config.chattranslator_enable) {

                Cyan1dex.lang.languagemap.forEach((lang, playerList) -> {
                            TextComponent text = new TextComponent(finalmsg);
                            text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Cyan1dex.translator.translate(event.getMessage().replace("\u00a7r", "").replace("\u00a77", ""), lang)).create()));
                            playerList.forEach(players -> players.sendMessage(text)
                            );
                        }
                );
            } else {
                Cyan1dex.server.getOnlinePlayers().forEach(Player2 -> Player2.sendMessage(finalmsg)
                );
            }
            System.out.println("<" + player.getDisplayName() + "> : " + event.getMessage());
        } else {
            World world = player.getWorld();
            world.getPlayers().forEach(Player2 -> {
                        Player2.sendMessage(localmsg);
                    }
            );
            System.out.println("[" + world.getName() + "] <" + player.getDisplayName() + "> : " + event.getMessage());
        }
        chatCooldown.add(player);
        Cyan1dex.server.getScheduler().runTaskLaterAsynchronously(Cyan1dex.instance, () -> {
                    chatCooldown.remove(player);
                }
                , 30);
    }

    public static String parseChat(String msg, boolean color, boolean allowlinks) {
        if (color) {
            msg = msg.replace("&", "§").replace("§§", "&");
        }
        String[] message = msg.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String m : message) {
            StringBuilder word;
            block14:
            {
                word = new StringBuilder();
                if (checkForSwear(m)) {
                    for (int x = 0; x < m.length(); ++x) {
                        word.append("*");
                    }
                }
                if (checkForIp(m)) {
                    if (allowlinks) {
                        try {
                            if (m.startsWith("§")) {
                                word.append(m.subSequence(0, 2)).append(UrlToMoney.getUrl(m.subSequence(2, m.length()).toString()));
                                break block14;
                            }
                            word.append(UrlToMoney.getUrl(m));
                        } catch (IOException e) {
                            System.out.println("Не удалось сократить ссылку");
                            word.append(m);
                        }
                    } else if (whitelistUrls.contains(m)) {
                        word.append(m);
                    } else {
                        word.append("[ссылка]");
                    }
                }
            }
            if (swearLibraryRus.contains(m.toLowerCase())) {
                word.append("[плохое слово]");
            } else {
                word.append(m);
            }
            sb.append(word).append(" ");
        }
        return sb.toString();
    }

    public static void sendTellMessage(String message, CommandSender from, Player to) {
        String fromnick;
        if (from instanceof Player) {
            Player fromplayer = (Player) from;
            fromnick = fromplayer.getDisplayName();
        } else {
            fromnick = "§7Консоль";
        }
        String formatMessage = fromnick + "§8 » " + to.getDisplayName() + "§8:§f " + message;
        from.sendMessage(formatMessage);
        to.sendMessage(formatMessage);
        to.sendTitle(fromnick, Cyan1dex.lang.translate(to, message), 5, 140, 5);
        to.playSound(to.getLocation(), Sound.BLOCK_NOTE_PLING, 2.14748365E9f, 1.0f);
    }

    public static boolean checkForSwear(String message) {
        return swearLibraryRus.contains(message.toLowerCase());
    }

    public static void broadcastTitle(String title, String subtitle, int rise, int stay, int fade) {
        ChatUtils.broadcastTitle(new ArrayList<Player>(Cyan1dex.server.getOnlinePlayers()), title, subtitle, rise, stay, fade);
    }

    public static void broadcastTitle(List<Player> list, String title, String subtitle, int rise, int stay, int fade) {
        list.forEach(player -> {
                    player.sendTitle(title, subtitle, rise, stay, fade);
                }
        );
    }

    public boolean checkForWeb(String message) {
        Pattern webpattern = Pattern.compile("[-a-zA-Z0-9@:%_\\+.~#?&//=]{2,256}\\.[a-z]{2,4}\\b(\\/[-a-zA-Z0-9@:%_\\+~#?&//=]*)?");
        Matcher regexMatcherurl = webpattern.matcher(message);
        while (regexMatcherurl.find()) {
            String text = regexMatcherurl.group().trim().replaceAll("www.", "").replaceAll("http://", "").replaceAll("https://", "");
            if (regexMatcherurl.group().length() == 0 || text.length() == 0 || !webpattern.matcher(message).find())
                continue;
            return true;
        }
        return false;
    }

    public boolean checkForAd(String message) {
        if (checkForIp(message = message.replace(" ", ""))) {
            return true;
        }
        return checkForWeb(message);
    }
}
