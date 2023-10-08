/*
 * Decompiled with CFR 0_123.
 *
 * Could not load the following classes:
 *  net.md_5.bungee.api.ProxyServer
 *  net.md_5.bungee.api.plugin.Plugin
 */
package ru.cyanworld.cyan1dex.bungee;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class CyanBungee extends Plugin {
    public static ProxyServer proxy;
    public static Plugin plugin;

    public void onEnable() {
        proxy = this.getProxy();
        plugin = this;
        new BungeeListener();
    }
}
