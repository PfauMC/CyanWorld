package ru.cyanworld.cyanuniverse;

import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BlockTypes;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import ru.cyanworld.cyan1dex.CyanEcoManager;
import ru.cyanworld.cyan1dex.Utils;

import java.io.File;
import java.util.List;
import java.util.UUID;

import static ru.cyanworld.cyanuniverse.CyanUniverse.*;

public class WorldManager {

    public static PotionEffect lobbyspeed = new PotionEffect(PotionEffectType.SPEED, 99999, 2);

    public static World generateEmptyWorld(String name, int size, boolean autoSave) {
        new WorldCreator(name)
                .type(WorldType.FLAT)
                .seed(0)
                .generatorSettings("3;minecraft:air")
                .generateStructures(false)
                .createWorld();
        World world = server.getWorld(name);
        world.setGameRuleValue("doMobSpawning", "false");
        world.setGameRuleValue("doDaylightCycle", "false");
        world.setGameRuleValue("doWeatherCycle", "false");
        WorldBorder worldBorder = world.getWorldBorder();
        worldBorder.setCenter(0.5, 0.5);
        worldBorder.setWarningDistance(0);
        worldBorder.setSize(size + 1);
        world.setAutoSave(autoSave);
        world.setPVP(false);
        return world;
    }

    public static World generateWorld(String name, WorldType worldType, long seed, String generatorSettings, boolean generateStructures, int size, boolean autoSave) {
        new WorldCreator(name)
                .type(worldType)
                .seed(seed)
                .generatorSettings(generatorSettings)
                .generateStructures(generateStructures)
                .createWorld();
        World world = server.getWorld(name);
        world.setGameRuleValue("doMobSpawning", "false");
        world.setGameRuleValue("doDaylightCycle", "false");
        world.setGameRuleValue("doWeatherCycle", "false");
        WorldBorder worldBorder = world.getWorldBorder();
        worldBorder.setCenter(0.5, 0.5);
        worldBorder.setWarningDistance(0);
        worldBorder.setSize(size + 1);
        world.setAutoSave(autoSave);
        world.setPVP(false);
        return world;
    }

