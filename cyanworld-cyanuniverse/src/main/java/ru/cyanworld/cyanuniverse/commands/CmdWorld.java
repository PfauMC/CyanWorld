package ru.cyanworld.cyanuniverse.commands;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.cyanworld.cyan1dex.Utils;
import ru.cyanworld.cyan1dex.messages.Msg;
import ru.cyanworld.cyanuniverse.WorldManager;

import java.util.ArrayList;

import static ru.cyanworld.cyanuniverse.CyanUniverse.worldscfg;

public class CmdWorld extends Command {
    public CmdWorld() {
        super
                (
                        "world",
                        "Настройки мира",
                        "§b/world name ИМЯ §f- Поставить имя мира"/* +
                                "\n§b/world icon §f- Поставить иконку"*/,
                        new ArrayList<>()
                );
        Bukkit.getCommandMap().register(getName(), this);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length >= 2) {
            if (sender instanceof Player) {
                Player playersender = (Player) sender;
                if (!playersender.getWorld().getName().startsWith(playersender.getUniqueId().toString())) {
                    playersender.sendMessage("Эта команда работает только в своём мире");
                    return true;
                }
                switch (args[0]) {
                    case "name": {
                        if (!Utils.playerIsDonator(playersender) && worldscfg.getInt(playersender.getWorld().getName() + ".votecount") < 25) {
                            playersender.sendMessage("У вашего мира должно быть минимум 25 голосов");
                            Utils.playCustomSound("deny", playersender);
                            return true;
                        }
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            stringBuilder.append(args[i]);
                            stringBuilder.append(" ");
                        }
                        String name = Utils.playerIsDonator(playersender) ? stringBuilder.toString().replace("&", "§").replace("§§", "&") : stringBuilder.toString();
                        if (name.length() > 40) {
                            playersender.sendMessage("Название слишком длинное!");
                            Utils.playCustomSound("deny", playersender);
                            return true;
                        }
                        worldscfg.set(playersender.getWorld().getName() + ".name", name);
                        playersender.sendMessage("Установленно название мира: \n" + name);
                        Utils.playCustomSound("ok", playersender);

                        TextComponent header = new TextComponent("§bCyan§3World");
                        TextComponent footer = new TextComponent("Мир");
                        switch (WorldManager.getWorldType(playersender.getWorld())) {
                            case "building": {
                                footer = new TextComponent("Строительство: " + name + "\n§rАвтор: " + playersender.getDisplayName());
                                break;
                            }
                            case "coding": {
                                footer = new TextComponent("Кодинг: " + name + "\n§rАвтор: " + playersender.getDisplayName());
                                break;
                            }
                            case "playing": {
                                footer = new TextComponent(name + "\n§rАвтор: " + playersender.getDisplayName());
                                break;
                            }
                        }
                        TextComponent finalFooter = footer;
                        playersender.getWorld().getPlayers().forEach(player -> player.setPlayerListHeaderFooter(header, finalFooter));
                        break;
                    }
                    case "logo":
                    case "icon": {
                        ItemStack itemStack = playersender.getInventory().getItemInMainHand();
                        if (itemStack == null || itemStack.getType() == Material.AIR) {
                            playersender.sendMessage("Положите предмет в руку для установки иконки");
                            return true;
                        }
                        String name = itemStack.getType().name();
                        worldscfg.set(playersender.getWorld().getName() + ".icon", name);
                        break;
                    }
                    default:
                        playersender.sendMessage(usageMessage);
                }
            } else {
                sender.sendMessage(Msg.consoledeny);
            }
        } else sender.sendMessage(usageMessage);
        return true;
    }
}
