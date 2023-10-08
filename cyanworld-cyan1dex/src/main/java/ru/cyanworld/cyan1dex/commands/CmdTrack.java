package ru.cyanworld.cyan1dex.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import ru.cyanworld.cyan1dex.Cyan1dex;
import ru.cyanworld.cyan1dex.messages.Msg;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class CmdTrack
        extends Command {
    public static YamlConfiguration trackcfg;

    public CmdTrack() {
        super("track", "Команда для отслеживания входа игрока на сервер", "/track ник", Arrays.asList("traking", "observe", "follow"));
        Cyan1dex.server.getCommandMap().register(this.getName(), this);
        trackcfg = YamlConfiguration.loadConfiguration(new File(Cyan1dex.dataFolder, "track.yml"));
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length == 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                String playeruuid = player.getUniqueId().toString();
                String trackname = args[0].toLowerCase();
                String trackuuid = Cyan1dex.cfguuid.getString(trackname);
                if (trackuuid == null) {
                    sender.sendMessage("Этот игрок никогда не заходил на сервер");
                    return true;
                }
                List tracklist = trackcfg.getStringList(trackuuid);
                if (tracklist.contains(playeruuid)) {
                    tracklist.remove(playeruuid);
                    sender.sendMessage("Вы перестали следить за " + trackname);
                } else {
                    tracklist.add(playeruuid);
                    sender.sendMessage("Вы начали следить за " + trackname);
                }
                trackcfg.set(trackuuid, tracklist);
            } else {
                sender.sendMessage(Msg.consoledeny);
            }
        } else {
            sender.sendMessage(this.usageMessage);
        }
        return true;
    }
}
