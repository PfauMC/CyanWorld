package ru.cyanworld.cyanuniverse.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.cyanworld.cyan1dex.messages.Msg;
import ru.cyanworld.cyanuniverse.WorldManager;

import java.util.ArrayList;

public class CmdFixcode extends Command {
    public CmdFixcode() {
        super
                (
                        "fixcode",
                        "Выбор режима мира",
                        "/fixcode x y z yaw pitch",
                        new ArrayList<>()
                );
        Bukkit.getCommandMap().register(getName(), this);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length == 5) {
            if (sender instanceof Player) {
                Player playersender = (Player) sender;
                if (!playersender.getWorld().getName().startsWith(playersender.getUniqueId().toString())) {
                    playersender.sendMessage("Эта команда работает только в своём мире");
                    return true;
                }
                Location location = new Location(playersender.getWorld(), Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Float.parseFloat(args[3]), Float.parseFloat(args[4]));
                switch (WorldManager.getWorldType(playersender.getWorld())) {
                    case "coding": {
                        playersender.teleport(location);
                        break;
                    }
                    case "playing": {
                        playersender.chat("/mode code");
                        playersender.teleport(location);
                        break;
                    }
                    default: {
                        sender.sendMessage("Эта команда работает только в режиме кодинга");
                        return true;
                    }
                }
            } else {
                sender.sendMessage(Msg.consoledeny);
            }
        } else sender.sendMessage(usageMessage);
        return true;
    }
}
