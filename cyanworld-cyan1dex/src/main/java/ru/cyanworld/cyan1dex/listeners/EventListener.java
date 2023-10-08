package ru.cyanworld.cyan1dex.listeners;

import com.destroystokyo.paper.event.server.ServerExceptionEvent;
import com.destroystokyo.paper.exception.ServerException;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import ru.cyanworld.cyan1dex.*;
import ru.cyanworld.cyan1dex.api.CustomItem;
import ru.cyanworld.cyan1dex.api.CustomMenu;
import ru.cyanworld.cyan1dex.commands.CmdLang;
import ru.cyanworld.cyan1dex.commands.CmdTrack;
import ru.cyanworld.cyan1dex.utils.ChatUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

public class EventListener
        implements Listener {
    public static Random random = new Random();
    public static DecimalFormat decimalFormat = new DecimalFormat("#.##");
    public static ItemStack diamond = new CustomItem(Material.DIAMOND).name("Кликни, чтобы подключится на сервер");

    public EventListener() {
        Cyan1dex.server.getPluginManager().registerEvents(this, Cyan1dex.instance);
        Cyan1dex.server.getScheduler().runTaskTimer(Cyan1dex.instance, () -> {
                    if (Cyan1dex.tempHolo.isEmpty()) {
                        return;
                    }
                    Cyan1dex.tempHolo.forEach(ArmorStand2 -> {
                                if (ArmorStand2.getTicksLived() >= 20) {
                                    ArmorStand2.remove();
                                }
                            }
                    );
                }
                , 0, 1);
    }

    private static void onError(String message, Exception ex, TextComponent text, Player Player2) {
        Player2.sendActionBar(message);
        if (Player2.isOp()) {
            Player2.sendMessage(ex.getCause().getLocalizedMessage());
            Player2.sendMessage(text);
            Player2.playSound(Player2.getLocation(), Sound.BLOCK_ANVIL_USE, 2.14748365E9f, 2.0f);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClick(InventoryClickEvent event) {
        InventoryHolder inventoryHolder = event.getInventory().getHolder();
        if (event.getCurrentItem() == null) {
            return;
        }
        if (inventoryHolder != null && inventoryHolder instanceof CustomMenu) {
            ((CustomMenu) inventoryHolder).onClick(event);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        CmdTrack.trackcfg.getStringList(uuid).forEach(key -> {
                    Player obs = Cyan1dex.server.getPlayer(UUID.fromString(key));
                    if (obs != null && obs.isOnline()) {
                        obs.sendMessage(player.getDisplayName() + " §rзашёл на сервер!");
                        obs.playSound(obs.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2.14748365E9f, 2.0f);
                    }
                }
        );
        if (Config.fakebutitsrealonline_enable) {
            player.teleport(Cyan1dex.world.getSpawnLocation());
            player.getInventory().clear();
            Utils.healthUpPlayer(player);
            player.getInventory().setItem(random.nextInt(8), diamond);
        }
        Cyan1dex.cfguuid.set(player.getName().toLowerCase(), uuid);
        Utils.setPlayerPrefix(player);
        if (Config.chattranslator_enable) {
            CmdLang.setLang(player, CmdLang.getLang(player));
        }
        if (Config.joinmsg_enable) {
            event.setJoinMessage(player.getDisplayName() + " §7зашёл на сервер");
        } else {
            event.setJoinMessage(null);
        }
        if (Config.customtab_enable) {
            player.setPlayerListHeaderFooter(Config.customtab_header, Config.customtab_footer);
        }
        if (Config.perworldtab_enable) {
            Cyan1dex.server.getWorlds().forEach(Utils::updatePerWorldTab);
            Cyan1dex.server.getScheduler().scheduleSyncDelayedTask(Cyan1dex.instance, () -> {
                        Utils.updatePerWorldTab(player.getWorld());
                    }
                    , 5);
        }
        if (!Config.isMainServer) {
            Cyan1dex.ecoMap.put(player, 0);
        }
        if (Utils.playerIsDonator(player)) {
            player.setViewDistance(16);
        } else {
            player.setViewDistance(8);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        CmdTrack.trackcfg.getStringList(uuid).forEach(key -> {
                    Player obs = Cyan1dex.server.getPlayer(UUID.fromString(key));
                    if (obs != null && obs.isOnline()) {
                        obs.sendMessage(player.getDisplayName() + " §rвышел с сервера");
                        obs.playSound(obs.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2.14748365E9f, 2.0f);
                    }
                }
        );
        if (Config.joinmsg_enable) {
            event.setQuitMessage(player.getDisplayName() + " §7вышел с сервера");
        } else {
            event.setQuitMessage(null);
        }
        if (!Config.isMainServer) {
            int eco = Cyan1dex.ecoMap.get(player);
            if (eco != 0) {
                Cyan1dex.mainRcon("cyan1dex eco add " + player.getUniqueId() + " " + Cyan1dex.ecoMap.get(player));
            }
            Cyan1dex.ecoMap.remove(player);
        }
        if (Config.chattranslator_enable) {
            CmdLang.removeLang(player, CmdLang.getLang(player));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(PlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }
        ChatUtils.chat(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();
        Location loc = entity.getLocation();
        if (damager instanceof Player) {
            if (event.getFinalDamage() < 0.1) {
                return;
            }
            String formattedDouble = decimalFormat.format(event.getFinalDamage());
            ArmorStand as = Utils.createHolo("§c-" + formattedDouble, new Location(loc.getWorld(), loc.getX() + random.nextDouble() * (double) (random.nextBoolean() ? 1 : -1), loc.getY() + 1.0 + random.nextDouble() * (double) (random.nextBoolean() ? 1 : -1), loc.getZ() + random.nextDouble() * (double) (random.nextBoolean() ? 1 : -1)));
            as.addScoreboardTag("tempholo");
            Cyan1dex.tempHolo.add(as);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();
        Utils.healthUpPlayer(player);
        String deathmsg = event.getDeathMessage().replace(player.getName(), "§r" + player.getDisplayName() + "§7");
        if (killer != null) {
            deathmsg = deathmsg.replace(killer.getName(), "§r" + killer.getDisplayName() + "§7");
            switch (player.getLastDamageCause().getCause()) {
                case VOID: {
                    deathmsg = deathmsg + " §7благодаря " + killer.getDisplayName();
                }
            }
            player.sendTitle("§cПОТРАЧЕНО", deathmsg, 0, 40, 10);
            killer.sendTitle(" ", "§aУбит " + player.getDisplayName(), 5, 10, 5);
        } else {
            player.sendTitle("§cПОТРАЧЕНО", deathmsg, 0, 40, 10);
        }
        player.playSound(player.getLocation(), Sound.ENTITY_IRONGOLEM_DEATH, 2.14748365E9f, 1.0f);
        if (Config.deathmsg_enable) {
            event.setDeathMessage(deathmsg);
        } else {
            event.setDeathMessage(null);
        }
        if (player.getGameMode() == GameMode.SPECTATOR) {
            player.setGameMode(GameMode.ADVENTURE);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onError(ServerExceptionEvent event) {
        String message;
        ServerException ex = event.getException();
        String string = message = "§8»§r Ошибка:§7 " + ex.getLocalizedMessage();
        int n = -1;
        string.hashCode();
        switch (n) {
            default:
        }
        TextComponent text = new TextComponent(message);
        StringBuilder sB = new StringBuilder();
        for (StackTraceElement s : ex.getStackTrace()) {
            sB.append(s.toString()).append("\n");
        }
        sB.append("§7Caused by: ").append(ex.getCause()).append("§r\n");
        Cyan1dex.server.getOnlinePlayers().forEach(arg_0 -> EventListener.onError(message, ex, text, arg_0));
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        if (Config.perworldtab_enable) {
            Player player = event.getPlayer();
            World fromworld = event.getFrom();
            Utils.updatePerWorldTab(player.getWorld());
            Utils.updatePerWorldTab(fromworld);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        String cmd = event.getMessage().toLowerCase();
        if (cmd.startsWith("/minecraft:")) {
            event.setCancelled(true);
        }
        switch (cmd) {
            case "/langinfo": {
                event.setCancelled(true);
                HashMap<String, List<Player>> map = Cyan1dex.lang.languagemap;
                player.sendMessage(Cyan1dex.lang.translate(player, "Колчиество языков: ") + map.size());
                map.forEach((LANG, PLAYERSLIST) -> {
                            ArrayList nicknames = new ArrayList();
                            PLAYERSLIST.forEach(PLAYERS -> {
                                        nicknames.add(PLAYERS.getDisplayName());
                                    }
                            );
                            player.sendMessage(LANG + ": " + nicknames);
                        }
                );
                break;
            }
            case "/afk": {
                break;
            }
            case "/money":
            case "/balance":
            case "/bal": {
                player.sendMessage("Ваш баланс: " + Cyan1dex.cfgplayers.getInt(uuid + ".eco") + " Цианов");
                event.setCancelled(true);
                break;
            }
            case "/iwantmoney": {
                if (!Config.isMainServer) break;
                event.setCancelled(true);
                List bought = Cyan1dex.cfgplayers.getStringList(uuid + ".bought");
                if (bought.contains("cyan1dex.iwantcyans")) {
                    player.sendMessage("Ты уже получил халявные монтеки!");
                    break;
                }
                player.sendMessage("Держи браток: +250 монеток");
                bought.add("cyan1dex.iwantcyans");
                Cyan1dex.cfgplayers.set(uuid + ".bought", bought);
                CyanEcoManager.addEco(player, 250, true);
            }
        }
        if (player.isOp()) {
            switch (cmd) {
                case "/stop": {
                    event.setCancelled(true);
                    RebootManager.reboot();
                    break;
                }
                case "/reload":
                case "/reload confirm": {
                    event.setCancelled(true);
                    Cyan1dex.server.broadcastMessage("Перезапуск плагинов...");
                    Cyan1dex.server.reload();
                    break;
                }
                case "/cyan importplayers": {
                    event.setCancelled(true);
                    YamlConfiguration importfile = YamlConfiguration.loadConfiguration(new File(Cyan1dex.dataFolder, "import.yml"));
                    importfile.getKeys(false).forEach(Key -> {
                                Cyan1dex.cfgplayers.set(Key + ".group", importfile.getString(Key + ".group"));
                                Cyan1dex.cfgplayers.set(Key + ".eco", importfile.getInt(Key + ".eco"));
                                Cyan1dex.cfgplayers.set(Key + ".bought", importfile.getStringList(Key + ".bought"));
                            }
                    );
                    Cyan1dex.saveCfg(Cyan1dex.cfgplayers, "players.yml");
                    player.sendMessage("Данные импортированны!");
                    break;
                }
            }
        }
    }

}
