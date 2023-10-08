package ru.cyanworld.cyan1dex;

import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import ru.cyanworld.cyan1dex.wrapper.WrapperPlayServerWorldParticles;

import java.util.ArrayList;
import java.util.List;

public class EffectManager {
    public List<Player> angelWings_list = new ArrayList<>();

    public void angelWings() {
        Cyan1dex.server.getScheduler().runTaskTimer(Cyan1dex.instance, () -> this.angelWings_list.forEach(player -> {
                            Location l = player.getLocation();
                            l.setPitch(0.0f);
                            World w = l.getWorld();
                            Vector direction = l.getDirection();
                            double offset = -0.35;
                            double x = l.getX();
                            double y = l.getY() + 1.0;
                            double z = l.getZ();
                            Location test1 = new Location(w, x, y, z).add(direction.multiply(offset));
                            Cyan1dex.world.spawnParticle(Particle.REDSTONE, test1, 1);


                            Location test2 = new Location(w, x, y, z).add(direction.multiply(offset));
                            WrapperPlayServerWorldParticles packet2 = new WrapperPlayServerWorldParticles();
                            packet2.setParticleType(EnumWrappers.Particle.REDSTONE);
                            packet2.setX((float) test2.getX());
                            packet2.setY((float) test2.getY());
                            packet2.setZ((float) test2.getZ());
                            Cyan1dex.world.getPlayers().forEach(packet2::sendPacket);

                        }
                )
                , 0, 1);
    }
}
