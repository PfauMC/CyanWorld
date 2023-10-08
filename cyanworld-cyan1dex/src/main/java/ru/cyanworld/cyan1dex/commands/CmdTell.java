package ru.cyanworld.cyan1dex.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.cyanworld.cyan1dex.Cyan1dex;
import ru.cyanworld.cyan1dex.Utils;
import ru.cyanworld.cyan1dex.utils.ChatUtils;

import java.util.Arrays;


public class CmdTell
        extends Command {
    public CmdTell() {
        super("tell", "Написать в личные сообщения", "/tell <ник> <текст>", Arrays.asList("msg", "w", "m"));
        Cyan1dex.server.getCommandMap().register(this.getName(), this);
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length >= 2) {
            Player to = Cyan1dex.server.getPlayer(args[0]);
            if (to != null) {
                if (to != sender) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 1; i < args.length; ++i) {
                        stringBuilder.append(args[i]);
                        stringBuilder.append(" ");
                    }
                    String msg = Utils.playerIsDonator(sender) ? stringBuilder.toString().replaceAll("&", "§").replaceAll("§§", "&") : stringBuilder.toString();
                    ChatUtils.sendTellMessage(msg, sender, to);
                } else {
                    sender.sendMessage("Не возможно отправить сообщение самому себе!");
                }
            } else {
                sender.sendMessage("Игрок не найден");
            }
        } else {
            sender.sendMessage(this.usageMessage);
        }
        return true;
    }
}

