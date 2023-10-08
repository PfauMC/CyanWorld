package ru.cyanworld.cyan1dex.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.cyanworld.cyan1dex.BungeeBridge;
import ru.cyanworld.cyan1dex.Cyan1dex;
import ru.cyanworld.cyan1dex.messages.Msg;

import java.util.Collections;

public class CmdHub extends Command {
    public CmdHub() {
        super("hub", "Команда для подключения в хаб", "/hub", Collections.singletonList("lobby"));
        Cyan1dex.server.getCommandMap().register(this.getName(), this);
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            BungeeBridge.connectPlayer((Player) sender, "main");
        } else {
            sender.sendMessage(Msg.consoledeny);
        }
        return true;
    }
}
