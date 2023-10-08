package ru.cyanworld.cyan1dex.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.cyanworld.cyan1dex.Cyan1dex;
import ru.cyanworld.cyan1dex.Utils;
import ru.cyanworld.cyan1dex.messages.Msg;

import java.util.ArrayList;

public class CmdSpawn
        extends Command {
    public CmdSpawn() {
        super("spawn", "Команда для телепортации на спаун", "/spawn", new ArrayList());
        Cyan1dex.server.getCommandMap().register(this.getName(), this);
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.getWorld().getName().equals("world")) {
                Utils.teleportPlayer(player, player.getWorld().getSpawnLocation(), true);
            } else {
                player.sendMessage(Msg.teleportdeny_otherworld);
            }
        } else {
            sender.sendMessage(Msg.consoledeny);
        }
        return true;
    }
}
