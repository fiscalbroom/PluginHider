package org.quindici.pluginhider.listeners;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.quindici.pluginhider.PluginHider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommandProtection implements Listener {
    private PluginHider plugin;

    public CommandProtection(PluginHider plugin) {
        this.plugin = plugin;
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onCommandSend(PlayerCommandSendEvent event) {
        Player p = event.getPlayer();
        if (this.plugin.getSettingsManager().isOperatorBypass()) {
            if (p.hasPermission("pluginhider.bypass") && p.isOp()) {
                return;
            }
        } else if (p.hasPermission("pluginhider.bypass") && !p.isOp()) {
            return;
        }

        if (this.plugin.getSettingsManager().hasCleanCommandList()) {
            event.getCommands().clear();
        } else {
            ArrayList cleanup;
            Iterator var4;
            String plugin;
            if (this.plugin.getSettingsManager().isCleanCommandList()) {
                cleanup = new ArrayList();
                var4 = this.plugin.getSettingsManager().getBlockedPlugins().iterator();

                while(var4.hasNext()) {
                    plugin = (String)var4.next();
                    Iterator var6 = event.getCommands().iterator();

                    while(var6.hasNext()) {
                        String command = (String)var6.next();
                        if (command.startsWith(plugin.toLowerCase())) {
                            cleanup.add(command);
                        }
                    }
                }

                event.getCommands().removeAll(cleanup);
            }

            cleanup = new ArrayList();
            var4 = this.plugin.getSettingsManager().getPluginCommands().values().iterator();

            while(var4.hasNext()) {
                List<Command> commands = (List)var4.next();
                ArrayList finalCleanup = cleanup;
                commands.forEach((commandx) -> {
                    finalCleanup.add(commandx.getName());
                    finalCleanup.addAll(commandx.getAliases());
                });
            }

            event.getCommands().removeAll(cleanup);
            var4 = this.plugin.getSettingsManager().getRemovedCommands().iterator();

            while(var4.hasNext()) {
                plugin = (String)var4.next();
                event.getCommands().remove(plugin);
            }

        }
    }
}