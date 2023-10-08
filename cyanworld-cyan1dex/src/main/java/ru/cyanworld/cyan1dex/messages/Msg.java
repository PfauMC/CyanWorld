package ru.cyanworld.cyan1dex.messages;

import ru.cyanworld.cyan1dex.Cyan1dex;

public class Msg {
    public static String consoledeny = Msg.a("consoledeny");
    public static String permdeny = Msg.a("permdeny");
    public static String teleportdeny_otherworld = Msg.a("teleportdeny-otherworld");
    public static String teleporting = Msg.a("teleporting");
    public static String sethome_needsleep = Msg.a("sethome-needsleep");
    public static String sethome_success = Msg.a("sethome-success");

    public static String a(String a) {
        return Cyan1dex.configuration.getString("messages." + a);
    }
}
