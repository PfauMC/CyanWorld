package ru.cyanworld.cyan1dex.api;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import ru.cyanworld.cyan1dex.Cyan1dex;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Debugger {
    public static List<Long> timings = new ArrayList<Long>();

    public static void timings(long time1, long time2) {
        long time = time2 - time1;
        timings.add(time);
        long summ = 0;
        Iterator<Long> iterator = timings.iterator();
        while (iterator.hasNext()) {
            long i = iterator.next();
            summ += i;
        }
        double average = summ / (long) timings.size();
        String result = "Время: " + time + "; Среднее: " + average;
        System.out.println(result);
    }

    public static void tpToLoc(Location location, String debugmsg) {
        String text = debugmsg + " | " + location.getBlockX() + ";" + location.getBlockY() + ";" + location.getBlockZ();
        System.out.println(text);
        TextComponent msg = new TextComponent(text);
        msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ()));
        Cyan1dex.server.getOnlinePlayers().forEach(Player2 -> {
                    Player2.sendMessage(msg);
                }
        );
    }

    public static void main(String[] arg) {
        for (int i = 1000; i < 1010; ++i) {
            int result = i % 3;
            System.out.println("Блок: " + i + " Результат: " + result);
        }
    }
}
