package org.quindici.pluginhider.utils;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemBuilder {
    private Material material;
    private String title;
    private List<String> lore = Lists.newArrayList();
    private int amount = 1;

    public ItemBuilder(Material material) {
        this.material = material;
    }

    public org.quindici.pluginhider.utils.ItemBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public org.quindici.pluginhider.utils.ItemBuilder setLore(String... lore) {
        return this.setLore((List)Lists.newArrayList(lore));
    }

    public org.quindici.pluginhider.utils.ItemBuilder setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemStack build() {
        ItemStack item = new ItemStack(this.material);
        ItemMeta meta = item.getItemMeta();
        if (this.title != null) {
            meta.setDisplayName(this.title);
        }

        if (this.lore != null) {
            meta.setLore(this.lore);
        }

        if (this.amount != 0) {
            item.setAmount(this.amount);
        }

        item.setItemMeta(meta);
        return item;
    }
}
