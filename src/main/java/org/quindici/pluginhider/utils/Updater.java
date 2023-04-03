package org.quindici.pluginhider.utils;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Updater {
    private final int project = 1;
    private URL checkURL;
    private String newVersion;
    private String currentVersion;
    private JavaPlugin plugin;

    public Updater(JavaPlugin plugin) {
        this.plugin = plugin;
        this.currentVersion = plugin.getDescription().getVersion();

        try {
            this.checkURL = new URL("Nessun link");
        } catch (MalformedURLException var3) {
        }

    }

    public int getProjectID() {
        return 12965;
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    public String getCurrentVersion() {
        return this.currentVersion;
    }

    public String getNewVersion() {
        return this.newVersion;
    }

    public String getResourceURL() {
        return "nessun link";
    }

    public boolean checkForUpdates() throws Exception {
        URLConnection con = this.checkURL.openConnection();
        this.newVersion = (new BufferedReader(new InputStreamReader(con.getInputStream()))).readLine();
        return !this.currentVersion.equals(this.newVersion);
    }
}
