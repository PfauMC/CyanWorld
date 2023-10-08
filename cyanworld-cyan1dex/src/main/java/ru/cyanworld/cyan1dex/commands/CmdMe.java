package ru.cyanworld.cyan1dex.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.cyanworld.cyan1dex.Cyan1dex;
import ru.cyanworld.cyan1dex.Utils;
import ru.cyanworld.cyan1dex.messages.Msg;
import ru.cyanworld.cyan1dex.utils.ChatUtils;

import java.util.Collections;

public class CmdMe
        extends Command {
    public CmdMe() {
        super("me", "Команда для оповещения в чат.", "/me <сообщение>", Collections.singletonList("shout"));
        Cyan1dex.server.getCommandMap().register(this.getName(), this);
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (Utils.playerIsDonator(sender)) {
            if (args.length >= 1) {
                StringBuilder stringBuilder = new StringBuilder();
                for (String arg : args) {
                    stringBuilder.append(arg);
                    stringBuilder.append(" ");
                }
                String displayname = sender.getName();
                if (sender instanceof Player) {
                    displayname = ((Player) sender).getDisplayName();
                }
                String parsedchat = ChatUtils.parseChat(stringBuilder.toString(), true, false);
                Cyan1dex.server.broadcastMessage("★ " + displayname + " §8»§r " + parsedchat);
            } else {
                sender.sendMessage(this.usageMessage);
            }
        } else {
            sender.sendMessage(Msg.permdeny);
        }
        return true;
    }
}
