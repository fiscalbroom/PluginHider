package org.quindici.pluginhider.nms;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class NMSVersion {
    public static final String UNSUPPORTED = "Unsupported";
    public static final String V1_7_R1 = "v1_7_R1";
    public static final String V1_7_R2 = "v1_7_R2";
    public static final String V1_7_R3 = "v1_7_R3";
    public static final String V1_7_R4 = "v1_7_R4";
    public static final String V1_8_R1 = "v1_8_R1";
    public static final String V1_8_R2 = "v1_8_R2";
    public static final String V1_8_R3 = "v1_8_R3";
    public static final String V1_9_R1 = "v1_9_R1";
    public static final String V1_9_R2 = "v1_9_R2";
    public static final String V1_10_R1 = "v1_10_R1";
    public static final String V1_11_R1 = "v1_11_R1";
    public static final String V1_12_R1 = "v1_12_R1";
    public static final String V1_13_R1 = "v1_13_R1";
    public static final String V1_13_R2 = "v1_13_R2";
    public static final String V1_14_R1 = "v1_14_R1";
    public static final String V1_15_R1 = "v1_15_R1";
    public static final String V1_16_R1 = "v1_16_R1";
    public static final String V1_16_R2 = "v1_16_R2";
    public static final String V1_16_R3 = "v1_16_R3";
    public static final String V1_17_R1 = "v1_17_R1";
    public static final String V1_18_R1 = "v1_18_R1";
    private final Map<Integer, String> versionMap = new HashMap();
    private int versionID;

    public int getVersionID() {
        return this.versionID;
    }

    public NMSVersion() {
        this.loadVersions();
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf(46) + 1);
        this.registerVersion(version);
        this.versionID = this.getVersionID(version);
        if (!this.versionMap.containsValue(version)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "----------------------------------------------------------");
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "§lSTAI UTILIZZANDO UNA VERSIONE NON SUPPORTATA DI SPIGOT!");
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Le funzionalità di PluginHider saranno limitate. Non venire a ");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "lamentarti con me, il dev di PluginHider, se qualcosa dovesse rompersi,");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "perchè usare una versione non supportata può farlo accadere. io non");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "mi prendo alcuna responsabilità di danni provocati da PluginHider");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "se usi una versione non supportata di Spigot. E' raccomandato usare una");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "versione supportata di Spigot. Supportiamo dalla versione 1.8 alla più recente");
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "----------------------------------------------------------");
        }

    }

    private void loadVersions() {
        this.registerVersion("Unsupported");
        this.registerVersion("v1_7_R1");
        this.registerVersion("v1_7_R2");
        this.registerVersion("v1_7_R3");
        this.registerVersion("v1_7_R4");
        this.registerVersion("v1_8_R1");
        this.registerVersion("v1_8_R2");
        this.registerVersion("v1_8_R3");
        this.registerVersion("v1_9_R1");
        this.registerVersion("v1_9_R2");
        this.registerVersion("v1_10_R1");
        this.registerVersion("v1_11_R1");
        this.registerVersion("v1_12_R1");
        this.registerVersion("v1_13_R1");
        this.registerVersion("v1_13_R2");
        this.registerVersion("v1_14_R1");
        this.registerVersion("v1_15_R1");
        this.registerVersion("v1_16_R1");
        this.registerVersion("v1_16_R2");
        this.registerVersion("v1_16_R3");
        this.registerVersion("v1_17_R1");
    }

    private void registerVersion(String string) {
        if (!this.versionMap.containsValue(string)) {
            this.versionMap.put(this.versionMap.size(), string);
        }
    }

    public String getVersionString() {
        return this.getVersionString(this.versionID);
    }

    public String getVersionString(int id) {
        return (String)this.versionMap.get(id);
    }

    public int getVersionID(String version) {
        return (Integer)this.versionMap.entrySet().parallelStream().filter((e) -> {
            return ((String)e.getValue()).equalsIgnoreCase(version);
        }).map(Map.Entry::getKey).findFirst().orElse(0);
    }

    public boolean isLegacy() {
        return this.versionID < this.getVersionID("v1_13_R1");
    }

    public boolean runningNewerThan(String version) {
        return this.versionID >= this.getVersionID(version);
    }
}
