package ru.cyanworld.cyanuniverse.commands;/*package ru.cyanworld.cyanuniverse.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.cyanworld.cyanuniverse.CyanUniverse.*;

public class CmdChangelog extends Command {
    public static String changelog;
    YamlConfiguration cfg;
    List<String> updatelist = new ArrayList<>();

    public CmdChangelog() {
        super
                (
                        "changelog",
                        "Чейджлог об обновлении сервера",
                        "/changelog",
                        Arrays.asList("update")
                );
        Bukkit.getCommandMap().register(getName(), this);
        loadchangelog();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(changelog);
            if (sender instanceof Player) {
                Player player = (Player) sender;
                updatenews.set(player.getUniqueId().toString(), version);
            }
        } else {
            String needversionraw = args[0];
            String needversion = needversionraw.replace(".", "_");
            if (needversion.equals("list")) {
                StringBuilder sb = new StringBuilder("\n Список обновлений:\n");
                updatelist.forEach(key -> sb.append(key.replace("_", ".")).append("\n"));
                sender.sendMessage(sb.toString() + "\n ");
            } else if (updatelist.contains(needversion)) {
                sender.sendMessage(getChangelog(cfg.getStringList(needversion), needversionraw));
            } else {
                sender.sendMessage("Чейнджлог для версии \"" + needversionraw + "\" не найден");
            }
        }
        return true;
    }

    private void loadchangelog() {
        File file = new File(server.getWorldContainer(), "/plugins/changelog.yml");
        cfg = YamlConfiguration.loadConfiguration(file);
        updatelist.addAll(cfg.getKeys(false));
        changelog = getChangelog(cfg.getStringList(version.replace(".", "_")), version);
    }

    private String getChangelog(List<String> list, String name) {
        if (list.isEmpty()) return "Ошибка загрузки чейнджлога. Версия не найдена:[" + name + "]";
        StringBuilder sb = new StringBuilder(" \n§b§lОбновление " + name + "\n");
        list.forEach(line -> {
            line = line.replace("+", "§a§l+§r").replace("*", "§6§l*§r").replace("Добавлено", "§a§lДобавлено").replace("Исправлено", "§6§lИсправлено");
            sb.append("§r").append(line).append("\n");
        });
        return sb.toString() + "\n ";
    }
}*/
