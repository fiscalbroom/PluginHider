package org.quindici.pluginhider.backend;

import com.google.common.collect.Maps;
import org.quindici.pluginhider.PluginHider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MessageManager {
    private final PluginHider plugin;
    private String noPermission;
    private String stringCommandMessage;
    private String consoleOnlyMessage;
    private String blockedCommandMessage;
    private final String missingMessage = "&cMessaggio non trovato in config.yml";
    private final Map<String, String> messages;
    private final Map<String, List<String>> listMessages;

    public MessageManager(PluginHider plugin) {
        this.plugin = plugin;
        this.messages = Maps.newHashMap();
        this.listMessages = Maps.newHashMap();
    }

    public void load() {
        this.stringCommandMessage = this.plugin.getConfig().getString("string-command-blocked");
        this.consoleOnlyMessage = this.plugin.getConfig().getString("console-only-message");
        this.noPermission = this.plugin.getConfig().getString("nopermission");
        this.blockedCommandMessage = this.plugin.getConfig().getString("blocked-command-message");
        Iterator var1;
        String key;
        if (this.plugin.getConfig().getConfigurationSection("messages") != null) {
            var1 = this.plugin.getConfig().getConfigurationSection("messages").getKeys(false).iterator();

            while(var1.hasNext()) {
                key = (String)var1.next();
                this.messages.put(key.toLowerCase(), this.plugin.getConfig().getString("messages." + key));
            }
        }

        if (this.plugin.getConfig().getConfigurationSection("list-messages") != null) {
            var1 = this.plugin.getConfig().getConfigurationSection("list-messages").getKeys(false).iterator();

            while(var1.hasNext()) {
                key = (String)var1.next();
                List<String> lines = new ArrayList(this.plugin.getConfig().getStringList("list-messages." + key));
                this.listMessages.put(key.toLowerCase(), lines);
            }
        }

    }

    public void reload() {
        if (!this.messages.isEmpty() && !this.listMessages.isEmpty()) {
            this.messages.clear();
            this.listMessages.clear();
        }

        this.load();
    }

    public String getMessage(String command) {
        return this.messages.containsKey(command) ? (String)this.messages.get(command) : null;
    }

    public List<String> getMessages(String command) {
        return (List)(this.listMessages.containsKey(command) ? (List)this.listMessages.get(command) : new ArrayList());
    }

    public String getNoPermission() {
        return this.noPermission;
    }

    public String getStringCommandMessage() {
        return this.stringCommandMessage;
    }

    public String getConsoleOnlyMessage() {
        return this.consoleOnlyMessage;
    }

    public String getBlockedCommandMessage() {
        return this.blockedCommandMessage;
    }

    public String getMissingMessage() {
        return "§8[§6PluginHider§8] §eQuesto messaggio non è stato trovato in config.yml";
    }
}