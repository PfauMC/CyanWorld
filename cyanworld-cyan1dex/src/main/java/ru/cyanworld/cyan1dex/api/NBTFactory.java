package ru.cyanworld.cyan1dex.api;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NBTFactory {
    private final net.minecraft.server.v1_12_R1.ItemStack itemStackNMS;
    private final NBTTagCompound nbtTagCompound;
    private ItemStack itemStackBukkit;

    public NBTFactory(ItemStack itemStack) {
        this.itemStackNMS = CraftItemStack.asNMSCopy(itemStack);
        this.nbtTagCompound = this.itemStackNMS.hasTag() ? this.itemStackNMS.getTag() : new NBTTagCompound();
    }

    public NBTTagCompound getNbtTagCompound() {
        return this.nbtTagCompound;
    }

    public ItemStack build(NBTTagCompound nbtTagCompound) {
        this.itemStackNMS.setTag(nbtTagCompound);
        return CraftItemStack.asBukkitCopy(this.itemStackNMS);
    }
}
