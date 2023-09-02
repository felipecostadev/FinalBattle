package br.com.felipecostadev.prototype.api.item;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftMetaBook;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ItemBuilder {

    private ItemStack itemStack;

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public ItemBuilder(Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    public ItemBuilder(Material material, int amount, byte durability) {
        this(new ItemStack(material, amount, durability));
    }

    public ItemBuilder changeItem(Consumer<ItemStack> consumer) {
        consumer.accept(itemStack);
        return this;
    }

    public ItemBuilder changeMeta(Consumer<ItemMeta> consumer) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        consumer.accept(itemMeta);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder name(String name) {
        return changeMeta(itemMeta -> itemMeta.setDisplayName(name));
    }

    public ItemBuilder amount(int amount) {
        return changeItem(itemStack -> itemStack.setAmount(amount));
    }

    public ItemBuilder durability(byte durability) {
        return changeItem(itemStack -> itemStack.setDurability(durability));
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        return changeMeta(itemMeta -> itemMeta.spigot().setUnbreakable(unbreakable));
    }

    public ItemBuilder color(Color color) {
        return changeMeta(itemMeta -> ((LeatherArmorMeta) itemMeta).setColor(color));
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        return changeItem(itemStack -> itemStack.addUnsafeEnchantment(enchantment, level));
    }

    public ItemBuilder removeEnchant(Enchantment enchantment) {
        return changeItem(itemStack -> itemStack.removeEnchantment(enchantment));
    }

    public ItemBuilder addFlag(ItemFlag... itemFlags) {
        return changeMeta(itemMeta -> itemMeta.addItemFlags(itemFlags));
    }

    public ItemBuilder removeFlag(ItemFlag... itemFlags) {
        return changeMeta(itemMeta -> itemMeta.removeItemFlags(itemFlags));
    }

    public ItemBuilder addPage(IChatBaseComponent component) {
        return changeMeta(itemMeta -> {
            BookMeta bookMeta = (BookMeta)itemMeta;
            try {
                List<IChatBaseComponent> pages = (List<IChatBaseComponent>) CraftMetaBook.class.getDeclaredField("pages").get(bookMeta);
                pages.add(component);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Deprecated
    public ItemBuilder headTexture(String value) {
        Bukkit.getUnsafe().modifyItemStack(this.itemStack, "{SkullOwner:{Id:" + new UUID(value.hashCode(), value.hashCode()) + ",Properties:{textures:[{Value:" + value + "}]}}}");
        return this;
    }

    public ItemBuilder lore(String... description) {
        return changeMeta(itemMeta -> itemMeta.setLore(Arrays.asList(description)));
    }

    public ItemBuilder loreCompact(String description) {
        return changeMeta(itemMeta -> {
            List<String> lore = new ArrayList<>();
            String color = "";
            String text = ChatColor.translateAlternateColorCodes('&', description);
            String[] loreSplit = description.split(" ");
            text = "";
            for (int index = 0; index < loreSplit.length; index++) {
                if (ChatColor.stripColor(text).length() >= 30 || ChatColor.stripColor(text).endsWith(".") || ChatColor.stripColor(text).endsWith("!")) {
                    lore.add(text);
                    if (text.endsWith(".") || text.endsWith("!"))
                        lore.add("");
                    text = color;
                }
                String toAdd = loreSplit[index];
                if (toAdd.contains("§"))
                    color = ChatColor.getLastColors(toAdd.toLowerCase());
                if (toAdd.contains("\n")) {
                    toAdd = toAdd.substring(0, toAdd.indexOf("\n"));
                    loreSplit[index] = loreSplit[index].substring(toAdd.length() + 1);
                    lore.add(text + ((text.length() == 0) ? "" : " ") + toAdd);
                    text = color;
                    index--;
                } else {
                    text = text + ((ChatColor.stripColor(text).length() == 0) ? "" : " ") + toAdd;
                }
            }
            lore.add(text);
            itemMeta.setLore(lore);
        });
    }

    public ItemStack build() {
        return itemStack.clone();
    }
}