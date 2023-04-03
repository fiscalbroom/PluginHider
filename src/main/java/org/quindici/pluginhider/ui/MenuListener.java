package org.quindici.pluginhider.ui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class MenuListener implements Listener {
    public MenuListener() {
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player player = (Player)e.getWhoClicked();
            UUID playerUUID = player.getUniqueId();
            UUID inventoryUUID = (UUID) org.quindici.pluginhider.ui.Menu.getOpenInventories().get(playerUUID);
            if (inventoryUUID != null) {
                if (e.getClickedInventory() == player.getOpenInventory().getTopInventory()) {
                    e.setCancelled(true);
                    org.quindici.pluginhider.ui.Menu gui = (org.quindici.pluginhider.ui.Menu) org.quindici.pluginhider.ui.Menu.getInventoryByUuid().get(inventoryUUID);
                    org.quindici.pluginhider.ui.IMenuAction action = (IMenuAction)gui.getActions().get(e.getSlot());
                    if (action != null) {
                        action.click(player, e.getCurrentItem(), e.getClick());
                    }
                } else {
                    e.setCancelled(true);
                }
            }

        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player player = (Player)e.getPlayer();
        UUID playerUUID = player.getUniqueId();
        UUID menuID = (UUID) org.quindici.pluginhider.ui.Menu.getOpenInventories().get(playerUUID);
        org.quindici.pluginhider.ui.Menu.getOpenInventories().remove(playerUUID);
        org.quindici.pluginhider.ui.Menu.getInventoryByUuid().remove(menuID);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();
        UUID menuID = (UUID) org.quindici.pluginhider.ui.Menu.getOpenInventories().get(playerUUID);
        org.quindici.pluginhider.ui.Menu.getOpenInventories().remove(playerUUID);
        Menu.getInventoryByUuid().remove(menuID);
    }
}

