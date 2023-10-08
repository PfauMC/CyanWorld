package ru.cyanworld.cyan1dex.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.cyanworld.cyan1dex.Cyan1dex;
import ru.cyanworld.cyan1dex.Utils;
import ru.cyanworld.cyan1dex.messages.Msg;
import ru.cyanworld.cyan1dex.utils.BanManager;
import ru.cyanworld.cyan1dex.utils.ChatUtils;

import java.util.ArrayList;

public class CmdKick extends Command {
    public CmdKick() {
        super("kick", "Команда для кика игрока", "/kick ник причина...", new ArrayList());
        Cyan1dex.server.getCommandMap().register(this.getName(), this);
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!Utils.playerIsModer(sender)) {
            sender.sendMessage(Msg.permdeny);
            return true;
        }
        switch (args.length) {
            case 0: {
                sender.sendMessage(this.usageMessage);
                break;
            }
            case 1: {
                String kickedby = sender instanceof Player ? ((Player) sender).getDisplayName() : "§fСервер";
                String result = BanManager.kick(Cyan1dex.server.getPlayer(args[0]), "(причина не указана)", kickedby);
                if (result == null) break;
                sender.sendMessage(result);
                break;
            }
            default: {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 1; i < args.length; ++i) {
                    stringBuilder.append(args[i].replace("&", "§").replace("§§", "&"));
                    stringBuilder.append(" ");
                }
                String kickedby = sender instanceof Player ? ((Player) sender).getDisplayName() : "§fСервер";
                String result = BanManager.kick(Cyan1dex.server.getPlayer(args[0]), ChatUtils.parseChat(stringBuilder.toString(), true, false), kickedby);
                if (result == null) break;
                sender.sendMessage(result);
                break;
            }
        }
        return true;
    }
}
