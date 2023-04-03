package org.quindici.pluginhider.commands;

import me.xanium.noplugin.utils.UtilMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.quindici.pluginhider.PluginHider;

public class ReloadCommand implements CommandExecutor {
    private final PluginHider plugin;

    public ReloadCommand(PluginHider plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String s1241, String[] args) {
        if (!sender.hasPermission("pluginhider.reload")) {
            sender.sendMessage(UtilMessage.colorize(this.plugin.getMessageManager().getNoPermission()));
            return true;
        } else {
            this.plugin.reloadConfig();
            this.plugin.getSettingsManager().reload();
            this.plugin.getMessageManager().reload();
            sender.sendMessage("§8[§6PluginHider§8] §eRicaricato.");
            return true;
        }
    }
}

