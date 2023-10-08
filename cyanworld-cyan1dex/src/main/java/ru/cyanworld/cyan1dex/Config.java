package ru.cyanworld.cyan1dex;

import net.md_5.bungee.api.chat.TextComponent;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Config {
    public static final boolean customtab_enable = Config.getBoolean("customtab.enable");
    public static final String motd = Config.getString("fakebutitsrealonline.motd");
    public static final String servername = Cyan1dex.server.getName();
    public static final String main = ("main");
    public static final TextComponent customtab_header = new TextComponent(Config.getString("customtab.header", "§2KiwiServer"));
    public static final TextComponent customtab_footer = new TextComponent(Config.getString("customtab.footer", servername));
    public static final String reboot_time = Config.getString("reboot.time");
    public static final boolean reboot_enable = Config.getBoolean("reboot.enable", false);
    public static final boolean joinmsg_enable = Config.getBoolean("joinmsg.enable", false);
    public static final boolean perworldtab_enable = Config.getBoolean("perworldtab.enable", false);
    public static final boolean perworldchat_enable = Config.getBoolean("perworldchat.enable", false);
    public static final boolean deathmsg_enable = Config.getBoolean("deathmsg.enable", true);
    public static final boolean fakebutitsrealonline_enable = Config.getBoolean("fakebutitsrealonline.enable", false);
    public static final boolean isMainServer = main != null;
    public static final boolean chattranslator_enable = Config.getBoolean("chattranslator.enable");

    public Config() {
        try {
            Cyan1dex.configuration.save(new File(Cyan1dex.dataFolder, "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Boolean getBoolean(String a) {
        return Cyan1dex.configuration.getBoolean(a);
    }

    public static Boolean getBoolean(String a, boolean def) {
        return Cyan1dex.configuration.getBoolean(a, def);
    }

    public static String getString(String a) {
        return Cyan1dex.configuration.getString(a) != null ? Cyan1dex.configuration.getString(a) : "null";
    }

    public static String getString(String a, String def) {
        return Cyan1dex.configuration.getString(a, def);
    }

    public static List<String> getStringList(String a) {
        return Cyan1dex.configuration.getStringList(a);
    }
}