    public static World teleportToWorld(Player player, String worldname) {
        if (worldname == null) {
            System.out.print("CyanUniverse.WorldManager::teleportToWorld - worldname is null");
            return null;
        }
        if (player == null) {
            System.out.print("CyanUniverse.WorldManager::teleportToWorld - player is null");
            return null;
        }
        World world = server.getWorld(worldname);

        //int size = Utils.playerIsDonator(worldname.split("#")[0]) ? 256 : 128;

        String playeruuid = player.getUniqueId().toString();
        String ownername = worldscfg.getString(worldname + ".owner", "?");
        String worlddisplayname = CyanUniverse.worldscfg.getString(worldname + ".name", "Мир #" + worldname.split("#")[1] + " " + ownername);

        int size = worldscfg.getInt(worldname + ".size", (Utils.playerIsDonator(playeruuid) ? 512 : 256));

        boolean initworld = worldname.startsWith(playeruuid);

        TextComponent header = new TextComponent("§2KiwiServer");
        TextComponent footer = new TextComponent("Мир " + ownername);

        if (worldname.startsWith(playeruuid)) player.setAllowFlight(true);
        else player.setAllowFlight(false);
        player.getInventory().clear();
        player.getScoreboardTags().remove("keepInventory");

        CodeEventHandler codeEventHandler = Coding.codeMap.get(world);

        switch (worldscfg.getString(worldname + ".type")) {
            case "building": {
                footer = new TextComponent("Строительство: " + worlddisplayname + "\n§rАвтор: " + ownername);
                if (world == null) {
                    world = WorldManager.generateEmptyWorld(worldname, size, true);
                    codeEventHandler = new CodeEventHandler(world);
                }
                Scoreboard sb = CyanUniverse.worldScoreboardMap.get(world);
                if (initworld) {
                    sb = server.getScoreboardManager().getNewScoreboard();
                    CyanUniverse.worldScoreboardMap.put(world, sb);
                    world.getWorldBorder().setSize(size + 1);
                    world.getWorldBorder().setCenter(0.5, 0.5);
                    world.setAutoSave(true);
                }
                player.setScoreboard(sb != null ? sb : CyanUniverse.server.getScoreboardManager().getMainScoreboard());
                if (worldname.startsWith(playeruuid)) player.setGameMode(GameMode.CREATIVE);
                else player.setGameMode(GameMode.ADVENTURE);
                player.teleport(world.getSpawnLocation().add(0.5, 0, 0.5));
                player.getInventory().setItem(8, ItemsList.worldscompass);
                player.getInventory().setItem(7, ItemsList.deco);
                player.setExp(0);
                player.setLevel(CyanEcoManager.getEco(player));
                break;
            }
            case "coding": {
                footer = new TextComponent("Кодинг: " + worlddisplayname + "\n§rАвтор: " + ownername);
                if (world == null) {
                    world = WorldManager.generateEmptyWorld(worldname, size, true);
                    codeEventHandler = new CodeEventHandler(world);
                }
                if (initworld) {
                    Scoreboard sb = server.getScoreboardManager().getNewScoreboard();
                    CyanUniverse.worldScoreboardMap.put(world, sb);
                    world.getWorldBorder().setCenter(1050, 1050);
                    world.getWorldBorder().setSize(100);
                    world.setAutoSave(true);
                    world.setPVP(false);
                    Block endchest = new Location(world, 1000, 0, 1000).getBlock();
                    Block endchest1 = new Location(world, 1099, 0, 1099).getBlock();
                    if (endchest1.getType() != Material.ENDER_CHEST) {
                        for (int x = 1000; x < 1100; x++) {
                            int linenumber = 0;
                            for (int z = 1000; z < 1100; z++) {
                                Block block = world.getBlockAt(x, 0, z);
                                if (x % 2 == 1 && z % 3 == 2) {
                                    if (x == 1001) {
                                        block.setType(Material.COMMAND_REPEATING);
                                        block.setData((byte) 5);
                                        Utils.createHolo("Строка " + linenumber, block.getLocation().add(0.5, 2, 0.5));
                                        linenumber++;
                                    } else {
                                        block.setType(Material.COMMAND_CHAIN);
                                        block.setData((byte) 5);
                                    }
                                } else block.setType(Material.GLASS);
                            }
                        }
                        endchest.setType(Material.ENDER_CHEST);
                        endchest1.setType(Material.ENDER_CHEST);
                    }
                }

                Coding.giveCodingKit(player);
                if (worldname.startsWith(playeruuid)) {
                    player.setGameMode(GameMode.ADVENTURE);
                    player.sendMessage(" \n§b§lДобро пожаловать в Кодинг!" +
                            "\n§rДля быстрого переключения между режимами используйте команды:\n§b/mode play§r, §b/mode code§r, §b/mode build" +
                            "\n§rДля дюпа предметов команда: §b/dupe" +
                            "\n§rВыдать права кодинга игроку: §b/code ник" +
                            "\n ");
                } else {
                    player.setGameMode(GameMode.SPECTATOR);
                }
                Utils.healthUpPlayer(player);
                player.setAllowFlight(true);
                player.teleport(new Location(world, 1001.5, 3, 1000.5));
                break;
            }
            case "playing": {
                footer = new TextComponent(worlddisplayname + "\n§rАвтор: " + ownername);
                if (world == null) {
                    world = WorldManager.generateEmptyWorld(worldname, size, false);
                    codeEventHandler = new CodeEventHandler(world);
                }
                if (initworld) {
                    Scoreboard sb = server.getScoreboardManager().getNewScoreboard();
                    CyanUniverse.worldScoreboardMap.put(world, sb);
                    world.getWorldBorder().setSize(size + 1);
                    world.getWorldBorder().setCenter(0.5, 0.5);
                    world.setAutoSave(false);
                    if (codeEventHandler == null) codeEventHandler = new CodeEventHandler(world);
                    codeEventHandler.compile();
                }
                player.setAllowFlight(false);
                Utils.healthUpPlayer(player);
                player.setGameMode(GameMode.ADVENTURE);
                player.teleport(world.getSpawnLocation().add(0.5, 0, 0.5));
                codeEventHandler.runCode("СобытиеИгрока_Вход", UUID.randomUUID(), player, null);
                codeEventHandler.runCode("Планировщик_Цикл", null, null, null);
                break;
            }
        }
        worldscfg.set(worldname + ".lastvisit", System.currentTimeMillis());
        player.setPlayerListHeaderFooter(header, footer);
        Utils.healthUpPlayer(player);
        player.getInventory().setHeldItemSlot(0);
        if (!worldname.startsWith(playeruuid)) {
            List<String> playedbefore = worldscfg.getStringList(worldname + ".playedbefore");
            if (!playedbefore.contains(playeruuid)) {
                playedbefore.add(playeruuid);
                worldscfg.set(worldname + ".playedbefore", playedbefore);
                world.getPlayers().forEach(players -> players.sendMessage(player.getDisplayName() + " §7зашёл в мир впервые"));
                Player owner = server.getPlayer(UUID.fromString(worldname.split("#")[0]));
                if (owner != null && owner.isOnline()) {
                    CyanEcoManager.addEco(owner, 1, true);
                    owner.sendMessage("§b+1 монетка");
                }
            } else {
                world.getPlayers().forEach(players -> players.sendMessage(player.getDisplayName() + " §7зашёл в мир"));
            }
        }
        return world;
    }

