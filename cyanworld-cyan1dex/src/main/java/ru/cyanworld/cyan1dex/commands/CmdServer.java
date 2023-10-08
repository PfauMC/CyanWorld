package ru.cyanworld.cyan1dex.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.cyanworld.cyan1dex.BungeeBridge;
import ru.cyanworld.cyan1dex.Cyan1dex;
import ru.cyanworld.cyan1dex.menus.MenusList;
import ru.cyanworld.cyan1dex.messages.Msg;

import java.util.ArrayList;

public class CmdServer extends Command {
    public CmdServer() {
        super("server", "Команда для перемещения между серверами", "/server", new ArrayList());
        Cyan1dex.server.getCommandMap().register(this.getName(), this);
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            switch (args.length) {
                case 0: {
                    player.openInventory(MenusList.serverMenu.getInventory());
                    break;
                }
                case 1: {
                    BungeeBridge.connectPlayer(player, args[0]);
                    break;
                }
                default: {
                    sender.sendMessage(this.usageMessage);
                    break;
                }
            }
        } else {
            sender.sendMessage(Msg.consoledeny);
        }
        return true;
    }
}
