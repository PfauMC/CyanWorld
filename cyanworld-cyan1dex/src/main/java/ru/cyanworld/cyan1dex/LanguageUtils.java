package ru.cyanworld.cyan1dex;

import org.bukkit.entity.Player;
import ru.cyanworld.cyan1dex.commands.CmdLang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LanguageUtils {
    public HashMap<String, List<Player>> languagemap = new HashMap<>();
    public List<String> langs = new ArrayList<>();

    public String translate(Player player, String string) {
        return Cyan1dex.translator.translate(string, CmdLang.getLang(player));
    }
}
