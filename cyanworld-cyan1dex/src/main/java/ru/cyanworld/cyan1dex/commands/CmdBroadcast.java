package ru.cyanworld.cyan1dex.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.cyanworld.cyan1dex.Cyan1dex;
import ru.cyanworld.cyan1dex.messages.Msg;

import java.util.Collections;

public class CmdBroadcast extends Command {
    public CmdBroadcast() {
        super("broadcast", "Команда для оповещения в чат.", "/broadcast <сообщение>", Collections.singletonList("bc"));
        Cyan1dex.server.getCommandMap().register(this.getName(), this);
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender.isOp()) {
            if (args.length >= 1) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < args.length; ++i) {
                    stringBuilder.append(args[i]);
                    stringBuilder.append(" ");
                }
                Cyan1dex.server.broadcastMessage("§8»§r " + stringBuilder.toString().replaceAll("&", "§").replaceAll("§§", "&"));
            } else {
                sender.sendMessage(this.usageMessage);
            }
        } else {
            sender.sendMessage(Msg.permdeny);
        }
        return true;
    }
}
