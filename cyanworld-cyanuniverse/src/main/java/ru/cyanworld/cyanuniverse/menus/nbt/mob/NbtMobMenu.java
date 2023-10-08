package ru.cyanworld.cyanuniverse.menus.nbt.mob;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.cyanworld.cyan1dex.api.CustomMenu;
import ru.cyanworld.cyan1dex.api.ItemBuilder;

import static ru.cyanworld.cyanuniverse.CyanUniverse.server;

public class NbtMobMenu extends CustomMenu {
    public static ItemStack[] contents;
    private Entity entity;

    public NbtMobMenu(Entity entity) {
        super(server, 3, "Редактор NBT - Моб");
        this.entity = entity;
        initItems();
    }

    public void initItems() {
        if (contents != null) {
            this.inventory.setContents(contents);
        } else {
            this.inventory.setItem(3, new ItemBuilder(Material.BOOK).name("Имя").build());
            contents = this.inventory.getContents();
        }

        calculateUnbreakable();

    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getClickedInventory().getHolder() != this) return;
        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) return;
        Player player = (Player) event.getWhoClicked();
        switch (item.getType()) {
            case BOOK: {

                break;
            }
        }
    }

    public void calculateUnbreakable() {

    }
}

