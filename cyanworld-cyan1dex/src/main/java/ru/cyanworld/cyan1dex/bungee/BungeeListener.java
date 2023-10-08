package ru.cyanworld.cyan1dex.bungee;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeListener
        implements Listener {
    static TextComponent text = new TextComponent("Подключайтесь по этому IP:\n§bplay.cyanworld.ru");

    public BungeeListener() {
        CyanBungee.proxy.getPluginManager().registerListener(CyanBungee.plugin, this);
    }

    @EventHandler
    public void onLogin(PreLoginEvent event) {
        PendingConnection connection = event.getConnection();
        String playername = connection.getName();
        String hostname = connection.getVirtualHost().getHostName();
        if (hostname.endsWith("litegamemc.ru") || hostname.endsWith("cyanworld.tk") || hostname.endsWith("arrowmc.ru")) {
            System.out.println(playername + " c домена " + hostname + " попытался подключится");
            event.setCancelled(true);
            event.setCancelReason(text);
        }
    }
}
