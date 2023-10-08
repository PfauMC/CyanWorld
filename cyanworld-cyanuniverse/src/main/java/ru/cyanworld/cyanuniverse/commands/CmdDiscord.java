package ru.cyanworld.cyanuniverse.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.cyanworld.cyan1dex.messages.Msg;

import java.util.ArrayList;

public class CmdDiscord extends Command {
    public CmdDiscord() {
        super
                (
                        "discord",
                        "Выдаёт ссылку на дс, а хуле нет?",
                        "/discord",
                        new ArrayList<>()
                );
        Bukkit.getCommandMap().register(getName(), this);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player playersender = (Player) sender;
            playersender.sendMessage("§7[§bCyan§3World§7] §fМы будем рады видеть вас в нашем Discord-канале: \n§6http://discord.gg/uWg4gAE");
            return true;
        } else {
            sender.sendMessage(Msg.consoledeny);
        }
        return true;
    }
}
