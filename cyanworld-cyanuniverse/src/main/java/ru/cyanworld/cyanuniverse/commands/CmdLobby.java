package ru.cyanworld.cyanuniverse.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import ru.cyanworld.cyanuniverse.CyanUniverse;
import ru.cyanworld.cyanuniverse.WorldManager;

import java.util.Arrays;

public class CmdLobby extends Command {
    CyanUniverse main;
    PluginDescriptionFile des;

    public CmdLobby(CyanUniverse main) {
        super
                (
                        "lobby",
                        "Выход в лобби",
                        "/lobby",
                        Arrays.asList("leave", "hub")
                );
        this.main = main;
        this.des = main.getDescription();
        Bukkit.getCommandMap().register(getName(), this);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            WorldManager.teleportToLobby(player);
        } else {
            sender.sendMessage("Эту команду нельзя выполнить в консоле");
        }
        return true;
    }
}
