package ru.cyanworld.cyan1dex;

import ru.cyanworld.cyan1dex.utils.ChatUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RebootManager {
    public static int timer = 900;

    public RebootManager() {
        if (!Config.reboot_enable) {
            return;
        }
        Cyan1dex.server.getScheduler().runTaskTimerAsynchronously(Cyan1dex.instance, this::testForTime, 0, 1200);
    }

    public static void reboot() {
        if (Config.isMainServer) {
            Cyan1dex.server.getOnlinePlayers().forEach(players -> {
                        players.sendTitle("", "Сервер перезагружается...", 0, 60, 10);
                    }
            );
        } else {
            Cyan1dex.server.getOnlinePlayers().forEach(players -> {
                        players.sendTitle("", "Сервер перезагружается...", 0, 60, 10);
                        Cyan1dex.server.getScheduler().scheduleSyncDelayedTask(Cyan1dex.instance, () -> {
                                    BungeeBridge.connectPlayer(players, "main");
                                }
                                , 20);
                    }
            );
        }
        Cyan1dex.server.getScheduler().scheduleSyncDelayedTask(Cyan1dex.instance, () -> {
                    Cyan1dex.server.shutdown();
                }
                , 60);
    }

    public void testForTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        String time = sdf.format(new Date());
        if (time.equals(Config.reboot_time)) {
            Cyan1dex.server.getScheduler().runTaskTimer(Cyan1dex.instance, this::tickForRestart, 0, 20);
        }
    }

    public void tickForRestart() {
        if (timer == 900) {
            ChatUtils.broadcastTitle(" ", "Перезагрузка через 15 минут", 10, 60, 10);
            Cyan1dex.server.broadcastMessage("Перезагрузка через 15 минут");
        }
        if (timer == 600) {
            ChatUtils.broadcastTitle(" ", "Перезагрузка через 10 минут", 10, 60, 10);
            Cyan1dex.server.broadcastMessage("Перезагрузка через 10 минут");
        }
        if (timer == 300) {
            ChatUtils.broadcastTitle(" ", "Перезагрузка через 5 минут", 10, 60, 10);
            Cyan1dex.server.broadcastMessage("Перезагрузка через 5 минут");
        }
        if (timer == 60) {
            ChatUtils.broadcastTitle(" ", "Перезагрузка через 1 минуту", 10, 60, 10);
            Cyan1dex.server.broadcastMessage("Перезагрузка через 1 минуту");
        }
        if (timer == 30) {
            ChatUtils.broadcastTitle(" ", "Перезагрузка через 30 секунд", 10, 60, 10);
            Cyan1dex.server.broadcastMessage("Перезагрузка через 30 секунд");
        }
        if (timer == 15) {
            ChatUtils.broadcastTitle(" ", "Перезагрузка через 15 секунд", 10, 60, 10);
            Cyan1dex.server.broadcastMessage("Перезагрузка через 15 секунд");
        }
        if (timer == 10) {
            ChatUtils.broadcastTitle(" ", "Перезагрузка через 10 секунд", 10, 60, 10);
            Cyan1dex.server.broadcastMessage("Перезагрузка через 10 секунд");
        }
        if (timer <= 5) {
            ChatUtils.broadcastTitle(" ", "Перезагрузка через " + timer + " секунд", 0, 60, 10);
            Cyan1dex.server.broadcastMessage("Перезагрузка через " + timer + " секунд");
        }
        if (timer <= 0) {
            RebootManager.reboot();
        }
        --timer;
    }
}
