package ru.cyanworld.cyanuniverse.menus.nbt.item;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.cyanworld.cyan1dex.api.CustomMenu;
import ru.cyanworld.cyan1dex.api.ItemBuilder;

import java.util.ArrayList;
import java.util.List;

import static ru.cyanworld.cyanuniverse.CyanUniverse.server;

public class NbtItemLore extends CustomMenu {
    public static ItemStack[] contents;
    private ItemBuilder itemBuilder;

    public NbtItemLore(ItemBuilder itemBuilder) {
        super(server, 1, "Редактор NBT - Предмет: Описание");
        this.itemBuilder = itemBuilder;
        initItems();
    }

    public void initItems() {
        if (contents != null) {
            this.inventory.setContents(contents);
        } else {
            this.inventory.setItem(7, new ItemBuilder(Material.STAINED_GLASS_PANE).dura(7).name(" ").build());
            this.inventory.setItem(8, new ItemBuilder(Material.WORKBENCH).name("Сохранить").build());
            contents = this.inventory.getContents();
        }

        List<String> lore = itemBuilder.itemMeta.getLore();
        if (lore != null && !lore.isEmpty()) {
            for (int i = 0; i < lore.size(); i++) {
                if (!lore.get(i).equals(" "))
                    this.inventory.setItem(i, new ItemBuilder(Material.BOOK).name(lore.get(i)).build());
            }
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
                    List<String> lore = new ArrayList<>();
                    for (int i = 0; i < 7; i++) {
                        ItemStack line = this.inventory.getItem(i);
                        if (line != null && line.getType() != Material.AIR && line.getItemMeta().getDisplayName() != null) {
                            lore.add(line.getItemMeta().getDisplayName());
                        } else {
                            lore.add(" ");
                        }
                    }
                    if (!lore.isEmpty()) {
                        for (int i = lore.size() - 1; i > 0; i--) {
                            if (lore.get(i).equals(" ")) {
                                lore.remove(i);
                            } else {
                                break;
                            }
                        }
                        itemBuilder.itemMeta.setLore(lore);
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
                        for (int i = 0; i < 7; i++) {
                            ItemStack checkitem = this.inventory.getItem(i);
                            if (checkitem == null || checkitem.getType() == Material.AIR) {
                                this.inventory.setItem(i, book);
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
    }
}