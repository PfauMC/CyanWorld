package ru.cyanworld.cyanuniverse.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.cyanworld.cyan1dex.api.ItemBuilder;
import ru.cyanworld.cyanuniverse.WorldManager;
import ru.cyanworld.cyanuniverse.menus.nbt.item.NbtItemMenu;

import java.util.*;

public class CmdNbt extends Command {
    public static List<String> blacklist = Arrays.asList("ru.cyanworld.cyanuniverse.coding.number", "ru.cyanworld.cyanuniverse.coding.effect", "ru.cyanworld.cyanuniverse.coding.location");
    public static Set<Player> selectmob = new HashSet<>();
    public static Map<Player, Entity> selectedmob = new HashMap<>();

    public CmdNbt() {
        super
                (
                        "nbt",
                        "Редактор NBT",
                        "/nbt",
                        new ArrayList<>()
                );
        Bukkit.getCommandMap().register(getName(), this);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            switch (WorldManager.getWorldType(player.getWorld())) {
                case "coding": {
                    if (player.getGameMode() != GameMode.ADVENTURE) {
                        sender.sendMessage("Вы не можете редактировать NBT без прав кодинга");
                        return true;
                    }
                    break;
                }
                case "building": {
                    if (player.getGameMode() != GameMode.CREATIVE) {
                        sender.sendMessage("Вы не можете редактировать NBT без прав строительства");
                        return true;
                    }
                    break;
                }
                default: {
                    sender.sendMessage("Редактор работает только в режиме кодинга или строительства");
                    return true;
                }
            }
            if (args.length < 1) {
                sender.sendMessage("/nbt item");
                return true;
            }
            switch (args[0]) {
                case "item": {
                    ItemStack item = player.getInventory().getItemInMainHand();
                    if (item == null || item.getType() == Material.AIR) {
                        sender.sendMessage("Возьмите предмет в руки и попробуйте снова");
                        return true;
                    }
                    ItemBuilder itemBuilder = new ItemBuilder(item);
                    String locname = itemBuilder.itemMeta.getLocalizedName();
                    if (locname != null && blacklist.contains(locname)) {
                        sender.sendMessage("NBT у этого предмета запрещено редактировать");
                        return true;
                    }
                    player.openInventory(new NbtItemMenu(itemBuilder).getInventory());
                    break;
                }
                case "mob": {
                    selectmob.add(player);
                    sender.sendMessage("Кликните по мобу для того чтобы открыть редактор моба");
                    break;
                }
                default: {
                    sender.sendMessage("/nbt item");
                }
            }
        } else sender.sendMessage(usageMessage);
        return true;
    }
}
