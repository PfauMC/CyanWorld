package ru.cyanworld.cyan1dex;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.cyanworld.cyan1dex.commands.*;
import ru.cyanworld.cyan1dex.listeners.EventListener;
import ru.cyanworld.cyan1dex.messages.Msg;
import ru.cyanworld.cyan1dex.messages.YandexTranslator;
import ru.cyanworld.cyan1dex.query.MCQuery;
import ru.cyanworld.cyan1dex.query.QueryResponse;
import ru.cyanworld.cyan1dex.rcon.RconManager;
import ru.cyanworld.cyan1dex.utils.ChatUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Cyan1dex extends JavaPlugin {
    public static Server server;
    public static Cyan1dex instance;
    public static File dataFolder;
    public static Random random;
    public static YandexTranslator translator;
    public static LanguageUtils lang;
    public static EffectManager effectManager;
    public static ChatUtils chatUtils;
    public static FileConfiguration configuration;
    public static YamlConfiguration cfgplayers;
    public static YamlConfiguration cfguuid;
    public static YamlConfiguration goodbyedear;
    public static Map<String, QueryResponse> queryResponseMap;
    public static Map<Player, Integer> ecoMap;
    public static World world;
    public static List<ArmorStand> tempHolo;

    static {
        random = new Random();
        queryResponseMap = new HashMap<String, QueryResponse>();
        tempHolo = new ArrayList<ArmorStand>();
    }

    public static void saveCfg(FileConfiguration cfg, String name) {
        try {
            cfg.save(new File(dataFolder, name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void mainRcon(String cmd) {
        System.out.println("[MainRCON] /" + cmd);
        Cyan1dex.rcon(35001, cmd, "4HJU9VPONRUFKE8A1PH4");
    }

    public static void rcon(String servername, String cmd) {
        switch (servername) {
            case "auth": {
                Cyan1dex.rcon(35000, cmd, "3CyanPetuh3");
                break;
            }
            case "survival": {
                Cyan1dex.rcon(35002, cmd, "3CyanPetuh3");
                break;
            }
            case "mw": {
                Cyan1dex.rcon(35003, cmd, "3CyanPetuh3");
                break;
            }
            case "bw1": {
                Cyan1dex.rcon(35101, cmd, "3CyanPetuh3");
                break;
            }
            case "bw2": {
                Cyan1dex.rcon(35102, cmd, "3CyanPetuh3");
                break;
            }
            default: {
                Cyan1dex.rcon(35001, cmd, "3CyanPetuh3");
            }
        }
    }

    public static void rcon(int port, String cmd, String pass) {
        try {
            RconManager rcon = new RconManager("localhost", port, pass.getBytes());
            rcon.command(cmd);
            rcon.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onEnable() {
        server = this.getServer();
        instance = this;
        dataFolder = this.getDataFolder();
        try {
            configuration = this.getConfig();
            configuration.save(new File(dataFolder, "config.yml"));
            cfgplayers = YamlConfiguration.loadConfiguration(new File(dataFolder, "players.yml"));
            cfguuid = YamlConfiguration.loadConfiguration(new File(dataFolder, "uuid.yml"));
            new Config();
            new Msg();
            chatUtils = new ChatUtils();
            new RebootManager();
            ecoMap = new HashMap<Player, Integer>();
            world = server.getWorld("world");
            new BungeeBridge();
            new EventListener();
            translator = new YandexTranslator(Arrays.asList("trnsl.1.1.20170422T093752Z.2a56bcc7b9a144df.80639eb6042f852255fab658bc107a0f6995554d", "trnsl.1.1.20170422T093801Z.6c7589690cd72e17.17c8b7df162fcb00389d50d025aead63ab97f20c", "trnsl.1.1.20170422T093823Z.09bb4567c9157c9c.4c4f9c6b07defe91494a26b4720b3f9532274cba"));
            lang = new LanguageUtils();
            this.initCommands();
            System.out.println("[Cyan1dex] Запуск для сервера " + Config.servername);
            server.getScheduler().scheduleSyncDelayedTask(instance, () -> {
                        server.getOnlinePlayers().forEach(players -> {
                                    players.sendMessage("§aПлагины перезапущены!");
                                }
                        );
                    }
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            server.shutdown();
        }
    }

    public void onDisable() {
        Cyan1dex.saveCfg(cfgplayers, "players.yml");
        Cyan1dex.saveCfg(cfguuid, "uuid.yml");
        Cyan1dex.saveCfg(CmdTrack.trackcfg, "track.yml");
        server.getWorlds().forEach(World2 -> {
                    World2.getEntities().forEach(Entity2 -> {
                                if (!(Entity2 instanceof ArmorStand)) {
                                    return;
                                }
                                ArmorStand as = (ArmorStand) Entity2;
                                if (as.getScoreboardTags().contains("tempholo")) {
                                    as.remove();
                                }
                            }
                    );
                }
        );
    }

    private void initCommands() {
        new CmdCyan1dex();
        new CmdBroadcast();
        new CmdPlayerInfo();
        new CmdTell();
        new CmdKick();
        new CmdLang();
        new CmdMe();
        new CmdBan();
        new CmdTrack();
        new CmdG();
    }

    private void initOnlineUpdate() {
        if (!Config.isMainServer) {
            return;
        }
        MCQuery hub = new MCQuery("localhost", 25001);
        MCQuery survival = new MCQuery("localhost", 25002);
        MCQuery mw = new MCQuery("localhost", 25003);
        MCQuery bw1 = new MCQuery("localhost", 25101);
        MCQuery bw2 = new MCQuery("localhost", 25102);
        server.getScheduler().scheduleSyncRepeatingTask(this, () -> {
                    queryResponseMap.put("hub", hub.basicStat());
                    queryResponseMap.put("survival", survival.basicStat());
                    queryResponseMap.put("mw", mw.basicStat());
                    queryResponseMap.put("bw1", bw1.basicStat());
                    queryResponseMap.put("bw2", bw2.basicStat());
                }
                , 0, 20);
    }

    private void initFakeButItsRealOnline() {
        if (!Config.fakebutitsrealonline_enable) {
            return;
        }
        if (!server.getPluginManager().isPluginEnabled("ProtocolLib")) {
            return;
        }
        new FakeButItsRealOnline(this);
    }
}
