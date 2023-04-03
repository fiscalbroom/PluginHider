package org.quindici.pluginhider;

import java.util.logging.Level;
import org.quindici.pluginhider.utils.Updater;
import com.comphenix.protocol.metrics.Metrics;
import org.quindici.pluginhider.backend.MessageManager;
import org.quindici.pluginhider.commands.PluginListCommand;
import org.quindici.pluginhider.commands.PluginsMenuCommand;
import org.quindici.pluginhider.listeners.CommandProtection;
import org.quindici.pluginhider.listeners.ProtectionListener;
import org.quindici.pluginhider.listeners.ProtocolLibProtection;
import org.quindici.pluginhider.nms.NMSVersion;
import org.quindici.pluginhider.ui.MenuListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.quindici.pluginhider.backend.SettingsManager;
import org.quindici.pluginhider.commands.ReloadCommand;

public class PluginHider extends JavaPlugin {
    private static org.quindici.pluginhider.PluginHider plugin;
    public static final String PREFIX = "§8[§6PluginHider§8] §7";
    private NMSVersion nmsVersion;
    private MessageManager messageManager;
    private SettingsManager settingsManager;

    public PluginHider() {
    }

    public void onEnable() {
        plugin = this;
        this.nmsVersion = new NMSVersion();
        this.saveDefaultConfig();
        this.settingsManager = new SettingsManager(this);
        this.settingsManager.load();
        this.messageManager = new MessageManager(this);
        this.messageManager.load();
        this.getLogger().info("Versione di minecraft: " + this.nmsVersion.getVersionString());
        if (this.getNmsVersion().isLegacy()) {
            if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
                ProtocolLibProtection protection = new ProtocolLibProtection(this);
                protection.register();
                this.getLogger().info("Trovato ProtocolLib! Blocco tab attivato!");
            } else {
                this.getLogger().warning("ProtocolLib non trovato, Il blocco tab non funzionerà!");
            }
        } else {
            this.getServer().getPluginManager().registerEvents(new CommandProtection(this), this);
            this.getLogger().info("Caricati asset per versioni di minecraft recenti (1.13+)");
        }

        this.getServer().getPluginManager().registerEvents(new ProtectionListener(this), this);
        this.getServer().getPluginManager().registerEvents(new MenuListener(), this);
        this.getCommand("pluginmenu").setExecutor(new PluginsMenuCommand(this));
        this.getCommand("npreload").setExecutor(new ReloadCommand(this));
        this.getCommand("pluginlist").setExecutor(new PluginListCommand(this));

        Metrics metrics = new Metrics(this);
        metrics.addCustomChart(new Metrics.SingleLineChart("plugins", () -> {
            return Bukkit.getPluginManager().getPlugins().length;
        }));
        this.getLogger().log(Level.INFO, "Successfully loaded.");
    }

    public void onDisable() {
        this.getLogger().log(Level.INFO, "disabled.");
    }


    public static org.quindici.pluginhider.PluginHider getInstance() {
        return plugin;
    }

    public MessageManager getMessageManager() {
        return this.messageManager;
    }

    public SettingsManager getSettingsManager() {
        return this.settingsManager;
    }

    public NMSVersion getNmsVersion() {
        return this.nmsVersion;
    }

    public static void debug(String message) {
        if (getInstance().getSettingsManager().isDebug()) {
            getInstance().getLogger().log(Level.INFO, message);
        }

    }
}

