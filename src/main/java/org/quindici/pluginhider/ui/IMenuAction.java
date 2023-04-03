package org.quindici.pluginhider.ui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public interface IMenuAction {
    void click(Player var1, ItemStack var2, ClickType var3);
}
