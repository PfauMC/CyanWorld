package ru.cyanworld.cyan1dex.api;

import com.google.common.collect.ForwardingMultimap;
import com.google.common.collect.Maps;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CustomItem
        extends ItemStack {
    public static Map<String, CustomItem> itemMap = Maps.newHashMap();

    public CustomItem(Material material) {
        super(new ItemStack(material));
    }

    public CustomItem(ItemStack itemStack) {
        super(itemStack);
    }

    public CustomItem(ItemStack itemStack, String name) {
        super(itemStack);
        this.name(name);
    }

    public CustomItem(ItemStack itemStack, String name, String id) {
        super(itemStack);
        this.name(name);
        this.id(id);
    }

    public static CustomItem getByStack(ItemStack itemStack) {
        if (!CustomItem.isHasLocName(itemStack)) {
            return null;
        }
        return CustomItem.getById(itemStack.getItemMeta().getLocalizedName());
    }

    public static boolean isHasLocName(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        if (!itemStack.hasItemMeta()) {
            return false;
        }
        return itemStack.getItemMeta().hasLocalizedName();
    }

    public static CustomItem getById(String id) {
        return itemMap.get(id);
    }

    public void register() {
        if (!CustomItem.isHasLocName(this)) {
            return;
        }
        itemMap.put(this.getItemMeta().getLocalizedName(), this);
    }

    public CustomItem amount(int amount) {
        this.setAmount(amount);
        return this;
    }

    public CustomItem durability(int durability) {
        return this.durability((short) durability);
    }

    public CustomItem durability(short durability) {
        this.setDurability(durability);
        return this;
    }

    public CustomItem name(String name) {
        if (name == null) {
            return this;
        }
        ItemMeta meta = this.getItemMeta();
        meta.setDisplayName("\u00a7r" + name);
        this.setItemMeta(meta);
        return this;
    }

    public CustomItem id(String id) {
        if (id == null) {
            return this;
        }
        ItemMeta meta = this.getItemMeta();
        meta.setLocalizedName(id);
        this.setItemMeta(meta);
        return this;
    }

    public CustomItem lore(String[] lore) {
        return this.lore(Arrays.asList(lore));
    }

    public CustomItem lore(List<String> lore) {
        if (lore == null) {
            return this;
        }
        ItemMeta meta = this.getItemMeta();
        meta.setLore(lore);
        this.setItemMeta(meta);
        return this;
    }

    public CustomItem ench(Enchantment enchantment, int lvl) {
        this.addUnsafeEnchantment(enchantment, lvl);
        return this;
    }

    public CustomItem ench(String ench) {
        this.addUnsafeEnchantment(Enchantment.getByName(ench.split("-")[0]), Integer.parseInt(ench.split("-")[1]));
        return this;
    }

    public CustomItem ench(String[] ench) {
        for (int i = 0; i < ench.length; ++i) {
            this.addUnsafeEnchantment(Enchantment.getByName(ench[i].split("-")[0]), Integer.parseInt(ench[i].split("-")[1]));
        }
        return this;
    }

    public CustomItem skull(Player player) {
        if (this.getType() != Material.SKULL_ITEM) {
            return this;
        }
        SkullMeta skullMeta = (SkullMeta) this.getItemMeta();
        skullMeta.setOwner(player.getName());
        this.setItemMeta(skullMeta);
        return this;
    }

    public CustomItem skull(String texture) {
        if (this.getType() != Material.SKULL_ITEM) {
            return this;
        }
        SkullMeta skullMeta = (SkullMeta) this.getItemMeta();
        this.loadProfile(skullMeta, texture);
        this.setItemMeta(skullMeta);
        return this;
    }

    public CustomItem unbreakable(boolean value) {
        ItemMeta meta = this.getItemMeta();
        meta.setUnbreakable(value);
        this.setItemMeta(meta);
        return this;
    }

    public CustomItem armorColor(Color color) {
        LeatherArmorMeta meta = (LeatherArmorMeta) this.getItemMeta();
        meta.setColor(color);
        this.setItemMeta(meta);
        return this;
    }

    public CustomItem hideFlags(ItemFlag[] itemFlag) {
        ItemMeta meta = this.getItemMeta();
        meta.addItemFlags(itemFlag);
        this.setItemMeta(meta);
        return this;
    }

    public CustomItem hideFlags(ItemFlag itemFlag) {
        ItemMeta meta = this.getItemMeta();
        meta.addItemFlags(itemFlag);
        this.setItemMeta(meta);
        return this;
    }

    private void loadProfile(ItemMeta meta, String url) {
        Class profileClass = Reflection.getClass("com.mojang.authlib.GameProfile");
        Constructor profileConstructor = Reflection.getDeclaredConstructor(profileClass, UUID.class, String.class);
        Object profile = Reflection.newInstance(profileConstructor, UUID.randomUUID(), null);
        byte[] encodedData = url.startsWith("http://") ? Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes()) : url.getBytes();
        Method getPropertiesMethod = Reflection.getDeclaredMethod(profileClass, "getProperties");
        Object propertyMap = Reflection.invoke(getPropertiesMethod, profile);
        Class propertyClass = Reflection.getClass("com.mojang.authlib.properties.Property");
        Reflection.invoke(Reflection.getDeclaredMethod(ForwardingMultimap.class, "put", Object.class, Object.class), propertyMap, "textures", Reflection.newInstance(Reflection.getDeclaredConstructor(propertyClass, String.class, String.class), "textures", new String(encodedData)));
        Reflection.setField("profile", meta, profile);
    }

    private static final class Reflection {
        private Reflection() {
        }

        private static Class<?> getClass(String forName) {
            try {
                return Class.forName(forName);
            } catch (ClassNotFoundException e) {
                return null;
            }
        }

        private static /* varargs */ <T> Constructor<T> getDeclaredConstructor(Class<T> clazz, Class<?>... params) {
            try {
                return clazz.getDeclaredConstructor(params);
            } catch (NoSuchMethodException e) {
                return null;
            }
        }

        private static /* varargs */ <T> T newInstance(Constructor<T> constructor, Object... params) {
            try {
                return constructor.newInstance(params);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                return null;
            }
        }

        private static /* varargs */ Method getDeclaredMethod(Class<?> clazz, String name, Class<?>... params) {
            try {
                return clazz.getDeclaredMethod(name, params);
            } catch (NoSuchMethodException e) {
                return null;
            }
        }

        private static /* varargs */ Object invoke(Method method, Object object, Object... params) {
            method.setAccessible(true);
            try {
                return method.invoke(object, params);
            } catch (IllegalAccessException | InvocationTargetException e) {
                return null;
            }
        }

        private static void setField(String name, Object instance, Object value) {
            Field field = Reflection.getDeclaredField(instance.getClass(), name);
            field.setAccessible(true);
            try {
                field.set(instance, value);
            } catch (IllegalAccessException illegalAccessException) {
                // empty catch block
            }
        }

        private static Field getDeclaredField(Class<?> clazz, String name) {
            try {
                return clazz.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                return null;
            }
        }
    }

}
