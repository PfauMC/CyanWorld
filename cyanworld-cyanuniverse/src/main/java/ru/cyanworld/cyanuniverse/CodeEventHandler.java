package ru.cyanworld.cyanuniverse;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import ru.cyanworld.cyan1dex.Cyan1dex;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static ru.cyanworld.cyanuniverse.CyanUniverse.plugin;
import static ru.cyanworld.cyanuniverse.CyanUniverse.server;

public class CodeEventHandler {
    public World world;
    public Player owner;
    public YamlConfiguration sourcecode;
    public YamlConfiguration savedInv;
    public File sourcecode_file;
    public Map<String, Object> variableMap = new HashMap<>();

    public Map<String, List<String>> command_lines = new HashMap<>();
    public List<Integer> scheduler_loop = new ArrayList<>();
    public List<Integer> playerevent_join = new ArrayList<>();
    public List<Integer> playerevent_leave = new ArrayList<>();
    public List<Integer> playerevent_rightclick = new ArrayList<>();
    public List<Integer> playerevent_leftclick = new ArrayList<>();
    public List<Integer> playerevent_sneak = new ArrayList<>();
    public List<Integer> playerevent_death = new ArrayList<>();
    public List<Integer> playerevent_killplayer = new ArrayList<>();
    public List<Integer> playerevent_damagemob = new ArrayList<>();
    public List<Integer> playerevent_clickatmob = new ArrayList<>();
    public List<Integer> playerevent_pickupitem = new ArrayList<>();
    public List<Integer> playerevent_dropitem = new ArrayList<>();
    public List<Integer> playerevent_blockplace = new ArrayList<>();
    public List<Integer> playerevent_blockbreak = new ArrayList<>();
    private List<BukkitRunnable> schedulersList = new LinkedList<>();
    private Player playerPlaceholder;
    private Object objectPlaceholder;

    public CodeEventHandler(World world) {
        this.world = world;
        if (!world.getName().contains("#")) return;
        owner = server.getPlayer(UUID.fromString(world.getName().split("#")[0]));
        sourcecode_file = new File(server.getWorldContainer(), world.getName() + "/sourcecode.yml");
        sourcecode = YamlConfiguration.loadConfiguration(sourcecode_file);
        savedInv = YamlConfiguration.loadConfiguration(new File(server.getWorldContainer(), world.getName() + "/savedinv.yml"));
        compile();
        Coding.codeMap.put(world, this);
    }

    public List<BukkitRunnable> getSchedulersList() {
        return schedulersList;
    }

