package ru.cyanworld.cyan1dex.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.cyanworld.cyan1dex.Cyan1dex;
import ru.cyanworld.cyan1dex.messages.Msg;

import java.util.ArrayList;

public class CmdSethome extends Command {
    public CmdSethome() {
        super("sethome", "Телепортация на точку дома", "/sethome", new ArrayList());
        Cyan1dex.server.getCommandMap().register(this.getName(), this);
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(Msg.sethome_needsleep);
        } else {
            sender.sendMessage(Msg.consoledeny);
        }
        return true;
    }
}
