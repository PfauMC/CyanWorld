package ru.cyanworld.cyanuniverse.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.cyanworld.cyan1dex.messages.Msg;
import ru.cyanworld.cyanuniverse.CyanUniverse;
import ru.cyanworld.cyanuniverse.WorldManager;

import java.util.ArrayList;

import static ru.cyanworld.cyanuniverse.CyanUniverse.worldscfg;

public class ModeCommand extends Command {
    public ModeCommand() {
        super
                (
                        "mode",
                        "Выбор режима мира",
                        "/mode <режим_мира>",
                        new ArrayList<>()
                );
        Bukkit.getCommandMap().register(getName(), this);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length == 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                World world = player.getWorld();
                if (world == CyanUniverse.lobby || !world.getName().startsWith(player.getUniqueId().toString())) {
                    player.sendMessage("Выбор типа мира можно сделать только в своём мире");
                    return true;
                } else {
                    switch (WorldManager.getWorldType(world)) {
                        case "building":
                        case "coding": {
                            world.save();
                            break;
                        }
                        case "playing": {
                            //CodeEventHandler codeEventHandler = Coding.codeMap.get(world);
                            //if (codeEventHandler == null) codeEventHandler = new CodeEventHandler(world);
                            //CodeEventHandler finalCodeEventHandler = codeEventHandler;
                            //System.out.println(finalCodeEventHandler.getSchedulersList());
                            //finalCodeEventHandler.getSchedulersList().forEach(BukkitRunnable::cancel);
                            //world.getPlayers().forEach(players -> finalCodeEventHandler.runCode("СобытиеИгрока_Выход", UUID.randomUUID(), players, null));
                            break;
                        }
                    }
                    String mode = args[0];
                    String worldname = player.getWorld().getName();
                    switch (mode) {
                        case "code":
                        case "coding": {
                            worldscfg.set(worldname + ".type", "coding");
                            world.getPlayers().forEach(players -> WorldManager.teleportToWorld(players, worldname));

                            break;
                        }
                        case "build":
                        case "building": {
                            worldscfg.set(worldname + ".type", "building");
                            world.getPlayers().forEach(players -> WorldManager.teleportToWorld(players, worldname));
                            break;
                        }
                        case "play":
                        case "playing": {
                            worldscfg.set(worldname + ".type", "playing");
                            world.getPlayers().forEach(players -> WorldManager.teleportToWorld(players, worldname));
                            break;
                        }
                        default: {
                            sender.sendMessage("Доступные режимы: §b/mode code§f, §b/mode play§f, §b/mode build");
                        }
                    }
                }
            } else {
                sender.sendMessage(Msg.consoledeny);
            }
        } else sender.sendMessage(usageMessage);
        return true;
    }
}
