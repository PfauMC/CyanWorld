package ru.cyanworld.cyan1dex.commands;

import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.cyanworld.cyan1dex.Cyan1dex;
import ru.cyanworld.cyan1dex.Utils;
import ru.cyanworld.cyan1dex.messages.Msg;

import java.util.ArrayList;

public class CmdHome extends Command {
    public CmdHome() {
        super("home", "Телепортация на точку дома", "/home", new ArrayList());
        Cyan1dex.server.getCommandMap().register(this.getName(), this);
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Location location = player.getBedSpawnLocation();
            if (location != null) {
                if (player.getWorld() == location.getWorld()) {
                    Utils.teleportPlayer(player, location, true);
                } else {
                    player.sendMessage(Msg.teleportdeny_otherworld);
                }
            } else {
                player.sendMessage(new TranslatableComponent("tile.bed.notValid"));
            }
        } else {
            sender.sendMessage(Msg.consoledeny);
        }
        return true;
    }
}
