package ru.cyanworld.cyanuniverse.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.cyanworld.cyan1dex.Utils;
import ru.cyanworld.cyan1dex.messages.Msg;
import ru.cyanworld.cyanuniverse.WorldManager;

import java.util.ArrayList;

import static ru.cyanworld.cyanuniverse.CyanUniverse.server;

public class CmdCode extends Command {
    public CmdCode() {
        super
                (
                        "code",
                        "Выбор режима мира",
                        "/code <ник>",
                        new ArrayList<>()
                );
        Bukkit.getCommandMap().register(getName(), this);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length == 1) {
            if (sender instanceof Player) {
                Player playersender = (Player) sender;
                if (!playersender.getWorld().getName().startsWith(playersender.getUniqueId().toString())) {
                    playersender.sendMessage("Эта команда работает только в своём мире");
                    return true;
                }
                if (!WorldManager.getWorldType(playersender.getWorld()).equals("coding")) {
                    sender.sendMessage("Эта команда работает только в режиме кодинга");
                    return true;
                }
                Player player = server.getPlayer(args[0]);
                if (player == null) {
                    sender.sendMessage("Игрок не онлайн");
                    return true;
                } else if (player.getWorld() != playersender.getWorld()) {
                    sender.sendMessage("Игрок не в вашем мире");
                    return true;
                }
                player.setGameMode(GameMode.ADVENTURE);
                Utils.healthUpPlayer(player);
                player.setAllowFlight(true);
                player.teleport(playersender);
                sender.sendMessage("Выданы права для кодинга игроку " + player.getDisplayName());
            } else {
                sender.sendMessage(Msg.consoledeny);
            }
        } else sender.sendMessage(usageMessage);
        return true;
    }
}
