package ru.cyanworld.cyan1dex;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class FakeButItsRealOnline
        implements Listener {
    public FakeButItsRealOnline(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(PacketAdapter.params(plugin, PacketType.Status.Server.SERVER_INFO).optionAsync()) {

            public void onPacketSending(PacketEvent event) {
                WrappedServerPing serverPing = event.getPacket().getServerPings().read(0);
                int hub = Cyan1dex.queryResponseMap.get("hub").getOnlinePlayers();
                int survival = Cyan1dex.queryResponseMap.get("survival").getOnlinePlayers();
                int mw = Cyan1dex.queryResponseMap.get("mw").getOnlinePlayers();
                int bw1 = Cyan1dex.queryResponseMap.get("bw1").getOnlinePlayers();
                int bw2 = Cyan1dex.queryResponseMap.get("bw2").getOnlinePlayers();
                int totalonline = hub + survival + mw + bw1 + bw2;
                serverPing.setPlayersOnline(totalonline);
                serverPing.setPlayersMaximum(totalonline + 1);
                serverPing.setMotD(Config.motd);
            }
        });
    }

}
