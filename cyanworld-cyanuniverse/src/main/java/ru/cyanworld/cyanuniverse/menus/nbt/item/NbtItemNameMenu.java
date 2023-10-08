package ru.cyanworld.cyanuniverse.menus.nbt.item;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.cyanworld.cyan1dex.api.CustomMenu;
import ru.cyanworld.cyan1dex.api.ItemBuilder;

import static ru.cyanworld.cyanuniverse.CyanUniverse.server;

public class NbtItemNameMenu extends CustomMenu {
    public static ItemStack[] contents;
    private ItemBuilder itemBuilder;

    public NbtItemNameMenu(ItemBuilder itemBuilder) {
        super(server, 1, "Редактор NBT - Предмет: Название");
        this.itemBuilder = itemBuilder;
        initItems();
    }

    public void initItems() {
        if (contents != null) {
            this.inventory.setContents(contents);
        } else {
            ItemStack glass = new ItemBuilder(Material.STAINED_GLASS_PANE).dura(7).name(" ").build();
            ItemStack table = new ItemBuilder(Material.WORKBENCH).name("Сохранить").build();
            for (int i = 1; i < 8; i++) {
                this.inventory.setItem(i, glass);
            }
            this.inventory.setItem(8, table);
            contents = this.inventory.getContents();
        }

        String name = itemBuilder.itemMeta.getDisplayName();
        if (name != null) {
            this.inventory.setItem(0, new ItemBuilder(Material.BOOK).name(name).build());
        }
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        if (item != null) {
            switch (item.getType()) {
                case STAINED_GLASS_PANE: {
                    event.setCancelled(true);
                    break;
                }
                case WORKBENCH: {
                    event.setCancelled(true);
                    ItemStack textitem = this.inventory.getItem(0);
                    if (textitem == null || textitem.getType() == Material.AIR) {
                        itemBuilder.itemMeta.setDisplayName(null);
                        player.openInventory(new NbtItemMenu(itemBuilder).getInventory());
                    } else {
                        itemBuilder.itemMeta.setDisplayName(textitem.getItemMeta().getDisplayName());
                        player.openInventory(new NbtItemMenu(itemBuilder).getInventory());
                    }
                    break;
                }
                case BOOK: {
                    if (event.getClickedInventory().getHolder() == this) {
                        item.setAmount(0);
                    } else {
                        ItemStack book = item.clone();
                        book.setAmount(1);
                        this.inventory.setItem(0, book);
                    }
                }
            }
        }
    }
}