    public void compile() {
        clear();
        sourcecode = new YamlConfiguration();
        for (int i = 0; i < 32; i++) {
            readLine(i);
        }
        command_lines.forEach((number, command_lines) -> sourcecode.set(String.valueOf(number), command_lines));
        try {
            sourcecode.save(sourcecode_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clear() {
        command_lines.clear();
        scheduler_loop.clear();
        schedulersList.forEach(BukkitRunnable::cancel);
        schedulersList.clear();
        playerevent_join.clear();
        playerevent_leave.clear();
        playerevent_rightclick.clear();
        playerevent_leftclick.clear();
        playerevent_sneak.clear();
        playerevent_death.clear();
        playerevent_killplayer.clear();
        playerevent_damagemob.clear();
        playerevent_clickatmob.clear();
        playerevent_pickupitem.clear();
        playerevent_dropitem.clear();
        playerevent_blockbreak.clear();
        playerevent_blockplace.clear();
    }

    private void readLine(int number) {
        int z = 1002 + (3 * number);
        Block block = world.getBlockAt(1001, 1, z);
        if (block.getType() != Material.WALL_SIGN) return;
        Sign eventsign = (Sign) block.getState();
        String[] eventlines = eventsign.getLines();

        switch (eventlines[0]) {
            case "§lСобытие игрока": {
                switch (eventlines[1]) {
                    case "Вход": {
                        playerevent_join.add(number);
                        break;
                    }
                    case "Выход": {
                        playerevent_leave.add(number);
                        break;
                    }
                    case "ПравыйКлик": {
                        playerevent_rightclick.add(number);
                        break;
                    }
                    case "ЛевыйКлик": {
                        playerevent_leftclick.add(number);
                        break;
                    }
                    case "Приседание": {
                        playerevent_sneak.add(number);
                        break;
                    }
                    case "Смерть": {
                        playerevent_death.add(number);
                        break;
                    }
                    case "УбийствоИгрока": {
                        playerevent_killplayer.add(number);
                        break;
                    }
                    case "УронМобу": {
                        playerevent_damagemob.add(number);
                    }
                    case "КликПоМобу": {
                        playerevent_clickatmob.add(number);
                        break;
                    }
                    case "ПоднятиеПредм": {
                        playerevent_pickupitem.add(number);
                        break;
                    }
                    case "ДропПредм": {
                        playerevent_dropitem.add(number);
                        break;
                    }
                    case "ПоставилБлок": {
                        playerevent_blockplace.add(number);
                        break;
                    }
                    case "СломалБлок": {
                        playerevent_blockbreak.add(number);
                        break;
                    }
                    default: {
                        reportError("Не настроенно событие на строке " + number, eventsign.getLocation());
                    }
                }
                break;
            }
            case "§lПланировщик": {
                switch (eventlines[1]) {
                    case "Цикл": {
                        scheduler_loop.add(number);
                        break;
                    }
                }
                break;
            }
        }
        List<String> command_line = writeCodeBlocks(new ArrayList<>(), number, 0, 50);
        command_lines.put(String.valueOf(number), command_line);
    }

    private List<String> writeCodeBlocks(List<String> commands, int line, int from, int to) {
        int z = 1002 + (3 * line);
        int nextcmd = from;

        for (int i = from; i < to; i++) {
            Block signblock = new Location(world, Coding.getXbyCmdNumber(nextcmd), 1, z).getBlock();
            if (signblock.getType() == Material.WALL_SIGN) {
                Sign sign = (Sign) signblock.getState();
                String codeblock = readBlock(sign, line);
                if (codeblock != null) {
                    if (codeblock.startsWith("Если") || codeblock.startsWith("Иначе") || codeblock.startsWith("Планировщик")) {
                        commands.add(codeblock);
                        Location pistonEnd = Coding.getPistonEndFrom(sign.getLocation().add(1, 0, -1));
                        if (pistonEnd != null) {
                            int newnextcmd = Coding.getCmdNumberByX(pistonEnd.getBlockX() + 1);
                            List<String> ifcommands = writeCodeBlocks(new ArrayList<>(), line, nextcmd + 1, newnextcmd - 1);
                            command_lines.put(codeblock, ifcommands);
                            nextcmd = newnextcmd;
                        } else nextcmd++;
                    } else {
                        commands.add(codeblock);
                        nextcmd++;
                    }
                } else nextcmd++;
            } else nextcmd++;
        }
        return commands;
    }

    private String readBlock(Sign sign, int line) {
        String[] lines = sign.getLines();
        Block blockchest = sign.getLocation().add(0, 2, -1).getBlock();
        Location chestloc = blockchest.getLocation();
        String fullloc = chestloc.getBlockX() + "_" + chestloc.getBlockY() + "_" + chestloc.getBlockZ();
        if (lines[1].equals("§o*Кликни блоком*"))
            reportError("Не настроен блок \"" + lines[0] + "§r\" на строке " + line, sign.getLocation());
        switch (lines[0]) {
            case "§lPlayer Action":
            case "§lСделать игроку": {
                return "СделатьИгроку_" + lines[1] + ";" + fullloc;
            }
            case "§lGame Action":
            case "§lПараметры игры": {
                return "ПараметрыИгры_" + lines[1] + ";" + fullloc;
            }
            case "§lIf Player":
            case "§lЕсли игрок": {
                return "ЕслиИгрок_" + lines[1] + ";" + fullloc;
            }
            case "§lIf Entity":
            case "§lЕсли моб": {
                return "ЕслиМоб_" + lines[1] + ";" + fullloc;
            }
            case "§lElse":
            case "§lИначе": {
                return "Иначе;" + fullloc;
            }
            case "§lScheduler":
            case "§lПланировщик": {
                return "Планировщик_" + lines[1] + ";" + fullloc;
            }
            case "§lПеременная": {
                return "Переменная_" + lines[1] + ";" + fullloc;
            }
            case "§lЕсли переменная": {
                return "ЕслиПеременная_" + lines[1] + ";" + fullloc;
            }
        }

        return null;
    }

    public void runCode(String eventname, UUID eventid, Player player, Entity entity) {
        switch (eventname) {
            case "СобытиеИгрока_Вход": {
                playerevent_join.forEach(number -> runBlockCode(String.valueOf(number), number, eventid, player, entity));
                break;
            }
            case "СобытиеИгрока_Выход": {
                playerevent_leave.forEach(number -> runBlockCode(String.valueOf(number), number, eventid, player, entity));
                break;
            }
            case "СобытиеИгрока_ПравыйКлик": {
                playerevent_rightclick.forEach(number -> runBlockCode(String.valueOf(number), number, eventid, player, entity));
                break;
            }
            case "СобытиеИгрока_ЛевыйКлик": {
                playerevent_leftclick.forEach(number -> runBlockCode(String.valueOf(number), number, eventid, player, entity));
                break;
            }
            case "СобытиеИгрока_Приседание": {
                playerevent_sneak.forEach(number -> runBlockCode(String.valueOf(number), number, eventid, player, entity));
                break;
            }
            case "СобытиеИгрока_УронМобу": {
                playerevent_damagemob.forEach(number -> runBlockCode(String.valueOf(number), number, eventid, player, entity));
                break;
            }
            case "СобытиеИгрока_Смерть": {
                playerevent_death.forEach(number -> runBlockCode(String.valueOf(number), number, eventid, player, entity));
                break;
            }
            case "СобытиеИгрока_УбийствоИгрока": {
                playerevent_killplayer.forEach(number -> runBlockCode(String.valueOf(number), number, eventid, player, entity));
                break;
            }
            case "СобытиеИгрока_КликПоМобу": {
                playerevent_clickatmob.forEach(number -> runBlockCode(String.valueOf(number), number, eventid, player, entity));
                break;
            }
            case "СобытиеИгрока_ПоднятиеПредм": {
                playerevent_pickupitem.forEach(number -> runBlockCode(String.valueOf(number), number, eventid, player, entity));
                break;
            }
            case "СобытиеИгрока_ДропПредм": {
                playerevent_dropitem.forEach(number -> runBlockCode(String.valueOf(number), number, eventid, player, entity));
                break;
            }
            case "СобытиеИгрока_ПоставилБлок": {
                playerevent_blockplace.forEach(number -> runBlockCode(String.valueOf(number), number, eventid, player, entity));
                break;
            }
            case "СобытиеИгрока_СломалБлок": {
                playerevent_blockbreak.forEach(number -> runBlockCode(String.valueOf(number), number, eventid, player, entity));
                break;
            }
            case "Планировщик_Цикл": {
                scheduler_loop.forEach(number -> {
                    Chest chest = (Chest) world.getBlockAt(1001, 2, Coding.getZbyLine(number)).getState();
                    int ticks = (int) getNumber(chest, 0);
                    if (ticks < 10) ticks = 10;

                    BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                        @Override
                        public void run() {
                            world.getPlayers().forEach(players -> runBlockCode(String.valueOf(number), number, null, players, null));
                        }
                    };
                    bukkitRunnable.runTaskTimer(Cyan1dex.instance, 1, ticks);
                    schedulersList.add(bukkitRunnable);
                });
                break;
            }
        }
    }

    public void runBlockCode(String name, int line, UUID eventid, Player player, Entity entity) {
        List<String> commands = command_lines.get(name);
        if (commands == null) return;
        for (int i = 0; i < commands.size(); i++) {
            String command = commands.get(i);
            if (!command.startsWith("Иначе")) {
                if (command.startsWith("Если")) {
                    //System.out.println("i: "+i+" size:"+commands.size()+"\ncommands: "+commands);
                    String elsecmd = i < commands.size() - 1 ? commands.get(i + 1) : null;

                    runCodeIf(line, command.split(";"), elsecmd, eventid, player, entity);
                } else runCommand(line, i, command.split(";"), eventid, player, entity);
            }
        }
    }


    public void runCodeIf(int line, String[] ifcmd, String elsecmd, UUID eventid, Player player, Entity entity) {
        // System.out.println("Сработал IF: "+ Arrays.toString(ifcmd)+" else: "+elsecmd);
        String[] loccords = ifcmd[1].split("_");
        Block blockchest = world.getBlockAt(Integer.parseInt(loccords[0]), Integer.parseInt(loccords[1]), Integer.parseInt(loccords[2]));
        Chest chest = null;
        if (blockchest.getType() == Material.CHEST) chest = (Chest) blockchest.getState();
        PlayerInventory inv = player.getInventory();
        ItemStack[] chestitems = getItems(chest);
        List<ItemStack> chestitemslist = chestitems == null ? new ArrayList<>() : Arrays.asList(chestitems);

        if (ifcmd[0].startsWith("ЕслиИгрок") && ifcmd[0].contains("_")) {
            switch (ifcmd[0].split("_")[1]) {
                case "ДержВПравойРуке": {
                    boolean hasitem = false;
                    if (chestitems != null) {
                        for (ItemStack item : chestitems) {
                            if (item != null && item.isSimilar(inv.getItemInMainHand())) {
                                hasitem = true;
                                break;
                            }
                        }
                    }
                    if (hasitem) {
                        runBlockCode(ifcmd[0] + ";" + ifcmd[1], line, eventid, player, entity);
                    } else if (elsecmd != null) {
                        runBlockCode(elsecmd, line, eventid, player, entity);
                    }
                    break;
                }

                case "ДержВЛевойРуке": {
                    boolean hasitem = false;
                    if (chestitems != null) {
                        for (ItemStack item : chestitems) {
                            if (item != null && item.isSimilar(inv.getItemInOffHand())) {
                                hasitem = true;
                                break;
                            }
                        }
                    }
                    if (hasitem) {
                        runBlockCode(ifcmd[0] + ";" + ifcmd[1], line, eventid, player, entity);
                    } else if (elsecmd != null) {
                        runBlockCode(elsecmd, line, eventid, player, entity);
                    }
                    break;
                }
                case "Крадётся": {
                    if (player.isSneaking()) {
                        runBlockCode(ifcmd[0] + ";" + ifcmd[1], line, eventid, player, entity);
                    } else if (elsecmd != null) {
                        runBlockCode(elsecmd, line, eventid, player, entity);
                    }
                    break;
                }
                case "ИмеетПредмет": {
                    boolean hasitem = false;
                    if (chestitems != null) {
                        for (ItemStack item : chestitems) {
                            if (item != null && inv.containsAtLeast(item, item.getAmount())) hasitem = true;
                        }
                    }
                    if (hasitem) {
                        runBlockCode(ifcmd[0] + ";" + ifcmd[1], line, eventid, player, entity);
                    } else if (elsecmd != null) {
                        runBlockCode(elsecmd, line, eventid, player, entity);
                    }
                    break;
                }
                case "ИмеетПредметы": {
                    int check[] = {0};
                    Map<ItemStack, Integer> map = new HashMap<>();
                    if (chestitems != null) {
                        for (ItemStack item : chestitems) {
                            if (item == null) continue;
                            int count = item.getAmount();
                            ItemStack mapitem = item.clone();
                            mapitem.setAmount(1);
                            map.put(mapitem, (map.get(mapitem) != null ? map.get(mapitem) : 0) + count);
                        }
                    }
                    map.forEach((itemStack, integer) -> {
                        boolean hasitems = inv.containsAtLeast(itemStack, integer);
                        if (hasitems) check[0]++;
                    });
                    if (check[0] == map.size()) {
                        runBlockCode(ifcmd[0] + ";" + ifcmd[1], line, eventid, player, entity);
                    } else if (elsecmd != null) {
                        runBlockCode(elsecmd, line, eventid, player, entity);
                    }
                    break;
                }
                case "Надето": {
                    if (Arrays.asList(inv.getArmorContents()).contains(chestitems != null ? chestitems[0] : null)) {
                        runBlockCode(ifcmd[0] + ";" + ifcmd[1], line, eventid, player, entity);
                    } else if (elsecmd != null) {
                        runBlockCode(elsecmd, line, eventid, player, entity);
                    }
                    break;
                }
                case "ДержитЩит": {
                    if (player.isBlocking()) {
                        runBlockCode(ifcmd[0] + ";" + ifcmd[1], line, eventid, player, entity);
                    } else if (elsecmd != null) {
                        runBlockCode(elsecmd, line, eventid, player, entity);
                    }
                    break;
                }
                case "НаЭлитрах": {
                    if (player.isGliding()) {
                        runBlockCode(ifcmd[0] + ";" + ifcmd[1], line, eventid, player, entity);
                    } else if (elsecmd != null) {
                        runBlockCode(elsecmd, line, eventid, player, entity);
                    }
                    break;
                }
                case "Бежит": {
                    if (player.isGliding()) {
                        runBlockCode(ifcmd[0] + ";" + ifcmd[1], line, eventid, player, entity);
                    } else if (elsecmd != null) {
                        runBlockCode(elsecmd, line, eventid, player, entity);
                    }
                    break;
                }
                case "Летает": {
                    if (player.isFlying()) {
                        runBlockCode(ifcmd[0] + ";" + ifcmd[1], line, eventid, player, entity);
                    } else if (elsecmd != null) {
                        runBlockCode(elsecmd, line, eventid, player, entity);
                    }
                    break;
                }
                case "ИмяРавно": {
                    if (player.getName().equals(getText(chest, 0).replaceFirst("§f", ""))) {
                        runBlockCode(ifcmd[0] + ";" + ifcmd[1], line, eventid, player, entity);
                    } else if (elsecmd != null) {
                        runBlockCode(elsecmd, line, eventid, player, entity);
                    }
                    break;
                }
                case "ПредметРавен": {
                    if (entity != null && entity.getType() == EntityType.DROPPED_ITEM) {
                        ItemStack itemdroped = ((Item) entity).getItemStack();

                        boolean hasitem = false;
                        if (chestitems != null) {
                            for (ItemStack item : chestitems) {
                                if (item != null && item.isSimilar(itemdroped)) {
                                    hasitem = true;
                                    break;
                                }
                            }
                        }
                        if (hasitem) {
                            runBlockCode(ifcmd[0] + ";" + ifcmd[1], line, eventid, player, entity);
                        } else if (elsecmd != null) {
                            runBlockCode(elsecmd, line, eventid, player, entity);
                        }
                    }
                    break;
                }
                case "НаКоординатах": {
                    Location loc = getLocation(chest, 0);
                    if (loc == null) {
                        reportError("Не правильно записанны координаты, используйте для этого 'Текст' или 'Местоположение'", chest.getLocation());
                        return;
                    }
                    Location ploc = player.getLocation();
                    if (ploc != null && ploc.getBlockX() == loc.getBlockX() && ploc.getBlockY() == loc.getBlockY() && ploc.getBlockZ() == loc.getBlockZ()) {
                        runBlockCode(ifcmd[0] + ";" + ifcmd[1], line, eventid, player, entity);
                    }
                    break;
                }
                case "БлокРавен": {
                    if (entity == null || !(entity instanceof Block)) return;
                    Block block = (Block) entity;
                    Material blocktype = block.getType();
                    Location blockloc = block.getLocation();
                    Location loc = getLocation(chest, 0);
                    byte blockdata = block.getData();

                    boolean hasitem = false;
                    if (chestitems != null) {
                        for (ItemStack item : chestitems) {
                            if (item != null) {
                                if (item.getType() == Material.PAPER) {
                                    if (loc != null) {
                                        if (loc.getBlockX() == blockloc.getBlockX() && loc.getBlockY() == blockloc.getBlockY() && loc.getBlockZ() == blockloc.getBlockZ()) {
                                            hasitem = true;
                                            break;
                                        }
                                    }
                                } else if (blocktype == item.getType() && blockdata == item.getDurability()) {
                                    hasitem = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (hasitem) {
                        runBlockCode(ifcmd[0] + ";" + ifcmd[1], line, eventid, player, entity);
                    } else if (elsecmd != null) {
                        runBlockCode(elsecmd, line, eventid, player, entity);
                    }
                    break;
                }

            }
        }
        if (ifcmd[0].startsWith("ЕслиМоб") && ifcmd[0].contains("_")) {
            if (entity == null) return;
            switch (ifcmd[0].split("_")[1]) {
                case "Тип": {
                    if (entity.getType().name().toLowerCase().equals(getText(chest, 0))) {
                        runBlockCode(ifcmd[0] + ";" + ifcmd[1], line, eventid, player, entity);
                    } else if (elsecmd != null) {
                        runBlockCode(elsecmd, line, eventid, player, entity);
                    }
                    break;
                }
                case "ИмяРавно": {
                    String name = entity.getCustomName();
                    if (name != null && name.equals(getText(chest, 0))) {
                        runBlockCode(ifcmd[0] + ";" + ifcmd[1], line, eventid, player, entity);
                    } else if (elsecmd != null) {
                        runBlockCode(elsecmd, line, eventid, player, entity);
                    }
                    break;
                }
            }
        }
    }

    private void runCommand(int line, int number, String[] command, UUID eventid, Player player, Entity entity) {
        //if (player.getWorld() != world) return false;
        if (command[0].startsWith("Иначе")) return;
        //server.broadcastMessage(Arrays.toString(command) + " §7Строка:" + line + " Номер:" + number);
        String[] loccords = command[1].split("_");
        Block blockchest = world.getBlockAt(Integer.parseInt(loccords[0]), Integer.parseInt(loccords[1]), Integer.parseInt(loccords[2]));
        Chest chest = null;
        if (blockchest.getType() == Material.CHEST) chest = (Chest) blockchest.getState();
        /*
        if (command[0].startsWith("ВыполнитьСтроку")) {
            runCode("warp_" + (int) getNumber(chest, 0), eventid, player, entity);
            //return true;
        }
        */
        if (command[0].startsWith("Планировщик") && command[0].contains("_")) {
            switch (command[0].split("_")[1]) {
                case "Ждать": {
                    int ticks = (int) getNumber(chest, 0);
                    if (ticks == -1) ticks = 20;
                    server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        if (player != null && player.getWorld() == world) {
                            runBlockCode(command[0] + ";" + command[1], line, eventid, player, entity);
                        }
                    }, ticks);
                    //return false;
                }
            }
        }

        PlayerInventory inv = player.getInventory();
        ItemStack[] chestitems = getItems(chest);
        List<ItemStack> chestitemslist = chestitems == null ? new ArrayList<>() : Arrays.asList(chestitems);

        if (command[0].startsWith("СделатьИгроку") && command[0].contains("_")) {
            switch (command[0].split("_")[1]) {
                case "ВыдатьПредмет": {
                    server.getScheduler().scheduleSyncDelayedTask(plugin, () -> chestitemslist.forEach(itemStack -> {
                        if (itemStack != null) inv.addItem(itemStack.clone());
                    }));
                    break;
                }
                case "НадетьБроню": {
                    if (chestitems != null && chestitems[0] != null) inv.setHelmet(chestitems[0]);
                    if (chestitems != null && chestitems[1] != null) inv.setChestplate(chestitems[1]);
                    if (chestitems != null && chestitems[2] != null) inv.setLeggings(chestitems[2]);
                    if (chestitems != null && chestitems[3] != null) inv.setBoots(chestitems[3]);
                    break;
                }
                case "УдалитьПредмет": {
                    if (chestitems != null) {
                        for (ItemStack item : chestitems) {
                            if (item != null) inv.removeItem(item);
                        }
                    }
                    break;
                }
                case "ОчиститьИнв": {
                    inv.clear();
                    break;
                }
                case "ОтпрСообщение": {
                    player.sendMessage(getText(chest, 0));
                    break;
                }
                case "ОчиститьЧат": {
                    player.sendMessage(Coding.clearChat);
                    break;
                }
                case "ЭкшнБар": {
                    player.sendActionBar(getText(chest, 0));
                    break;
                }
                case "Титл": {
                    int fadeIn = (int) getNumber(chest, 2);
                    int stay = (int) getNumber(chest, 3);
                    int fadeOut = (int) getNumber(chest, 4);
                    player.sendTitle(
                            getText(chest, 0),
                            getText(chest, 1),
                            fadeIn != -1 ? fadeIn : 20,
                            stay != -1 ? stay : 20,
                            fadeOut != -1 ? stay : 20
                    );
                    break;
                }
                case "Звук": {
                    float pitch = (float) getNumber(chest, 1);
                    String sound = getText(chest, 0).replace("§f", "");
                    player.playSound(player.getLocation(), sound, Integer.MAX_VALUE, pitch != -1 ? pitch : 1);
                    break;

                }
                case "ВклПолёт": {
                    player.setAllowFlight(true);
                    break;
                }
                case "ВыклПолёт": {
                    player.setAllowFlight(false);
                    break;
                }
                case "Приключение": {
                    player.setGameMode(GameMode.ADVENTURE);
                    break;
                }
                case "Выживание": {
                    player.setGameMode(GameMode.SURVIVAL);
                    break;
                }
                case "Креатив": {
                    player.setGameMode(GameMode.CREATIVE);
                    break;
                }
                case "Наблюдатель": {
                    player.setGameMode(GameMode.SPECTATOR);
                    break;
                }
                case "ВыклСохИнвСмерти": {
                    player.getScoreboardTags().remove("keepInventory");
                    break;
                }
                case "ВклСохИнвСмерти": {
                    player.getScoreboardTags().add("keepInventory");
                    break;
                }
                case "ПредметВСлот": {
                    if (chestitems != null) {
                        ItemStack item = chestitems[0];
                        int slot = (int) getNumber(chest, 1);
                        if (slot <= -1 || slot >= 41) {
                            inv.addItem(item.clone());
                        } else inv.setItem(slot, item);
                    }
                    break;
                }
                case "СохранитьИнв": {
                    String uuid = player.getUniqueId().toString();
                    for (int i = 0; i < 40; i++) {
                        ItemStack item = inv.getItem(i);
                        savedInv.set(uuid + "." + i, item);
                    }
                    break;
                }
                case "ЗагрузитьИнв": {
                    String uuid = player.getUniqueId().toString();
                    for (int i = 0; i < 40; i++) {
                        ItemStack item = savedInv.getItemStack(uuid + "." + i);
                        inv.setItem(i, item);
                    }
                    break;
                }
                case "Здоровье": {
                    double health = getNumber(chest, 0);
                    if (health == -1 || health < 0 || health > 20) health = 20;
                    if (player.getLastDamageCause().getCause() != EntityDamageEvent.DamageCause.VOID)
                        player.setHealth(health);
                    break;
                }
                case "Сытость": {
                    int food = (int) getNumber(chest, 0);
                    if (food == -1 || food < 0 || food > 20) food = 20;
                    player.setFoodLevel(food);
                    break;
                }
                case "УровеньОпыта": {
                    int xp = (int) getNumber(chest, 0);
                    player.setLevel(xp);
                    break;
                }
                case "ШкалаОпыта": {
                    int xp = (int) getNumber(chest, 0);
                    if (xp < 0 || xp > 100) xp = 0;
                    float finalxp = Float.parseFloat(xp == 100 ? "1" : "0." + xp);
                    player.setExp(finalxp);
                    break;
                }
                case "ДобЭффект": {
                    String potionname = getText(chest, 0).replace("§f", "");
                    PotionEffectType potion = PotionEffectType.getByName(potionname);
                    if (potion == null) return;
                    int duration = (int) getNumber(chest, 1);
                    if (duration == -1) duration = 20;
                    int amp = (int) getNumber(chest, 2);
                    if (amp < 0) amp = 0;
                    if (amp > 255) amp = 255;

                    int finalDuration = duration;
                    int finalAmp = amp;
                    server.getScheduler().scheduleSyncDelayedTask(plugin, () -> player.addPotionEffect(new PotionEffect(potion, finalDuration, finalAmp)), 1);
                    break;
                }
                case "УдалЭффект": {
                    String potionname = getText(chest, 0).replace("§f", "");
                    PotionEffectType potion = PotionEffectType.getByName(potionname);
                    if (potion == null) return;
                    player.removePotionEffect(potion);
                    break;
                }
                case "Телепортация": {
                    player.teleport(getLocation(chest, 0));
                    break;
                }
                case "ЗапусВверх": {
                    double vector = getNumber(chest, 0) / 2;
                    if (vector > 256) vector = 256;
                    player.setVelocity(new Vector(0, vector, 0));
                    break;
                }
                case "ЗапусВперёд": {
                    double vector = getNumber(chest, 0) / 2;
                    if (vector > 256) vector = 256;
                    player.setVelocity(player.getLocation().getDirection().multiply(vector));
                    break;
                }
                case "ЗапусПоТраектории": {
                    double vector1 = getNumber(chest, 0) / 2;
                    if (vector1 > 256) vector1 = 256;
                    double vector2 = getNumber(chest, 1) / 2;
                    if (vector2 > 256) vector2 = 256;
                    double vector3 = getNumber(chest, 2) / 2;
                    if (vector3 > 256) vector3 = 256;
                    player.setVelocity(new Vector(vector1, vector2, vector3));
                    break;
                }
                case "НаCпавн": {
                    WorldManager.teleportToLobby(player);
                    break;
                }
            }
        }
        if (command[0].startsWith("ПараметрыИгры") && command[0].contains("_")) {
            switch (command[0].split("_")[1]) {
                case "ВклPvP": {
                    world.setPVP(true);
                    break;
                }
                case "ВыклPvP": {
                    world.setPVP(false);
                    break;
                }
                case "Сложность": {
                    int d = (int) getNumber(chest, 0);
                    if (d == 0) world.setDifficulty(Difficulty.PEACEFUL);
                    if (d == 1) world.setDifficulty(Difficulty.EASY);
                    if (d == 2) world.setDifficulty(Difficulty.NORMAL);
                    if (d == 3) world.setDifficulty(Difficulty.HARD);
                    break;
                }
                case "ЛутСМобовВкл": {
                    world.setGameRuleValue("doMobLoot", "true");
                    break;
                }
                case "ЛутСМобовВыкл": {
                    world.setGameRuleValue("doMobLoot", "false");
                    break;
                }
                case "УстБлок": {
                    Location location = getLocation(chest, 0);
                    ItemStack item = getItems(chest)[1];
                    if (location == null) {
                        reportError("Местоположение не настроенно!", chest.getLocation());
                        return;
                    }
                    if (item != null && !item.getType().isBlock()) {
                        reportError("Невозможно поставить предмет как блок", chest.getLocation());
                    }

                    world.getBlockAt(location).setType(item != null ? item.getType() : Material.AIR);
                    if (item != null) world.getBlockAt(location).setData((byte) item.getDurability());
                    break;
                }
                case "ВыпадБлоковВкл": {
                    world.setGameRuleValue("doTileDrops", "true");
                    break;
                }
                case "ВыпадБлоковВыкл": {
                    world.setGameRuleValue("doTileDrops", "false");
                    break;
                }
                case "ОтменитьСобытие": {
                    Coding.canceledEvents.add(eventid);
                    break;
                }
                case "ЗакрытьБезСохр": {
                    server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        MainListener.unloadwithoutsave.add(world);
                        if (owner != null && owner.isOnline()) {
                            WorldManager.teleportToLobby(owner);
                        } else {
                            world.getPlayers().forEach(WorldManager::teleportToLobby);
                        }
                    });
                    break;
                }
            }
        }
    }

    private String getText(Chest chest, int slot) {
        ItemStack item = getItems(chest)[slot];
        return item != null ? item.getItemMeta().getDisplayName().replaceFirst("§f", "") : " ";
    }

    private double getNumber(Chest chest, int slot) {
        if (chest == null) {
            return -1;
        }
        ItemStack item = getItems(chest)[slot];
        if (item != null && item.getType() == Material.SLIME_BALL) {
            String number = item.getItemMeta().getDisplayName().replace("§f", "");
            try {
                if (number.endsWith("s")) return Double.parseDouble(number.replace("s", "")) * 20;
                if (number.endsWith("m")) return Double.parseDouble(number.replace("m", "")) * 1200;
                if (number.endsWith("h")) return Double.parseDouble(number.replace("h", "")) * 72000;
                if (number.endsWith("d")) return Double.parseDouble(number.replace("s", "")) * 1728000;
                return Double.parseDouble(number);
            } catch (NumberFormatException ex) {
                reportError("Не удалость распознать число", chest.getLocation());
            }
        }
        return -1;
    }

    private Location getLocation(Chest chest, int slot) {
        if (chest == null) return world.getSpawnLocation().add(0.5, 0, 0.5);
        ItemStack item = getItems(chest)[slot];
        if (item != null && item.getType() == Material.PAPER) {
            String fullcords = item.getItemMeta().getDisplayName().replace("§f", "").replace("§r", "");
            if (fullcords == null) return world.getSpawnLocation().add(0.5, 0, 0.5);
            String[] cords = fullcords.split(" ");
            return new Location(world, Double.parseDouble(cords[0]), Double.parseDouble(cords[1]), Double.parseDouble(cords[2]), Float.parseFloat(cords[3]), Float.parseFloat(cords[4]));
        }
        return world.getSpawnLocation().add(0.5, 0, 0.5);
    }

    private ItemStack[] getItems(Chest chest) {
        if (chest == null) return null;
        return chest.getBlockInventory().getContents();
    }

    public void reportError(String string, Location location) {
        if (location != null) {
            location = location.clone().add(0.5, 0, 1.5);
            location.setYaw(180);
            if (location.getBlock().getType() == Material.SIGN) {
                location.setPitch(40);
            } else {
                location.setPitch(0);
            }
        }
        TextComponent text = new TextComponent("§c§lОШИБКА! §r" + string + (location != null ? " §f§o(Кликни, чтобы телепортироваться)" : ""));
        if (location != null) {
            String cmd = "/fixcode " + location.getX() + " " + location.getY() + " " + location.getZ() + " " + location.getYaw() + " " + location.getPitch();
            text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
            text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(cmd).create()));
        }
        owner.sendMessage(text);
        owner.playSound(owner.getLocation(), Sound.ENTITY_CAT_AMBIENT, Integer.MAX_VALUE, 0.5f);
    }

    public void broadcastMessage(String string) {
        world.getPlayers().forEach(player -> player.sendMessage(string));
    }
}
