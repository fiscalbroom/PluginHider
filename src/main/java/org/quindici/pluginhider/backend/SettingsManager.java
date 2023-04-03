package org.quindici.pluginhider.backend;

import org.quindici.pluginhider.PluginHider;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class SettingsManager {
    private final PluginHider plugin;
    private final List<String> consoleOnlyCommands;
    private final List<String> stringCommands;
    private final List<String> removedCommands;
    private final List<String> blockedPlugins;
    private final Map<Plugin, List<Command>> pluginCommands;
    private boolean debug = false;
    private boolean updater = true;
    private boolean updateNotify = true;
    private boolean operatorBypass = false;
    private boolean listMessaging = false;
    private boolean clearCommandList = false;
    private boolean stringCommandProtection = false;
    private boolean consoleOnlyCommand = false;
    private boolean blockPluginCommands = false;
    private boolean cleanCommandList = false;

    public SettingsManager(PluginHider plugin) {
        this.plugin = plugin;
        this.consoleOnlyCommands = new ArrayList();
        this.stringCommands = new ArrayList();
        this.removedCommands = new ArrayList();
        this.blockedPlugins = new ArrayList();
        this.pluginCommands = new HashMap();
    }

    public void load() {
        this.debug = this.plugin.getConfig().getBoolean("debug");
        this.updater = this.plugin.getConfig().getBoolean("updater");
        this.updateNotify = this.plugin.getConfig().getBoolean("update-notification");
        this.operatorBypass = this.plugin.getConfig().getBoolean("operator-bypass");
        this.listMessaging = this.plugin.getConfig().getBoolean("list-messaging");
        this.clearCommandList = this.plugin.getConfig().getBoolean("clear-command-list");
        this.stringCommandProtection = this.plugin.getConfig().getBoolean("string-command-protection");
        this.consoleOnlyCommand = this.plugin.getConfig().getBoolean("console-only-commands");
        this.blockPluginCommands = this.plugin.getConfig().getBoolean("block-plugin-commands");
        this.cleanCommandList = this.plugin.getConfig().getBoolean("clean-command-list");
        this.removedCommands.addAll(this.plugin.getConfig().getStringList("removed-commands"));
        this.stringCommands.addAll(this.plugin.getConfig().getStringList("string-commands"));
        this.consoleOnlyCommands.addAll(this.plugin.getConfig().getStringList("console-commands"));
        this.blockedPlugins.addAll(this.plugin.getConfig().getStringList("plugins"));
        Bukkit.getScheduler().runTaskLater(this.plugin, this::map, 10L);
    }

    public void reload() {
        this.consoleOnlyCommands.clear();
        this.stringCommands.clear();
        this.removedCommands.clear();
        this.blockedPlugins.clear();
        this.pluginCommands.clear();
        this.load();
    }

    public void map() {
        Iterator var1;
        if (this.blockPluginCommands) {
            var1 = this.blockedPlugins.iterator();

            while(var1.hasNext()) {
                String stringPlugin = (String)var1.next();
                Plugin blockedPlugin = Bukkit.getPluginManager().getPlugin(stringPlugin);
                if (blockedPlugin == null) {
                    PluginHider.debug("Plugin " + stringPlugin + " non trovato!");
                } else {
                    this.pluginCommands.put(blockedPlugin, new ArrayList());
                    List<Command> pluginCommands = PluginCommandYamlParser.parse(blockedPlugin);
                    PluginHider.debug("Lista comandi (" + blockedPlugin.getName() + ")");
                    pluginCommands.forEach((command) -> {
                        PluginHider.debug(command.getName());
                    });
                    ((List)this.pluginCommands.get(blockedPlugin)).addAll(pluginCommands);
                }
            }
        }

        if (!this.plugin.getNmsVersion().isLegacy()) {
            var1 = Bukkit.getOnlinePlayers().iterator();

            while(var1.hasNext()) {
                Player online = (Player)var1.next();
                online.updateCommands();
                PluginHider.debug("Aggiornamento comandi per " + online.getName() + ".");
            }
        }

    }

    public List<String> getConsoleOnlyCommands() {
        return this.consoleOnlyCommands;
    }

    public List<String> getStringCommands() {
        return this.stringCommands;
    }

    public List<String> getRemovedCommands() {
        return this.removedCommands;
    }

    public List<String> getBlockedPlugins() {
        return this.blockedPlugins;
    }

    public Map<Plugin, List<Command>> getPluginCommands() {
        return this.pluginCommands;
    }

    public Plugin getPlugin(String name) {
        return Bukkit.getPluginManager().getPlugin(name);
    }

    public boolean isDebug() {
        return this.debug;
    }

    public boolean isUpdater() {
        return this.updater;
    }

    public boolean isUpdateNotify() {
        return this.updateNotify;
    }

    public boolean isOperatorBypass() {
        return this.operatorBypass;
    }

    public boolean isListMessaging() {
        return this.listMessaging;
    }

    public boolean hasCleanCommandList() {
        return this.clearCommandList;
    }

    public boolean isStringCommandProtection() {
        return this.stringCommandProtection;
    }

    public boolean isConsoleOnlyCommand() {
        return this.consoleOnlyCommand;
    }

    public boolean isBlockPluginCommands() {
        return this.blockPluginCommands;
    }

    public boolean isCleanCommandList() {
        return this.cleanCommandList;
    }
}
