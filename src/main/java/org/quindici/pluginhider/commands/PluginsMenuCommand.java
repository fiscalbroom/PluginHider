package org.quindici.pluginhider.commands;

import me.xanium.noplugin.gui.PM1;
import me.xanium.noplugin.utils.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.help.HelpTopic;
import org.quindici.pluginhider.PluginHider;

import java.util.Iterator;

public class PluginsMenuCommand implements CommandExecutor {
    private final PluginHider plugin;

    public PluginsMenuCommand(PluginHider plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String s123, String[] args) {
        Iterator var5 = Bukkit.getHelpMap().getHelpTopics().iterator();

        while(var5.hasNext()) {
            HelpTopic test = (HelpTopic)var5.next();
            PluginHider.debug(test.getName());
        }

        if (!(sender instanceof Player)) {
            return true;
        } else {
            Player player = (Player)sender;
            if (!player.hasPermission("pluginhider.command.pluginsmenu")) {
                sender.sendMessage(UtilMessage.colorize(this.plugin.getMessageManager().getNoPermission()));
                return true;
            } else {
                (new PM1()).open(player);
                return true;
            }
        }
    }
}
