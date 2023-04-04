package org.quindici.pluginhider.commands;

import org.quindici.pluginhider.utils.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.quindici.pluginhider.PluginHider;

import java.util.ArrayList;
import java.util.List;

public class PluginListCommand implements CommandExecutor {
    private final PluginHider plugin;

    public PluginListCommand(PluginHider plugin) {
        this.plugin = plugin;
    }

    private boolean hasAccess(CommandSender sender) {
        if (this.plugin.getSettingsManager().isOperatorBypass()) {
            return sender.hasPermission("pluginhider.command.list") && sender.isOp();
        } else {
            return sender.hasPermission("pluginhider.command.list") && !sender.isOp();
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd1254, String s2436, String[] args) {
        if (!this.hasAccess(sender)) {
            sender.sendMessage(UtilMessage.colorize(this.plugin.getMessageManager().getNoPermission()));
            return true;
        } else {
            List<String> toPrint = new ArrayList();
            Plugin[] var6 = Bukkit.getPluginManager().getPlugins();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                Plugin p = var6[var8];
                PluginDescriptionFile file = p.getDescription();
                toPrint.add("-> " + file.getFullName() + " autori(s) - " + file.getAuthors());
            }

            toPrint.forEach((s) -> {
                this.plugin.getLogger().info(s);
            });
            sender.sendMessage("§8[§6PluginHider§8] §eStampato. Controlla la console");
            return true;
        }
    }
}