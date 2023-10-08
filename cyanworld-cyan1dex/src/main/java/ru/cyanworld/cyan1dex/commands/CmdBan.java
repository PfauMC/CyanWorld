/*
 * Decompiled with CFR 0_123.
 *
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandMap
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package ru.cyanworld.cyan1dex.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.cyanworld.cyan1dex.Cyan1dex;
import ru.cyanworld.cyan1dex.messages.Msg;
import ru.cyanworld.cyan1dex.utils.BanManager;
import ru.cyanworld.cyan1dex.utils.ChatUtils;

import java.util.ArrayList;

public class CmdBan extends Command {
    public CmdBan() {
        super("ban", "\u041a\u043e\u043c\u0430\u043d\u0434\u0430 \u0434\u043b\u044f \u043a\u0438\u043a\u0430 \u0438\u0433\u0440\u043e\u043a\u0430", "/ban \u043d\u0438\u043a \u043f\u0440\u0438\u0447\u0438\u043d\u0430...", new ArrayList());
        Cyan1dex.server.getCommandMap().register(this.getName(), this);
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(Msg.permdeny);
            return true;
        }
        switch (args.length) {
            case 0: {
                sender.sendMessage(this.usageMessage);
                break;
            }
            case 1: {
                String kickedby = sender instanceof Player ? ((Player) sender).getDisplayName() : "\u00a7f\u0421\u0435\u0440\u0432\u0435\u0440";
                String result = BanManager.ban(Cyan1dex.server.getPlayer(args[0]).getName(), "(\u043f\u0440\u0438\u0447\u0438\u043d\u0430 \u043d\u0435 \u0443\u043a\u0430\u0437\u0430\u043d\u0430)", kickedby);
                if (result == null) break;
                sender.sendMessage(result);
                break;
            }
            default: {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 1; i < args.length; ++i) {
                    stringBuilder.append(args[i].replace("&", "\u00a7").replace("\u00a7\u00a7", "&"));
                    stringBuilder.append(" ");
                }
                String kickedby = sender instanceof Player ? ((Player) sender).getDisplayName() : "\u00a7f\u0421\u0435\u0440\u0432\u0435\u0440";
                String result = BanManager.ban(args[0], ChatUtils.parseChat(stringBuilder.toString(), true, false), kickedby);
                if (result == null) break;
                sender.sendMessage(result);
                break;
            }
        }
        return true;
    }
}