    public static void teleportToLobby(Player player) {
        player.teleport(lobby.getSpawnLocation().add(0.5, 0, 0.5));
        server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (player.isOnline()) {
                player.setScoreboard(CyanUniverse.worldScoreboardMap.get(lobby));
                player.setGameMode(GameMode.ADVENTURE);
                player.getInventory().clear();
                player.getInventory().setItem(0, ItemsList.worldscompass);
                player.getInventory().setItem(1, ItemsList.myworlds);
                player.setHealth(20);
                player.setFoodLevel(20);
                player.setFireTicks(0);
                player.setFallDistance(0);
                player.getActivePotionEffects().forEach(PotionEffect -> player.removePotionEffect(PotionEffect.getType()));
                player.setExp(0);
                player.setLevel(CyanEcoManager.getEco(player));
                player.getInventory().setHeldItemSlot(0);
                player.sendTitle(" ", " ", 20, 20, 20);
                player.addPotionEffect(lobbyspeed);
                Utils.updatePerWorldTab(player.getWorld());
                if (Utils.playerIsDonator(player) || Utils.playerIsModer(player) || player.isOp() == true) {
                    player.setAllowFlight(true);
                } else player.setAllowFlight(false);
            }
        }, 5);

        TextComponent header = new TextComponent("§2KiwiServer");
        TextComponent footer = new TextComponent("Лобби");
        player.setPlayerListHeaderFooter(header, footer);
    }

    public static void generateWorld(Player player, String worldname, Material type) {
        int size = Utils.playerIsDonator(player) ? 512 : 256;
        World world = WorldManager.generateEmptyWorld(worldname, size, true);
        world.setTime(0);
        com.sk89q.worldedit.world.World faweworld = FaweAPI.getWorld(worldname);

        switch (type) {
            case GRASS: {
                try (EditSession session = WorldEdit.getInstance().newEditSession(faweworld)) {
                    session.setBlocks((Region) new CuboidRegion(BlockVector3.at(-(size / 2), 0, -(size / 2)), BlockVector3.at((size / 2), 0, (size / 2))), BlockTypes.BEDROCK.getDefaultState());
                    session.setBlocks((Region) new CuboidRegion(BlockVector3.at(-(size / 2), 1, -(size / 2)), BlockVector3.at((size / 2), 2, (size / 2))), BlockTypes.DIRT.getDefaultState());
                    session.setBlocks((Region) new CuboidRegion(BlockVector3.at(-(size / 2), 3, -(size / 2)), BlockVector3.at((size / 2), 3, (size / 2))), BlockTypes.GRASS.getDefaultState());
                }
                break;
            }
            case GLASS: {
                try (EditSession session = WorldEdit.getInstance().newEditSession(faweworld)) {
                    session.setBlocks((Region) new CuboidRegion(BlockVector3.at(-3, 16, -3), BlockVector3.at(3, 16, 3)), BlockTypes.STONE.getDefaultState());
                }
                world.getBlockAt(0, 16, 0).setType(Material.COBBLESTONE);
                break;
            }
        }

        world.setSpawnLocation(0, world.getHighestBlockYAt(0, 0), 0);
        world.setGameRuleValue("doFireTick", "false");
        world.setGameRuleValue("keepInventory", "true");
        world.setGameRuleValue("doTileDrops", "false");
        world.setGameRuleValue("doMobLoot", "false");
        worldscfg.set(worldname + ".type", "building");
        worldscfg.set(worldname + ".owner", player.getDisplayName());
        worldscfg.set(worldname + ".allowenter", "all");
        worldscfg.set(worldname + ".votecount", 0);
        worldscfg.set(worldname + ".votescore", 0d);
        WorldManager.teleportToWorld(player, worldname);
    }

    public static void loadPlayerWorlds(Player player) {

    }

    public static void purgeOldWorlds(int days, boolean delete) {
        long timeNow = System.currentTimeMillis();
        server.broadcastMessage("[CyanUniverse] Удаление старых миров!");
        StringBuilder sb = new StringBuilder();
        sb.append("[CyanUniverse] Будут удалены миры игроков: ");
        final int[] count = {0};
        worldscfg.getKeys(false).forEach(worldName -> {
            if (Utils.playerIsDonator(worldName.split("#")[0])) return;
            Long lastseen = timeNow - worldscfg.getLong(worldName + ".lastvisit");
            if (!(lastseen > 86400000 * days)) return;
            try {
                sb.append(worldscfg.getString(worldName + ".owner")).append(", ");
                FileUtils.deleteDirectory(new File(server.getWorldContainer(), worldName + "/playerdata"));
                if (delete) {
                    worldscfg.set(worldName, null);
                    FileUtils.deleteDirectory(new File(server.getWorldContainer(), worldName));

                }

                count[0]++;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        CyanUniverse.saveCfgs();
        long timeAfterPurge = System.currentTimeMillis();
        if (delete)
            server.broadcastMessage("[CyanUniverse] Удалено " + count[0] + " миров за " + (timeAfterPurge - timeNow) + "мс");
        if (count[0] != 0) server.broadcastMessage(sb.toString());
    }

    public static String getWorldType(World world) {
        if (world == lobby) return "lobby";
        return worldscfg.getString(world.getName() + ".type");
    }
}
