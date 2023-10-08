package ru.cyanworld.cyan1dex.commands;

import com.google.common.collect.Maps;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.cyanworld.cyan1dex.Config;
import ru.cyanworld.cyan1dex.Cyan1dex;
import ru.cyanworld.cyan1dex.CyanEcoManager;
import ru.cyanworld.cyan1dex.Utils;
import ru.cyanworld.cyan1dex.messages.Msg;
import ru.cyanworld.cyan1dex.rcon.RconManager;
import ru.cyanworld.cyan1dex.utils.ChatUtils;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CmdCyan1dex extends Command {
    public static Map<Integer, String> waitingCommand = Maps.newConcurrentMap();

    public CmdCyan1dex() {
        super("cyan1dex", "Основная команда Cyan1dex", "/cyan1dex", Collections.singletonList("cyan"));
        Cyan1dex.server.getCommandMap().register(this.getName(), this);
        Cyan1dex.server.getScheduler().runTaskTimer(Cyan1dex.instance, () -> {
                    if (waitingCommand.isEmpty()) {
                        return;
                    }
                    waitingCommand.forEach((port, command) -> {
                                boolean result = this.rconRequest(port, command);
                                if (result) {
                                    waitingCommand.remove(port);
                                }
                            }
                    );
                }
                , 0, 200);
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage("Эта команда только для администраторов");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("/cyan1dex setgroup <ник> <группа>\n/cyan1dex cmdall Команда...\n/cyan1dex fakeerror\n/cyan1dex viewdistance <ник> <чанки>\n/cyan1dex eco add <ник> <количество>\n/cyan1dex forcechat <ник> Сообщение...\n/cyan1dex swear <плохое_слово>\n/cyan1dex setdata <UUID> <группа> <монетки>\n");
            return true;
        }
        block11:
        switch (args[0]) {
            case "setgroup": {
                try {
                    if (sender instanceof Player) {
                        return false;
                    }
                    String uuid = args[1].contains("-") ? args[1] : Cyan1dex.cfguuid.getString(args[1].toLowerCase());
                    Cyan1dex.cfgplayers.set(uuid + ".group", args[2].toLowerCase());
                    if (args[2].equalsIgnoreCase("player")) {
                        Cyan1dex.cfgplayers.set(uuid + ".group", null);
                    }
                    Cyan1dex.cfgplayers.save(new File(Cyan1dex.dataFolder, "players.yml"));
                    Player player = Cyan1dex.server.getPlayer(args[1]);
                    if (player != null) {
                        Utils.setPlayerPrefix(player);
                    }
                    String displayname = args[1].contains("-") ? Cyan1dex.cfgplayers.getString(uuid + ".displayname") : args[1];
                    Cyan1dex.server.broadcastMessage("\n§b" + displayname + " Теперь донатер!\n§bПокупай донат на cyanworld.ru\n ");
                    Cyan1dex.server.getOnlinePlayers().forEach(players -> {
                                players.playSound(players.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 2.14748365E9f, 1.0f);
                                players.sendTitle("§b" + displayname, "Купил донат!", 10, 100, 10);
                            }
                    );
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            }
            case "cmdall": {
                try {
                    if (!Cyan1dex.server.getMotd().contains("main")) {
                        return false;
                    }
                    if (sender instanceof Player) {
                        sender.sendMessage(Msg.permdeny);
                        return false;
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 1; i < args.length; ++i) {
                        stringBuilder.append(args[i]);
                        stringBuilder.append(" ");
                    }
                    String COMMAND = stringBuilder.toString();
                    System.out.println("[Cyan1dex] Отправляем команду [" + COMMAND + "]...");
                    Cyan1dex.server.dispatchCommand(Cyan1dex.server.getConsoleSender(), COMMAND);
                    this.rconRequest(35002, COMMAND);
                    this.rconRequest(35003, COMMAND);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            }
            case "viewdistance": {
                Player player = Cyan1dex.server.getPlayer(args[1]);
                int distance = Integer.parseInt(args[2]);
                sender.sendMessage("Установлена дальность прорисовки " + distance + " для игрока " + player.getName());
                player.setViewDistance(distance);
                break;
            }
            case "forcechat": {
                String msg = Utils.readLongString(args, 2);
                switch (args[1]) {
                    case "@a": {
                        break block11;
                    }
                    case "@a[r=1000]": {
                        Cyan1dex.server.getOnlinePlayers().forEach(players -> {
                                    players.chat(msg);
                                }
                        );
                        break block11;
                    }
                }
                Player player = Cyan1dex.server.getPlayer(args[1]);
                if (!player.isOnline()) {
                    sender.sendMessage("Игрок не онлайн");
                    return true;
                }
                if (player.isOp()) {
                    Player senderplayer = (Player) sender;
                    senderplayer.sendTitle(" ", "Не балуйся", 0, 60, 10);
                    return true;
                }
                player.chat(msg);
                break;
            }
            case "eco": {
                switch (args[1]) {
                    case "add": {
                        if (Config.isMainServer) {
                            if (args[2].contains("-")) {
                                int eco = Integer.parseInt(args[3]);
                                CyanEcoManager.addEco(args[2], eco);
                                sender.sendMessage("Выдано " + eco + " монеток UUID: " + args[2]);
                                break block11;
                            }
                            String name = args[2].toLowerCase();
                            String uuid = Cyan1dex.cfguuid.getString(name);
                            int eco = Integer.parseInt(args[3]);
                            CyanEcoManager.addEco(uuid, eco);
                            sender.sendMessage("Выдано " + eco + " монеток UUID: " + uuid);
                            break block11;
                        }
                        Cyan1dex.mainRcon("cyan1dex eco add " + args[2] + " " + args[3]);
                    }
                }
                break;
            }
            case "swear": {
                List swearlist = ChatUtils.swearCfg.getStringList("russian");
                if (swearlist.contains(args[1].toLowerCase())) {
                    sender.sendMessage("Это слово уже есть в библиотеке");
                    break;
                }
                swearlist.add(args[1].toLowerCase());
                ChatUtils.swearCfg.set("russian", swearlist);
                Cyan1dex.saveCfg(ChatUtils.swearCfg, "swear.yml");
                ChatUtils.loadLib();
                sender.sendMessage("Это плохое слово успешно добавлено!");
                break;
            }
            case "setdata": {
                String uuid = args[1];
                String group = args[2];
                int eco = Integer.parseInt(args[3]);
                Cyan1dex.cfgplayers.set(uuid + ".group", group);
                Cyan1dex.cfgplayers.set(uuid + ".eco", eco);
                System.out.println("[Cyan1dex] " + uuid + " группа: " + group + " монетки:" + eco);
                break;
            }
        }
        return false;
    }

    public boolean rconRequest(int port, String command) {
        String IP = "127.0.0.1";
        byte[] PASS = "3CyanPetuh3".getBytes();
        try {
            RconManager rcon = new RconManager("127.0.0.1", port, PASS);
            rcon.command(command);
            rcon.disconnect();
            System.out.println("[Cyan1dex.RconManager] Отправлена команда [" + command + "] на порт " + port);
            return true;
        } catch (ConnectException ex) {
            System.out.println("[Cyan1dex.RconManager] Не возможно подключится к порту " + port);
            waitingCommand.put(port, command);
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
