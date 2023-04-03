package org.quindici.pluginhider.ui;

import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public abstract class Menu {
    private static Map<UUID, UUID> openInventories = Maps.newHashMap();
    private static Map<UUID, org.quindici.pluginhider.ui.Menu> inventoryByUuid = Maps.newHashMap();
    private Map<Integer, org.quindici.pluginhider.ui.IMenuAction> actions = Maps.newHashMap();
    private Inventory inventory;
    private UUID inventoryID;

    public Menu(int slots, String title) {
        this.inventory = Bukkit.createInventory((InventoryHolder)null, slots, title);
        this.inventoryID = UUID.randomUUID();
        inventoryByUuid.put(this.getInventoryID(), this);
    }

    public void setItem(int slot, ItemStack stack, org.quindici.pluginhider.ui.IMenuAction action) {
        this.getInventory().setItem(slot, stack);
        if (action != null) {
            this.actions.put(slot, action);
        }

    }

    public void setItem(int slot, ItemStack stack) {
        this.setItem(slot, stack, (org.quindici.pluginhider.ui.IMenuAction)null);
    }

    public void delete() {
        Iterator var1 = Bukkit.getOnlinePlayers().iterator();

        while(var1.hasNext()) {
            Player p = (Player)var1.next();
            UUID u = (UUID)openInventories.get(p.getUniqueId());
            if (u.equals(this.getInventoryID())) {
                p.closeInventory();
            }
        }

        inventoryByUuid.remove(this.getInventoryID());
    }

    public void open(Player player) {
        player.openInventory(this.getInventory());
        openInventories.put(player.getUniqueId(), this.getInventoryID());
    }

    public void clearSlots() {
        for(int i = 0; i < 45; ++i) {
            this.inventory.clear(i);
        }

    }

    public UUID getInventoryID() {
        return this.inventoryID;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public org.quindici.pluginhider.ui.IMenuAction getAction(int slot) {
        return (org.quindici.pluginhider.ui.IMenuAction)this.actions.get(slot);
    }

    public Map<Integer, IMenuAction> getActions() {
        return this.actions;
    }

    public static Map<UUID, UUID> getOpenInventories() {
        return openInventories;
    }

    public static Map<UUID, org.quindici.pluginhider.ui.Menu> getInventoryByUuid() {
        return inventoryByUuid;
    }
}

