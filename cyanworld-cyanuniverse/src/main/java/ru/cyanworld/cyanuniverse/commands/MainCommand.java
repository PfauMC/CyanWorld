package ru.cyanworld.cyanuniverse.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import ru.cyanworld.cyan1dex.messages.Msg;
import ru.cyanworld.cyanuniverse.CyanUniverse;
import ru.cyanworld.cyanuniverse.MenusList;
import ru.cyanworld.cyanuniverse.WorldManager;
import ru.cyanworld.cyanuniverse.menus.decorations.DecoHeads;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static ru.cyanworld.cyanuniverse.CyanUniverse.server;
import static ru.cyanworld.cyanuniverse.CyanUniverse.worldscfg;

public class MainCommand extends Command {
    CyanUniverse main;
    PluginDescriptionFile des;

    public MainCommand(CyanUniverse main) {
        super
                (
                        "cyanuniverse",
                        "Выход в лобби",
                        "/cyanuniverse",
                        Arrays.asList("cyanuniverse")
                );
        this.main = main;
        this.des = main.getDescription();
        server.getCommandMap().register(getName(), this);
    }

    public static String getTexture(ItemStack itemStack, boolean encoded) {
        if (itemStack.getType() != Material.SKULL_ITEM) return "null";
        net.minecraft.server.v1_12_R1.ItemStack stack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound itemTags = (stack.hasTag() ? stack.getTag() : new NBTTagCompound());
        NBTTagList list = (NBTTagList) itemTags.getCompound("SkullOwner").getCompound("Properties").get("textures");
        if (list == null) return "null";
        String texture = list.getString(0).replace("{Value:\"", "").replace("\"}", "");
        if (encoded) return texture;
        return new String(Base64.decodeBase64(texture)).replace("{\"textures\":{\"SKIN\":{\"url\":\"", "").replace("\"}}}", "");
    }

    public static String readLongString(String[] args, int startfrom) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = startfrom; i < args.length; i++) {
            stringBuilder.append(args[i]);
            if (i != args.length - 1) stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length == 0) {
            if (!sender.isOp())
                sender.sendMessage("§b" + des.getName() + " v" + des.getVersion() + " by " + des.getAuthors());
            else sender.sendMessage("1");
            return true;
        }
        if (!sender.isOp()) return true;
        switch (args[0]) {
            case "importold": {
                try {
                    for (File folder : server.getWorldContainer().listFiles()) {
                        if (!folder.isDirectory()) continue;
                        if (folder.getName().equals("crash-reports")) continue;
                        if (!folder.getName().contains("-")) continue;
                        if (folder.getName().contains("#")) continue;
                        String name = folder.getName();
                        Bukkit.broadcastMessage("Импортирую [" + name + "]...");
                        folder.renameTo(new File(server.getWorldContainer(), name + "#0"));
                        CyanUniverse.worldscfg.set(name + "#0.type", "building");
                    }
                } catch (Exception ex) {
                    sender.sendMessage("Не удалось импортировать");
                    ex.printStackTrace();
                } finally {
                    sender.sendMessage("Импортированно!");
                }
                break;
            }
            case "purge": {
                WorldManager.purgeOldWorlds(Integer.parseInt(args[1]), args[2].equals("true"));
                break;
            }
            case "tpworld": {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    String worldname = args[1];
                    if (worldname.equals("world")) WorldManager.teleportToLobby(player);
                    else WorldManager.teleportToWorld(player, worldname);
                } else sender.sendMessage(Msg.consoledeny);
                break;
            }
            case "removeallvotes": {
                worldscfg.getKeys(false).forEach(String -> worldscfg.set(String + ".votes", null));
                CyanUniverse.saveCfgs();
                sender.sendMessage("Голоса очищенны");
                break;
            }
            case "heads": {
                switch (args[1]) {
                    case "reload": {
                        DecoHeads.headbase = YamlConfiguration.loadConfiguration(new File(CyanUniverse.pluginFolder, "heads.yml"));
                        MenusList.decoHeads.setupItems();
                        sender.sendMessage("База голов перезагруженна");
                        break;
                    }
                    case "gettexture": {
                        Player player = (Player) sender;
                        ItemStack itemStack = player.getInventory().getItemInMainHand();
                        switch (args[2]) {
                            case "url": {
                                player.sendMessage(getTexture(itemStack, false));
                                break;
                            }
                            case "encoded": {
                                String encoded = getTexture(itemStack, true);
                                TextComponent text = new TextComponent(encoded);
                                text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, encoded));
                                player.sendMessage(text);
                                break;
                            }
                            default:
                                player.sendMessage("url/encoded");
                        }
                        break;
                    }
                    case "add": {
                        ItemStack item = null;
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            item = player.getInventory().getItemInMainHand();
                        }
                        int price = Integer.parseInt(args[2]);
                        String category = args[3].split("\\.")[0];
                        String id = args[3].split("\\.")[1];
                        String name = readLongString(args, 4);
                        String texture = item != null ? getTexture(item, false) : "null";
                        server.getOnlinePlayers().forEach(Player -> {
                            Player.sendMessage(
                                    "   \n"
                                            + "§aНовая голова в декорациях!\n"
                                            + "§aКатегория: §f" + category + "\n"
                                            + "§aНазвание: §f" + name + "\n"
                                            + "§aЦена: §f" + price + " цианов\n"
                                            + "   "
                            );
                            Player.playSound(Player.getLocation(), Sound.ENTITY_CAT_PURREOW, Integer.MAX_VALUE, 2);
                        });

                        YamlConfiguration cfg = DecoHeads.headcategorymap.get(category);
                        cfg.set(id + ".name", name);
                        cfg.set(id + ".texture", texture);
                        cfg.set(id + ".price", price);
                        try {
                            cfg.save(new File(CyanUniverse.pluginFolder, "heads/" + category + ".yml"));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        DecoHeads.headbase = YamlConfiguration.loadConfiguration(new File(CyanUniverse.pluginFolder, "heads.yml"));
                        MenusList.decoHeads.setupItems();
                        break;
                    }
                    default:
                        sender.sendMessage("reload/add/gettexture");
                }
            }
        }
        return true;
    }
}
