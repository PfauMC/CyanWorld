package ru.cyanworld.cyanuniverse;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import ru.cyanworld.cyan1dex.Cyan1dex;
import ru.cyanworld.cyan1dex.CyanEcoManager;
import ru.cyanworld.cyan1dex.Utils;
import ru.cyanworld.cyan1dex.api.CustomItem;
import ru.cyanworld.cyan1dex.api.ItemBuilder;
import ru.cyanworld.cyanuniverse.commands.CmdNbt;
import ru.cyanworld.cyanuniverse.commands.MainCommand;
import ru.cyanworld.cyanuniverse.menus.MyWorlds;
import ru.cyanworld.cyanuniverse.menus.coding.*;
import ru.cyanworld.cyanuniverse.menus.coding.variables.EffectMenu;
import ru.cyanworld.cyanuniverse.menus.coding.variables.VariableMenu;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ru.cyanworld.cyanuniverse.Coding.cancelEvent;
import static ru.cyanworld.cyanuniverse.Coding.codeMap;
import static ru.cyanworld.cyanuniverse.CyanUniverse.*;

public class MainListener implements Listener {
    public static List<World> unloadwithoutsave = new ArrayList<>();
    public List<Player> canceledOpen = new ArrayList<>();
    DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public MainListener() {
        server.getPluginManager().registerEvents(this, plugin);
        server.getScheduler().scheduleSyncRepeatingTask(plugin, () -> canceledOpen.removeAll(canceledOpen), 0, 1);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        ItemStack itemStack = event.getItem();
        Player player = event.getPlayer();
        World world = player.getWorld();
        UUID eventid = UUID.randomUUID();
        String uuid = player.getUniqueId().toString();

        switch (WorldManager.getWorldType(world)) {
            //TODO: Правирить!!1!11!
            case "building": {
                try {
                    if (!event.hasItem()) {
                        if (player.getGameMode() != GameMode.CREATIVE) {
                            event.setUseInteractedBlock(Event.Result.DENY);
                        }
                    }
                    if (event.getAction().name().startsWith("RIGHT_CLICK") && itemStack != null) {
                        switch (itemStack.getType()) {
                            case PAPER: {
                                Location location = player.getLocation();
                                if (location.getX() >= 999) {
                                    player.sendTitle("§cОШИБКА!", "Нажмите ЛКМ для телепортации в мир", 5, 60, 10);
                                    Utils.playCustomSound("deny", player);
                                } else {
                                    String loc = ("§r" + decimalFormat.format(location.getX()) + " " + decimalFormat.format(location.getY()) + " " + decimalFormat.format(location.getZ()) + " " + decimalFormat.format(location.getYaw()) + " " + decimalFormat.format(location.getPitch())).replace(",", ".");
                                    player.getInventory().getItemInMainHand().getItemMeta().setDisplayName(loc);
                                    player.sendTitle("§aУстановлено местоположение:", loc, 5, 60, 10);
                                    Utils.playCustomSound("ok", player);
                                }
                                break;
                            }
                            case COMPASS: {
                                event.setCancelled(true);
                                if (player.getWorld().getName().startsWith(uuid))
                                    player.openInventory(MenusList.myWorldMenu.inventory);
                                else player.openInventory(MenusList.worldsMenu.inventory);
                                break;
                            }
                            case MAP: {
                                event.setCancelled(true);
                                if (world == lobby) {
                                    player.openInventory(new MyWorlds(player).getInventory());
                                }
                                break;
                            }
                            case PRISMARINE_CRYSTALS: {
                                event.setCancelled(true);
                                player.openInventory(MenusList.decoMain.getInventory());
                                break;
                            }
                            case MAGMA_CREAM: {
                                event.setCancelled(true);
                                player.chat("/lobby");
                                break;
                            }
                        }
                    }

                } catch (Exception ex) {
                }

            }
            case "coding": {
                if (player.getGameMode() == GameMode.SPECTATOR) return;
                if (event.getAction().name().startsWith("RIGHT_CLICK")) {
                    if (itemStack == null || itemStack.getType() == Material.AIR) return;
                    switch (itemStack.getType()) {
                        case IRON_INGOT: {
                            server.getScheduler().scheduleSyncDelayedTask(plugin, () -> player.openInventory(new VariableMenu().getInventory()));
                            break;
                        }
                        case GLASS_BOTTLE: {
                            player.openInventory(new EffectMenu().getInventory());
                            break;
                        }
                        case PAPER: {
                            Location location = player.getLocation();
                            if (location.getX() >= 999) {
                                player.sendTitle("§cОШИБКА!", "Нажмите ЛКМ для телепортации в мир", 5, 60, 10);
                                Utils.playCustomSound("deny", player);
                            } else {
                                String loc = ("§r" + decimalFormat.format(location.getX()) + " " + decimalFormat.format(location.getY()) + " " + decimalFormat.format(location.getZ()) + " " + decimalFormat.format(location.getYaw()) + " " + decimalFormat.format(location.getPitch())).replace(",", ".");
                                player.sendTitle("§aУстановлено местоположение:", loc, 5, 60, 10);
                                player.getInventory().setItemInMainHand(new ItemBuilder(itemStack).name(loc).build());
                                Utils.playCustomSound("ok", player);
                            }
                            break;
                        }
                    }
                } else if (event.getAction().name().startsWith("LEFT_CLICK")) {
                    if (!event.hasItem()) return;
                    switch (event.getMaterial()) {
                        case PAPER: {
                            event.setCancelled(true);
                            Location location = player.getLocation();
                            if (location.getX() >= 999) {
                                player.setGameMode(GameMode.CREATIVE);
                                world.getWorldBorder().setSize(22848);
                                player.teleport(world.getSpawnLocation());
                                world.getWorldBorder().setSize(100);
                            } else {
                                player.teleport(new Location(world, 1001.5, 3, 1000.5));
                                player.setGameMode(GameMode.ADVENTURE);
                                player.setAllowFlight(true);
                            }
                            break;
                        }
                    }
                }
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    Block block = event.getClickedBlock();
                    switch (block.getType()) {
                        case WALL_SIGN: {
                            Sign sign = (Sign) block.getState();
                            String lines[] = sign.getLines();
                            switch (lines[0]) {
                                case "§lСобытие игрока": {
                                    player.openInventory(new PlayerEventMenu(sign).getInventory());
                                    break;
                                }
                                case "§lСделать игроку": {
                                    player.openInventory(new PlayerActionMenu(sign).getInventory());
                                    break;
                                }
                                case "§lЕсли игрок": {
                                    player.openInventory(new IfPlayerMenu(sign).getInventory());
                                    break;
                                }
                                case "§lПараметры игры": {
                                    player.openInventory(new GameAction(sign).getInventory());
                                    break;
                                }
                                case "§lПланировщик": {
                                    player.openInventory(new SchedulerMenu(sign).getInventory());
                                    break;
                                }
                                case "§lЕсли моб": {
                                    player.openInventory(new IfEntityMenu(sign).getInventory());
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
                break;
            }
            case "playing": {
                CodeEventHandler codeEventHandler = codeMap.get(world);
                switch (event.getAction()) {
                    case LEFT_CLICK_BLOCK: {
                        if (event.getHand() == EquipmentSlot.OFF_HAND) {
                            codeEventHandler.runCode("СобытиеИгрока_ЛевыйКлик", eventid, player, null);
                            cancelEvent(event, eventid);
                        }
                        break;
                    }
                    case LEFT_CLICK_AIR: {
                        codeEventHandler.runCode("СобытиеИгрока_ЛевыйКлик", eventid, player, null);
                        cancelEvent(event, eventid);
                        break;
                    }
                    case RIGHT_CLICK_BLOCK: {
                        if (event.getHand() == EquipmentSlot.OFF_HAND) {
                            codeEventHandler.runCode("СобытиеИгрока_ПравыйКлик", eventid, player, null);
                            cancelEvent(event, eventid);
                        }
                        break;
                    }
                    case RIGHT_CLICK_AIR: {
                        codeEventHandler.runCode("СобытиеИгрока_ПравыйКлик", eventid, player, null);
                        cancelEvent(event, eventid);
                        break;
                    }
                }
                break;
            }
            case "lobby": {
                try {
                    if (!event.hasItem()) {
                        if (player.isOp() != true) {
                            event.setUseInteractedBlock(Event.Result.DENY);
                        }
                    }
                    if (event.getAction().name().startsWith("RIGHT_CLICK")) {
                        switch (itemStack.getType()) {
                            case COMPASS: {
                                event.setCancelled(true);
                                player.openInventory(MenusList.worldsMenu.inventory);
                                break;
                            }
                            case MAP: {
                                event.setCancelled(true);
                                if (world == lobby) {
                                    player.openInventory(new MyWorlds(player).getInventory());
                                }
                                break;
                            }

                        }
                    } else if (player.isOp() != true) {
                        event.setUseInteractedBlock(Event.Result.DENY);
                    }
                } catch (Exception ex) {
                    ex.toString();
                }

            }

        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        if (event.getInventory().getType() == InventoryType.MERCHANT && canceledOpen.contains(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void on(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        if (world == lobby) {
            if (!player.isOp()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void entityInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        if (world == lobby) {
            if (!player.isOp()) {
                event.setCancelled(true);
            }
        }
        if (world != lobby) {
            switch (worldscfg.getString(world.getName() + ".type")) {
                case "building": {
                    if (player.getGameMode() != GameMode.CREATIVE) {
                        switch (event.getRightClicked().getType()) {
                            case ITEM_FRAME:
                            case ARMOR_STAND: {
                                event.setCancelled(true);
                                break;
                            }
                        }
                    } else {
                        if (CmdNbt.selectmob.contains(player)) {
                            CmdNbt.selectmob.remove(player);
                            CmdNbt.selectedmob.put(player, event.getRightClicked());

                        }
                    }
                    break;
                }
                case "playing": {
                    UUID eventid = UUID.randomUUID();
                    CodeEventHandler codeEventHandler = codeMap.get(world);
                    if (codeEventHandler == null) return;
                    codeEventHandler.runCode("СобытиеИгрока_КликПоМобу", eventid, player, event.getRightClicked());
                    if (cancelEvent(event, eventid) && event.getRightClicked().getType() == EntityType.VILLAGER) {
                        canceledOpen.add(player);
                    }
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();
        World world = player.getWorld();
        String uuid = player.getUniqueId().toString();

        List<String> list = Cyan1dex.cfgplayers.getStringList(uuid + ".bought");

        if (player.getWorld() == lobby) {
            if (!Utils.playerIsModer(player)) {
                WorldManager.teleportToLobby(player);
                event.setCancelled(true);
                //server.getBanList(BanList.Type.IP).addBan(player.getAddress().getAddress().getHostAddress(), "Сообщите, как у вас получилось строить в лобби §bvk.me/cyanworld", null, null);
                return;
            }
        } else {
            switch (WorldManager.getWorldType(world)) {
                case "coding": {
                    Coding.codingOnPlace(event);
                    break;
                }
            }
        }
        if (player.isOp()) return;
        switch (block.getType()) {
            case SKULL: {
                ItemStack item = event.getItemInHand();
                if (item.getDurability() != 3) return;
                if (!CustomItem.isHasLocName(item)) {
                    if (MainCommand.getTexture(item, true).equals("null")) return;
                    if (Utils.playerIsDonator(player)) {
                        return;
                    }
                    if (CyanEcoManager.getEco(player) < 10) {
                        event.setCancelled(true);
                    } else {
                        CyanEcoManager.addEco(player, -10, false);
                        player.sendTitle(" ", "-10 монеток", 10, 20 * 3, 10);
                    }
                } else if (!player.getWorld().getName().startsWith(uuid)) {
                    player.sendTitle("", "Ставить этот блок можно только в своём мире", 0, 20, 10);
                    event.setCancelled(true);
                } else if (!list.contains(item.getItemMeta().getLocalizedName())) {
                    player.sendTitle("", "У вас не куплен этот блок", 0, 20, 10);
                    event.setCancelled(true);
                }
                break;
            }
            case DRAGON_EGG: {
                if (!player.getWorld().getName().startsWith(uuid)) {
                    player.sendTitle("", "Ставить этот блок можно только в своём мире", 0, 20, 10);
                    event.setCancelled(true);
                } else if (!list.contains("ru.cyanworld.cyanuniverse.block.dragon_egg")) {
                    player.sendTitle("", "У вас не куплен этот блок", 0, 20, 10);
                    event.setCancelled(true);
                }
                break;
            }
            case MOB_SPAWNER: {
                if (!player.getWorld().getName().startsWith(uuid)) {
                    player.sendTitle("", "Ставить этот блок можно только в своём мире", 0, 20, 10);
                    event.setCancelled(true);
                } else if (!list.contains("ru.cyanworld.cyanuniverse.block.spawner")) {
                    player.sendTitle("", "У вас не куплен этот блок", 0, 20, 10);
                    event.setCancelled(true);
                }
                break;
            }
            case BARRIER: {
                if (!player.getWorld().getName().startsWith(uuid)) {
                    player.sendTitle("", "Ставить этот блок можно только в своём мире", 0, 20, 10);
                    event.setCancelled(true);
                } else if (!list.contains("ru.cyanworld.cyanuniverse.block.barrier")) {
                    player.sendTitle("", "У вас не куплен этот блок", 0, 20, 10);
                    event.setCancelled(true);
                }
                break;
            }
            case STRUCTURE_VOID: {
                if (!player.getWorld().getName().startsWith(uuid)) {
                    player.sendTitle("", "Ставить этот блок можно только в своём мире", 0, 20, 10);
                    event.setCancelled(true);
                } else if (!list.contains("ru.cyanworld.cyanuniverse.block.structure_void")) {
                    player.sendTitle("", "У вас не куплен этот блок", 0, 20, 10);
                    event.setCancelled(true);
                }
                break;
            }
            case COMMAND:
            case COMMAND_CHAIN:
            case COMMAND_REPEATING:
            case COMMAND_MINECART: {
                if (!player.isOp()) {
                    event.setCancelled(true);
                    player.getInventory().setItemInMainHand(null);
                    player.getInventory().setItemInOffHand(null);
                }
                break;
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        if (world == lobby) {
            if (!Utils.playerIsModer(player)) {
                WorldManager.teleportToLobby(player);
                event.setCancelled(true);
                //server.getBanList(BanList.Type.IP).addBan(player.getAddress().getAddress().getHostAddress(), "Сообщите, как у вас получилось строить в лобби §bvk.me/cyanworld", null, null);
            }
        } else {
            switch (WorldManager.getWorldType(world)) {
                case "coding": {
                    Coding.codingOnBreak(event);
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        World world = player.getWorld();
        if (world != lobby) {
            switch (worldscfg.getString(world.getName() + ".type")) {

            }
        } else if (!Utils.playerIsModer(player)) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        WorldManager.teleportToLobby(player);

        server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (player.hasPlayedBefore()) {
                String playeruuid = player.getUniqueId().toString();
                String update = updatenews.getString(playeruuid);
                if (update != null && !update.equals(version)) {
                    player.sendTitle("§2KiwiServer", "Новое обновление v" + version + " пиши /updates", 10, 100, 100);
                    player.sendMessage(" \nНовое обновление на сервере!\n" + "\nНапиши §b/updates §rчтобы узнать о новостях\n ");
                    player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, Integer.MAX_VALUE, 0.5f);
                    player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, Integer.MAX_VALUE, 1);
                } else player.sendTitle("§2KiwiServer", "С возращением!", 10, 30, 10);
            } else player.sendTitle("§2KiwiServer", "Добро пожаловать!", 10, 60, 10);
        }, 6);

        server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (!Utils.playerIsDonator(player)) return;
            player.addAttachment(plugin, "worldedit.wand", true);
            player.addAttachment(plugin, "worldedit.wand.toggle", true);
            player.addAttachment(plugin, "headdb.open", true);
            player.addAttachment(plugin, "headdb.category.*", true);
            player.addAttachment(plugin, "headdb.free.*", true);

            player.addAttachment(plugin, "worldedit.selection.pos", true);
            player.addAttachment(plugin, "worldedit.selection.chunk", true);
            player.addAttachment(plugin, "worldedit.selection.hpos", true);
            player.addAttachment(plugin, "worldedit.selection.contract", true);
            player.addAttachment(plugin, "worldedit.selection.outset", true);
            player.addAttachment(plugin, "worldedit.selection.inset", true);
            player.addAttachment(plugin, "worldedit.selection.size", true);
            player.addAttachment(plugin, "worldedit.selection.shift", true);

            player.addAttachment(plugin, "worldedit.region.hollow", true);
            player.addAttachment(plugin, "worldedit.region.line", true);
            player.addAttachment(plugin, "worldedit.region.curve", true);
            player.addAttachment(plugin, "worldedit.region.overlay", true);
            player.addAttachment(plugin, "worldedit.region.center", true);
            player.addAttachment(plugin, "worldedit.region.naturalize", true);
            player.addAttachment(plugin, "worldedit.region.walls", true);
            player.addAttachment(plugin, "worldedit.region.faces", true);
            player.addAttachment(plugin, "worldedit.region.smooth", true);
            player.addAttachment(plugin, "worldedit.region.move", true);
            player.addAttachment(plugin, "worldedit.region.deform", true);
            player.addAttachment(plugin, "worldedit.region.forest", true);
            player.addAttachment(plugin, "worldedit.region.stack", true);
            player.addAttachment(plugin, "worldedit.region.set", true);
            player.addAttachment(plugin, "worldedit.region.replace", true);

            player.addAttachment(plugin, "worldedit.clipboard.cut", true);
            player.addAttachment(plugin, "worldedit.clipboard.paste", true);
            player.addAttachment(plugin, "worldedit.clipboard.clear", true);
            player.addAttachment(plugin, "worldedit.clipboard.copy", true);
            player.addAttachment(plugin, "worldedit.clipboard.flip", true);
            player.addAttachment(plugin, "worldedit.clipboard.rotate", true);

            player.addAttachment(plugin, "worldedit.history.undo", true);
            player.addAttachment(plugin, "worldedit.history.redo", true);
            player.addAttachment(plugin, "worldedit.history.clear", true);

            //player.addAttachment(instance, "worldedit.superpickaxe.*", true);
            //player.addAttachment(instance, "worldedit.tool.*", true);
            //player.addAttachment(instance, "worldedit.brush.*", true);


            if (!Utils.playerIsModer(player)) return;
            player.addAttachment(plugin, "chatguard.*", true);
            player.addAttachment(plugin, "worldedit.*", true);
            player.addAttachment(plugin, "fawe.*", true);
            player.addAttachment(plugin, "minecraft.command.ban", true);
            player.addAttachment(plugin, "minecraft.command.ban-ip", true);
            player.addAttachment(plugin, "minecraft.command.pardon", true);
            player.addAttachment(plugin, "minecraft.command.pardon-ip", true);
            player.addAttachment(plugin, "minecraft.command.kick", true);
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        if (CmdNbt.selectmob.contains(player)) CmdNbt.selectmob.remove(player);
        WorldManager.teleportToLobby(player);
    }

    @EventHandler
    public void onNaturalMobSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL && event.getEntityType() == EntityType.SLIME)
            event.setCancelled(true);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World fromworld = event.getFrom();
        UUID eventid = UUID.randomUUID();
        CodeEventHandler codeEventHandler = codeMap.get(fromworld);

        switch (WorldManager.getWorldType(fromworld)) {
            case "playing": {
                if (codeEventHandler != null) {
                    codeEventHandler.getSchedulersList().forEach(BukkitRunnable::cancel);
                    codeEventHandler.runCode("СобытиеИгрока_Выход", eventid, player, null);
                }
                break;
            }
        }
        if (fromworld != lobby)
            fromworld.getPlayers().forEach(Player -> Player.sendMessage(player.getDisplayName() + " §7вышел из мира"));
        if (fromworld.getName().split("#")[0].equals(player.getUniqueId().toString())) {
            if (codeEventHandler != null) codeEventHandler.getSchedulersList().forEach(BukkitRunnable::cancel);

            fromworld.getPlayers().forEach(Player -> {
                WorldManager.teleportToLobby(Player);
                Player.sendTitle("Мир закрыт", "владельцем: " + player.getDisplayName(), 5, 60, 10);
            });
            switch (WorldManager.getWorldType(fromworld)) {
                case "building": {
                    server.unloadWorld(fromworld, true);
                    break;
                }
                case "coding": {
                    if (codeEventHandler != null) codeEventHandler.compile();
                    server.unloadWorld(fromworld, true);
                    break;
                }
                case "playing": {
                    player.sendTitle(" ", " ", 20, 20, 20);
                    codeMap.get(fromworld).compile();
                    if (unloadwithoutsave.contains(fromworld)) {
                        server.unloadWorld(fromworld, false);
                        unloadwithoutsave.remove(fromworld);
                    } else server.unloadWorld(fromworld, true);
                    break;
                }
                default:
                    server.unloadWorld(fromworld, false);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        ItemStack itemStack = event.getItemDrop().getItemStack();
        Player player = event.getPlayer();
        World world = player.getWorld();
        UUID eventid = UUID.randomUUID();
        switch (WorldManager.getWorldType(world)) {
            case "lobby": {
                event.setCancelled(true);
                break;
            }
            case "playing": {
                CodeEventHandler codeEventHandler = codeMap.get(world);
                if (codeEventHandler != null)
                    codeEventHandler.runCode("СобытиеИгрока_ДропПредм", eventid, player, event.getItemDrop());
                Coding.cancelEvent(event, eventid);
                break;
            }
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        Item itementity = event.getItem();
        Player player = event.getPlayer();
        World world = player.getWorld();
        UUID eventid = UUID.randomUUID();
        switch (WorldManager.getWorldType(world)) {
            case "lobby": {
                event.setCancelled(true);
                break;
            }
            case "playing": {
                CodeEventHandler codeEventHandler = codeMap.get(world);
                if (codeEventHandler != null)
                    codeEventHandler.runCode("СобытиеИгрока_ПоднятиеПредм", eventid, player, itementity);
                Coding.cancelEvent(event, eventid);
                break;
            }
        }
    }

    /*
    @EventHandler
    public void onDispence(BlockDispenseEvent event) {
        World world = event.getBlock().getWorld();
        if (world != lobby) {
            switch (worldscfg.getString(world.getName() + ".type")) {

            }
        }
    }
    */

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();

        World world = entity.getWorld();
        Entity entity1 = event.getEntity();
        UUID eventid = UUID.randomUUID();

        if (world == lobby) {
            event.setCancelled(true);
        }
        switch (WorldManager.getWorldType(world)) {
            case "playing": {
                if (damager instanceof Player) {
                    CodeEventHandler codeEventHandler = codeMap.get(world);
                    if (codeEventHandler == null) return;
                    codeEventHandler.runCode("СобытиеИгрока_УронМобу", eventid, (Player) damager, entity);
                    cancelEvent(event, eventid);
                }
                break;
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        World world = entity.getWorld();
        if (world == lobby) {
            event.setCancelled(true);
        }

        switch (WorldManager.getWorldType(world)) {
            case "coding": {
                if (entity instanceof Player) {
                    event.setDamage(0);
                    entity.teleport(new Location(world, 1001.5, 5, 1001.5));
                    Player player = (Player) entity;
                    Utils.healthUpPlayer(player);
                }
                break;
            }
            case "playing": {
                if (entity instanceof Player) {
                    if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                        event.setDamage(20);
                    }
                }
            }
            default: {

            }
        }
    }

    @EventHandler
    public void onEntitydamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();
        World world = damager.getWorld();
        if (world == lobby) {
            event.setCancelled(true);
        } else if (damager instanceof Player) {
            Player player = (Player) damager;
            if (world == lobby) {
                if (!player.isOp()) event.setCancelled(true);
            } else {
                switch (worldscfg.getString(world.getName() + ".type")) {
                    case "building": {
                        if (player.getGameMode() != GameMode.CREATIVE) {
                            switch (entity.getType()) {
                                case VEX:
                                case HUSK:
                                case BLAZE:
                                case GHAST:
                                case GIANT:
                                case SLIME:
                                case STRAY:
                                case WITCH:
                                case EVOKER:
                                case SPIDER:
                                case CAVE_SPIDER:
                                case WITHER:
                                case ZOMBIE:
                                case CREEPER:
                                case SHULKER:
                                case ENDERMAN:
                                case FIREBALL:
                                case GUARDIAN:
                                case SKELETON:
                                case ENDERMITE:
                                case MAGMA_CUBE:
                                case PIG_ZOMBIE:
                                case POLAR_BEAR:
                                case SILVERFISH:
                                case VINDICATOR:
                                case ENDER_DRAGON:
                                case ZOMBIE_VILLAGER:
                                case WITHER_SKELETON:
                                case DRAGON_FIREBALL:
                                case SMALL_FIREBALL:
                                case ELDER_GUARDIAN:
                                case SHULKER_BULLET:
                                case WITHER_SKULL:
                                case EVOKER_FANGS:
                                    break;
                                default:
                                    event.setCancelled(true);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {
        UUID eventid = UUID.randomUUID();
        Player player = event.getEntity();
        Player killer = player.getKiller();
        Location loc = player.getLocation();

        World world = player.getWorld();
        if (world == lobby) {
            event.setKeepInventory(true);
            event.setKeepLevel(true);
            player.teleport(lobby.getSpawnLocation().add(0.5, 0, 0.5));
            return;
        }
        switch (WorldManager.getWorldType(world)) {
            case "building": {
                player.teleport(world.getSpawnLocation().add(0.5, 0, 0.5));
                break;
            }
            case "coding": {
                event.setKeepInventory(true);
                player.teleport(new Location(world, 1001.5, 5, 1001.5));
                break;
            }
            case "playing": {
                player.teleport(world.getSpawnLocation().add(0.5, 0, 0.5));
                Coding.playingOnDeath(event);
                CodeEventHandler codeEventHandler = codeMap.get(world);
                if (codeEventHandler == null) return;
                if (killer != null) {
                    codeEventHandler.runCode("СобытиеИгрока_УбийствоИгрока", eventid, killer, null);
                }
                break;
            }
            default: {
                player.teleport(world.getSpawnLocation().add(0.5, 0, 0.5));
                break;
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        WorldManager.teleportToLobby(player);
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();
        World world = player.getWorld();
        if (world == lobby) {
            event.setCancelled(true);
        } else switch (worldscfg.getString(world.getName() + ".type")) {
            case "building":
            case "coding": {
                event.setCancelled(true);
                break;
            }
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        UUID eventid = UUID.randomUUID();

        CodeEventHandler codeEventHandler = codeMap.get(world);
        if (codeEventHandler == null) return;
        switch (WorldManager.getWorldType(world)) {
            case "playing": {
                codeEventHandler.runCode("СобытиеИгрока_Приседание", eventid, player, null);
                break;
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        UUID eventid = UUID.randomUUID();

        CodeEventHandler codeEventHandler = codeMap.get(world);
        if (codeEventHandler == null) return;
        switch (WorldManager.getWorldType(world)) {
            case "playing": {
                codeEventHandler.runCode("СобытиеИгрока_СломалБлок", eventid, player, null);
                break;
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        UUID eventid = UUID.randomUUID();

        CodeEventHandler codeEventHandler = codeMap.get(world);
        if (codeEventHandler == null) return;
        switch (WorldManager.getWorldType(world)) {
            case "playing": {
                codeEventHandler.runCode("СобытиеИгрока_ПоставилБлок", eventid, player, null);
                break;
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        World world = player.getWorld();
        String worldname = world.getName();
        String msg = event.getMessage().toLowerCase();
        switch (msg) {
            case "/compile": {
                event.setCancelled(true);
                CodeEventHandler codeEventHandler = Coding.codeMap.get(world);
                if (codeEventHandler != null) {
                    codeEventHandler.compile();
                    player.sendMessage("Код скомпилирован");
                }
                break;
            }
            case "/bcupdate": {
                if (player.isOp()) {
                    event.setCancelled(true);
                    server.getOnlinePlayers().forEach(players -> {
                        players.sendTitle("§2KiwiServer", "Новое обновление v" + version + " пиши /updates", 10, 100, 100);
                        players.sendMessage(" \nНовое обновление на сервере!\n" + "\nНапиши §b/updates §rчтобы узнать о новостях\n ");
                        players.playSound(players.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, Integer.MAX_VALUE, 0.5f);
                        players.playSound(players.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, Integer.MAX_VALUE, 1);
                    });
                }
                break;
            }
            case "/more":
            case "/dupe": {
                event.setCancelled(true);
                if (WorldManager.getWorldType(world).equals("coding")) {
                    ItemStack item = player.getInventory().getItemInMainHand();
                    if (item != null && item.getType() != Material.AIR && item.getAmount() != 64) {
                        item.setAmount(item.getAmount() * 2 > 64 ? 64 : item.getAmount() * 2);
                        player.getInventory().setItemInMainHand(item);
                        Utils.playCustomSound("ok", player);
                    } else {
                        player.sendMessage("Нечего дюпать");
                        Utils.playCustomSound("fail", player);
                    }
                } else {
                    Utils.playCustomSound("fail", player);
                    player.sendMessage("Эту команду можно использовать только в режиме кодинга");
                }
                break;
            }
            case "/lobby":
            case "/leave":
            case "/spawn":
            case "/hub": {
                event.setCancelled(true);
                WorldManager.teleportToLobby(player);
                break;
            }
            case "/world":
            case "/worldinfo":
            case "/rate":
            case "/rating":
            case "/votes": {
                event.setCancelled(true);
                if (player.getWorld() == lobby) {
                    player.sendMessage("Рейтинг мира: §bСУПЕР §aМЕГА §cОТЛИЧНО");
                } else {
                    String playername = CyanUniverse.worldscfg.getString(worldname + ".owner");
                    int votecount = worldscfg.getInt(worldname + ".votecount");

                    player.sendMessage("§fВладелец мира: " + playername + "\n"
                            + "§fКоличество голосов: " + votecount);
                }
                break;
            }
            case "/vote": {
                event.setCancelled(true);
                if (player.getWorld() == lobby) {
                    player.sendMessage("Голосовать можно только за миры игроков");
                    Utils.playCustomSound("deny", player);
                    return;
                }
                List<String> votes = worldscfg.getStringList(worldname + ".votes");
                int votecount = worldscfg.getInt(worldname + ".votecount");
                if (worldname.startsWith(uuid)) {
                    player.sendMessage("Голосовать за свои миры нельзя");
                    Utils.playCustomSound("deny", player);
                } else if (votes.contains(uuid)) {
                    player.sendMessage("Вы уже голосовали за этот мир");
                    Utils.playCustomSound("deny", player);
                } else {
                    votes.add(uuid);
                    worldscfg.set(worldname + ".votecount", votecount + 1);
                    worldscfg.set(worldname + ".votes", votes);
                    player.sendMessage("§aВаш голос принят");
                    Utils.playCustomSound("ok", player);
                    world.getPlayers().forEach(players -> players.sendMessage(" \n" + player.getDisplayName() + " §aпроголосовал за этот мир\nТебе тоже понравился мир? Проголосуй за него: /vote\n "));
                    Player owner = server.getPlayer(UUID.fromString(worldname.split("#")[0]));
                    if (owner != null && owner.isOnline()) {
                        CyanEcoManager.addEco(owner, 5, true);
                        owner.sendMessage("§b+5 монеток");
                    }
                }
            }
            default: {
                if (msg.startsWith("//") || (msg.contains("/worldedit:")) && (player.isOp() == false)) {
                    List<String> list = Cyan1dex.cfgplayers.getStringList(uuid + ".bought");

                    if (!Utils.playerIsDonator(player) && (player.isOp() == false)) {
                        player.sendMessage("Использование WorldEdit разрешено только донатерам");
                        event.setCancelled(true);
                    } else if (!player.getWorld().getName().startsWith(player.getUniqueId().toString()) && (player.isOp() == false)) {
                        player.sendMessage("Использование WorldEdit разрешено только в своём мире");
                        event.setCancelled(true);
                    } else if (player.getGameMode() != GameMode.CREATIVE && (player.isOp() == false)) {
                        player.sendMessage("Использование WorldEdit разрешено только в творческом режиме");
                        event.setCancelled(true);
                    } else if (msg.contains("122") || msg.contains("dragon_egg") || msg.contains("dragonegg")) {
                        if (!list.contains("ru.cyanworld.cyanuniverse.block.dragon_egg")) {
                            player.sendTitle("", "У вас не куплен этот блок", 0, 20, 10);
                            event.setCancelled(true);
                        }
                    } else if ((msg.contains("52") || msg.contains("mob_spawner") || msg.contains("mobspawner") || msg.contains("spawner"))
                            && !list.contains("ru.cyanworld.cyanuniverse.block.spawner")) {
                        player.sendTitle("", "У вас не куплен этот блок", 0, 20, 10);
                        event.setCancelled(true);
                    } else if ((msg.contains("166") || msg.contains("barrier"))
                            && !list.contains("ru.cyanworld.cyanuniverse.block.barrier")) {
                        player.sendTitle("", "У вас не куплен этот блок", 0, 20, 10);
                        event.setCancelled(true);
                    }
                } else return;
            }
        }
    }
}
