package ru.cyanworld.cyanuniverse.menus.coding.variables;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.cyanworld.cyan1dex.Utils;
import ru.cyanworld.cyan1dex.api.CustomMenu;
import ru.cyanworld.cyan1dex.api.ItemBuilder;

import java.util.Arrays;

import static ru.cyanworld.cyanuniverse.CyanUniverse.server;

public class VariableMenu extends CustomMenu {
    public static ItemStack[] contents;

    public VariableMenu() {
        super(server, 1, "Переменные");
        initItems();
    }

    public void initItems() {
        if (contents != null) {
            this.inventory.setContents(contents);
        } else {
            this.inventory.setItem(0, new ItemBuilder(Material.BOOK).name("§bТекст").lore(Arrays.asList("Напиши в чате текст,", "держа в руке этот предмет")).build());
            this.inventory.setItem(1, new ItemBuilder(Material.SLIME_BALL).name("§eЧисло").locname("ru.cyanworld.cyanuniverse.coding.number").lore(Arrays.asList("Напиши в чате число,", "держа в руке этот предмет")).build());
            this.inventory.setItem(2, new ItemBuilder(Material.GLASS_BOTTLE).name("§dЭффект").locname("ru.cyanworld.cyanuniverse.coding.effect").lore(Arrays.asList("Кликни по этому предмету,", "чтобы выбрать эффект")).build());
            this.inventory.setItem(3, new ItemBuilder(Material.PAPER).name("§aМестоположение").locname("ru.cyanworld.cyanuniverse.coding.location").lore(Arrays.asList("§7ЛКМ - телепортироваться в обычный мир", "§7ПКМ - поставить местоположение")).build());
            this.inventory.setItem(4, new ItemBuilder(new ItemStack(Material.MAGMA_CREAM)).name("§cПеременная").locname("ru.cyanworld.cyanuniverse.coding.variable").lore(Arrays.asList("%player%  другие")).build()
            );
            contents = this.inventory.getContents();
        }
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) return;
        if (event.getClickedInventory().getHolder() != this) return;
        Player player = (Player) event.getWhoClicked();
        switch (item.getType()) {
            case GLASS_BOTTLE: {
                player.openInventory(new EffectMenu().getInventory());
                break;
            }
            default: {
                player.getInventory().addItem(item);
                Utils.playCustomSound("ok", player);
            }
        }
    }
}
