package ru.cyanworld.cyanuniverse.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.cyanworld.cyan1dex.messages.Msg;

import java.util.ArrayList;

public class CmdRules extends Command {
    public CmdRules() {
        super
                (
                        "rules",
                        "Правила шоле?",
                        "/rules",
                        new ArrayList<>()
                );
        Bukkit.getCommandMap().register(getName(), this);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player playersender = (Player) sender;
            playersender.sendMessage("§7[§bCyan§3World§7] §fКароч тут такая проблема, вообщем правила ещё пишутся, обожди)0))");
            return true;
        } else {
            sender.sendMessage(Msg.consoledeny);
        }
        return true;
    }
}
