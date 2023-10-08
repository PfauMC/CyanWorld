package ru.cyanworld.cyan1dex;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import ru.cyanworld.cyan1dex.commands.CmdTrack;

import java.util.UUID;

public class BungeeBridge
        implements PluginMessageListener {
    public BungeeBridge() {
        Cyan1dex.server.getMessenger().registerOutgoingPluginChannel(Cyan1dex.instance, "BungeeCord");
        Cyan1dex.server.getMessenger().registerIncomingPluginChannel(Cyan1dex.instance, "BungeeCord", this);
    }

    public static void connectPlayer(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        String uuid = player.getUniqueId().toString();
        player.closeInventory();
        if (Config.isMainServer) {
            CmdTrack.trackcfg.getStringList(uuid).forEach(key -> {
                        Player obs = Cyan1dex.server.getPlayer(UUID.fromString(key));
                        if (obs != null && obs.isOnline()) {
                            obs.sendMessage(player.getDisplayName() + " §rподключился к " + server);
                            obs.playSound(obs.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2.14748365E9f, 2.0f);
                        }
                    }
            );
            player.sendTitle("Подключение...", "", 0, 20, 10);
            Cyan1dex.server.getScheduler().scheduleSyncDelayedTask(Cyan1dex.instance, () -> {
                        player.sendPluginMessage(Cyan1dex.instance, "BungeeCord", out.toByteArray());
                    }
                    , 20);
        } else {
            player.sendPluginMessage(Cyan1dex.instance, "BungeeCord", out.toByteArray());
        }
    }

    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        subchannel.getClass();
    }
}
