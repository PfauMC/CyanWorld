package ru.cyanworld.cyan1dex.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.cyanworld.cyan1dex.Cyan1dex;
import ru.cyanworld.cyan1dex.messages.Msg;

import java.util.Collections;

public class CmdG
        extends Command {
    public CmdG() {
        super("g", "Команда для написания в глобальный чат.", "Написать в глобальный чат\n/g <сообщение>", Collections.singletonList("global"));
        Cyan1dex.server.getCommandMap().register(this.getName(), this);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player playersender = (Player) sender;
            playersender.sendMessage("§7[§bCyan§3World§7] §fФункция временно отключена §4:(");
            return true;
        } else {
            sender.sendMessage(Msg.consoledeny);
        }
        return true;
    }
}

//public boolean execute(CommandSender sender, String commandLabel, String[] args) {
//        if (args.length >= 1) {
//            StringBuilder stringBuilder = new StringBuilder();
//            for (String arg : args) {
//                stringBuilder.append(arg);
//                stringBuilder.append(" ");
//            }
//            String displayname = sender.getName();
//            if (sender instanceof Player) {
//                displayname = ((Player) sender).getDisplayName();
//            }
//            String parsedchat = ChatUtils.parseChat(stringBuilder.toString(), true, false);
//            Cyan1dex.server.broadcastMessage("§c[G] " + displayname + "§8:§r " + parsedchat);
//            System.out.println("[G] <" + displayname + "> : " + parsedchat);
//        } else {
//            sender.sendMessage(this.usageMessage);
//        }
//    return true;
//}
