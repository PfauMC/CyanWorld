package ru.cyanworld.cyan1dex.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.cyanworld.cyan1dex.Cyan1dex;
import ru.cyanworld.cyan1dex.messages.Msg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CmdLang extends Command {
    public CmdLang() {
        super("lang", "Команда для подключения в хаб", "/lang <language>", Collections.singletonList("language"));
        Cyan1dex.server.getCommandMap().register(this.getName(), this);
    }

    public static void setLang(Player player, String lang) {
        String uuid = player.getUniqueId().toString();
        String currentlang = CmdLang.getLang(player);
        List<Player> list = Cyan1dex.lang.languagemap.get(currentlang);
        if (list == null) {
            list = new ArrayList<Player>();
            Cyan1dex.lang.languagemap.put(currentlang, list);
        } else list.remove(player);
        List<Player> newlist = Cyan1dex.lang.languagemap.get(lang);
        if (newlist == null) {
            newlist = new ArrayList<Player>();
            newlist.add(player);
            Cyan1dex.lang.languagemap.put(lang, newlist);
        } else {
            newlist.add(player);
        }
        Cyan1dex.cfgplayers.set(uuid + ".lang", lang);
    }

    public static String getLang(Player player) {
        String uuid = player.getUniqueId().toString();
        String currentlang = Cyan1dex.cfgplayers.getString(uuid + ".lang");
        if (currentlang == null) {
            currentlang = "ru";
            Cyan1dex.cfgplayers.set(uuid + ".lang", "ru");
        }
        return currentlang;
    }

    public static void removeLang(Player player, String lang) {
        String uuid = player.getUniqueId().toString();
        List<Player> players = Cyan1dex.lang.languagemap.get(lang);
        if (players != null) {
            players.remove(player);
        }
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0 || args.length > 1) {
                player.sendMessage(this.usageMessage);
                return true;
            }
            CmdLang.setLang(player, args[0]);
            player.sendMessage(Cyan1dex.translator.translate("Установлен язык: " + args[0], args[0]));
        } else {
            sender.sendMessage(Msg.consoledeny);
        }
        return true;
    }
}
