package ru.cyanworld.cyanuniverse;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import ru.cyanworld.cyanuniverse.commands.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class CyanUniverse extends JavaPlugin {
    public static String version;
    public static Server server;
    public static CyanUniverse plugin;
    public static PluginDescriptionFile pluginDescription;
    public static File pluginFolder;
    public static YamlConfiguration worldscfg;
    public static YamlConfiguration updatenews;
    public static HashMap<String, Integer> itemPrice = new HashMap<>();
    public static HashMap<World, Scoreboard> worldScoreboardMap = new HashMap<>();
    public static World lobby;

    public static void saveCfgs() {
        try {
            worldscfg.save(new File(pluginFolder, "worlds.yml"));
            updatenews.save(new File(pluginFolder, "update.yml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        server = getServer();
        plugin = this;
        pluginDescription = getDescription();
        pluginFolder = getDataFolder();
        version = pluginDescription.getVersion();
        try {
            worldscfg = YamlConfiguration.loadConfiguration(new File(pluginFolder, "worlds.yml"));
            updatenews = YamlConfiguration.loadConfiguration(new File(pluginFolder, "update.yml"));
            lobby = server.getWorld("world");
            lobby.setPVP(false);
            initLobbyScoreboard();
            new MainListener();
            new MenusList();
            new MainCommand(this);
            new ModeCommand();
            new Coding();
            new CmdNbt();
            new CmdCode();
            new CmdFixcode();
            new CmdWorld();
            new CmdDiscord();
            new CmdRules();
            new CyanUniverseStartup(this);
            server.getWorlds().forEach(world -> {
                if (world == lobby) return;
                new CodeEventHandler(world);
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            server.shutdown();
        }
    }

    @Override
    public void onDisable() {
        saveCfgs();
        server.getOnlinePlayers().forEach(Player::closeInventory);
    }

    private void initLobbyScoreboard() {
        Scoreboard sb = server.getScoreboardManager().getNewScoreboard();
        worldScoreboardMap.put(lobby, sb);
    }
}
