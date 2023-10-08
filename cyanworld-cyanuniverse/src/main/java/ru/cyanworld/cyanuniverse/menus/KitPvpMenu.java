package ru.cyanworld.cyanuniverse.menus;

import com.google.common.collect.Maps;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.cyanworld.cyan1dex.Utils;
import ru.cyanworld.cyan1dex.api.CustomItem;
import ru.cyanworld.cyan1dex.api.CustomMenu;

import java.util.Map;
import java.util.UUID;

import static org.bukkit.Material.DIAMOND_CHESTPLATE;
import static ru.cyanworld.cyanuniverse.CyanUniverse.server;

public class KitPvpMenu extends CustomMenu {
    public static Map<UUID, String> map = Maps.newHashMap();
    public static ItemStack helmet_chain = new CustomItem(Material.CHAINMAIL_HELMET).unbreakable(true);
    public static ItemStack helmet_leather = new CustomItem(Material.LEATHER_HELMET).unbreakable(true);

    public static ItemStack chestplate_leather = new CustomItem(Material.LEATHER_CHESTPLATE).unbreakable(true).id("ru.cyanworld.cyanuniverse.kit.mage");
    public static ItemStack chestplate_diamond = new CustomItem(DIAMOND_CHESTPLATE).unbreakable(true).id("ru.cyanworld.cyanuniverse.kit.tank");
    public static ItemStack chestplate_chain = new CustomItem(Material.CHAINMAIL_CHESTPLATE).unbreakable(true).id("ru.cyanworld.cyanuniverse.kit.archer");

    public static ItemStack leggings_chain = new CustomItem(Material.CHAINMAIL_LEGGINGS).unbreakable(true);
    public static ItemStack leggings_leather = new CustomItem(Material.LEATHER_LEGGINGS).unbreakable(true);


    public static ItemStack boots_iron = new CustomItem(Material.IRON_BOOTS).unbreakable(true);
    public static ItemStack boots_chain = new CustomItem(Material.CHAINMAIL_BOOTS).unbreakable(true);
    public static ItemStack boots_diamond = new CustomItem(Material.DIAMOND_BOOTS).unbreakable(true);
    public static ItemStack boots_leather = new CustomItem(Material.LEATHER_BOOTS).unbreakable(true);


    public static ItemStack sword_wood = new CustomItem(Material.WOOD_SWORD).unbreakable(true);
    public static ItemStack sword_stone = new CustomItem(Material.STONE_SWORD).ench(Enchantment.KNOCKBACK, 2).unbreakable(true);
    public static ItemStack sword_diamond = new CustomItem(Material.DIAMOND_SWORD).unbreakable(true);

    public static ItemStack shield = new CustomItem(Material.SHIELD).unbreakable(true);
    public static ItemStack bow = new CustomItem(Material.BOW).unbreakable(true).ench(Enchantment.ARROW_DAMAGE, 1).ench(Enchantment.ARROW_INFINITE, 1);
    public static ItemStack arrow = new ItemStack(Material.ARROW);
    public static ItemStack elytra = new CustomItem(Material.ELYTRA).unbreakable(true).id("ru.cyanworld.cyanuniverse.kit.ninja");
    public static ItemStack end_rod = new CustomItem(Material.END_ROD).ench(Enchantment.DAMAGE_ALL, 5).ench(Enchantment.KNOCKBACK, 2);

    public static PotionEffect invisibility = new PotionEffect(PotionEffectType.INVISIBILITY, 6000, 0);
    public static PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 6000, 0);
    public static PotionEffect healthboost = new PotionEffect(PotionEffectType.REGENERATION, 6000, 1);

    public static PotionEffect fastinv = new PotionEffect(PotionEffectType.INVISIBILITY, 30, 255);
    public static PotionEffect resistance = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 30, 255);

    public KitPvpMenu() {
        super(server, 3, "Выбор набора");
        setupItems();
    }

    public static void giveKit(Player player, Material material) {
        PlayerInventory inv = player.getInventory();
        inv.clear();
        Utils.healthUpPlayer(player);
        player.addPotionEffect(resistance);
        switch (material) {
            case DIAMOND_CHESTPLATE: {
                player.addPotionEffect(fastinv);
                inv.setHelmet(helmet_chain);
                inv.setChestplate(chestplate_diamond);
                inv.setLeggings(leggings_chain);
                inv.setBoots(boots_iron);
                inv.addItem(sword_wood);
                inv.setItemInOffHand(shield);
                break;
            }
            case BOW: {
                player.addPotionEffect(fastinv);
                inv.setHelmet(helmet_chain);
                inv.setChestplate(chestplate_chain);
                inv.setLeggings(leggings_chain);
                inv.setBoots(boots_chain);
                inv.addItem(sword_stone);
                inv.addItem(arrow);
                inv.setItemInOffHand(bow);
                break;
            }
            case DIAMOND_BOOTS: {
                player.addPotionEffect(invisibility);
                player.addPotionEffect(speed);
                inv.setChestplate(elytra);
                inv.setBoots(boots_diamond);
                inv.addItem(sword_diamond);
                break;
            }
            case POTION: {
                player.addPotionEffect(fastinv);
                inv.setHelmet(helmet_leather);
                inv.setChestplate(chestplate_leather);
                inv.setLeggings(leggings_leather);
                inv.setBoots(boots_leather);
                inv.addItem(end_rod);
                player.addPotionEffect(healthboost);
            }
            default: {
                return;
            }
        }
    }

    private void setupItems() {
        inventory.setItem(11, new CustomItem(DIAMOND_CHESTPLATE).name("Танк").hideFlags(ItemFlag.HIDE_ATTRIBUTES));
        inventory.setItem(12, new CustomItem(Material.POTION).name("Маг").hideFlags(ItemFlag.HIDE_POTION_EFFECTS));
        inventory.setItem(13, new CustomItem(Material.DIAMOND_BOOTS).name("Ниндзя").hideFlags(ItemFlag.HIDE_ATTRIBUTES));
        inventory.setItem(14, new CustomItem(Material.BOW).name("Лучник").hideFlags(ItemFlag.HIDE_ATTRIBUTES));
    }

    public Inventory getInv(UUID uuid, String worldname) {
        map.put(uuid, worldname);
        return inventory;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        giveKit(player, item.getType());
        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(player.getWorld().getSpawnLocation().add(0.5, 0, 0.5));
    }
}
