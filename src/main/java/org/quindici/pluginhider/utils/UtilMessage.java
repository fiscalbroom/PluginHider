package org.quindici.pluginhider.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;

public class UtilMessage {
    public UtilMessage() {
    }

    public static void message(CommandSender sender, List<String> messageList) {
        Iterator var2 = messageList.iterator();

        while(var2.hasNext()) {
            String curMessage = (String)var2.next();
            message(sender, colorize(curMessage));
        }

    }

    public static void message(CommandSender sender, String message) {
        if (sender != null) {
            if (sender instanceof Player) {
                sender.sendMessage(colorize(message));
            }
        }
    }

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
