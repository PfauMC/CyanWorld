package ru.cyanworld.cyan1dex.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.cyanworld.cyan1dex.Cyan1dex;
import ru.cyanworld.cyan1dex.geo.GeoLocation;
import ru.cyanworld.cyan1dex.geo.GeoUtils;
import ru.cyanworld.cyan1dex.messages.Msg;

import java.util.ArrayList;
import java.util.UUID;

public class CmdPlayerInfo
        extends Command {
    public CmdPlayerInfo() {
        super("playerinfo", "Узнать информацию о игроке", "/playerinfo <ник>", new ArrayList());
        Cyan1dex.server.getCommandMap().register(this.getName(), this);
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(Msg.permdeny);
            return true;
        }
        switch (args.length) {
            case 1: {
                this.infoHandler(args[0], sender);
                break;
            }
            //case 2: {
            //    if (sender instanceof Player) {
            //        sender.sendMessage(Msg.consoledeny);
            //        return true;
            //    }
            //    Cyan1dex.cfgplayers.set(args[0] + ".group", args[1]);
            //   System.out.println(args[0] + " добавлен в группу " + args[1]);
            //}
            default: {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    this.infoHandler(player.getUniqueId().toString(), sender);
                    break;
                }
                sender.sendMessage(Msg.consoledeny);
            }
        }
        return true;
    }

    private void infoHandler(String arg0, CommandSender sender) {
        String uuid = arg0.contains("-") ? arg0 : Cyan1dex.cfguuid.getString(arg0.toLowerCase());
        Player player = Cyan1dex.server.getPlayer(UUID.fromString(uuid));
        sender.sendMessage(" ");
        sender.sendMessage("§8»§r Никнейм:§7 " + Cyan1dex.cfgplayers.getString(uuid + ".displayname"));
        sender.sendMessage("§8»§r UUID:§7 " + uuid);
        if (player == null) {
            sender.sendMessage("§8»§r IP:§7 " + Cyan1dex.cfgplayers.getString(uuid + ".ip.address") + " [" + Cyan1dex.cfgplayers.getString(uuid + ".ip.country") + ", " + Cyan1dex.cfgplayers.getString(uuid + ".ip.region") + ", " + Cyan1dex.cfgplayers.getString(uuid + "ip.city]"));
        } else {
            String ip = player.getAddress().toString().replaceAll(":" + player.getAddress().getPort(), "").replaceAll("/", "").replaceFirst(player.getAddress().getHostName(), "");
            GeoLocation geoloc = GeoUtils.getIpGeoBaseDataByIp(ip);
            try {
                sender.sendMessage("§8»§r IP:§7 " + ip + " [" + geoloc.getCountry() + ", " + geoloc.getRegion() + ", " + geoloc.getCity() + "]");
            } catch (Exception exception) {
                // empty catch block
            }
        }
        sender.sendMessage(" ");
    }
}
