package org.quindici.pluginhider.gui;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.quindici.pluginhider.ui.Menu;
import org.quindici.pluginhider.utils.ItemBuilder;

import java.util.Iterator;

public class PM3 extends Menu {
    public PM3() {
        super(54, "Plugins Menu Page 3");
        this.createGUI();
        this.setItem(45, (new ItemBuilder(Material.PAPER)).setTitle("§aPrevious Page").build(), (player, itemStack, type) -> {
            Menu m = new PM2();
            m.open(player);
        });
    }

    private void createGUI() {
        int slot = 0;
        int plugin = Bukkit.getPluginManager().getPlugins().length;

        for(int i = 90; i < plugin && slot != 45; ++i) {
            Plugin plugins = Bukkit.getPluginManager().getPlugins()[i];
            String version = plugins.getDescription().getVersion();
            String website;
            if (plugins.getDescription().getWebsite() == null) {
                website = "Nessun sito web trovato.";
            } else {
                website = plugins.getDescription().getWebsite();
            }

            String authors;
            Iterator var10;
            String s;
            if (plugins.getDescription().getAuthors().isEmpty()) {
                authors = "Nessun autore trovato.";
            } else {
                authors = "";

                for(var10 = plugins.getDescription().getAuthors().iterator(); var10.hasNext(); authors = authors + "§7, §a" + s) {
                    s = (String)var10.next();
                }
            }

            String dependencies;
            if (plugins.getDescription().getDepend().isEmpty()) {
                dependencies = "Nessuna dipendenza trovata.";
            } else {
                dependencies = "";

                for(var10 = plugins.getDescription().getDepend().iterator(); var10.hasNext(); dependencies = dependencies + "§7, §a" + s) {
                    s = (String)var10.next();
                }
            }

            String softdependencies;
            if (plugins.getDescription().getSoftDepend().isEmpty()) {
                softdependencies = "Nessuna dipendenza opzionale trovata.";
            } else {
                softdependencies = "";

                for(var10 = plugins.getDescription().getSoftDepend().iterator(); var10.hasNext(); softdependencies = softdependencies + "§7, §a" + s) {
                    s = (String)var10.next();
                }
            }

            if (plugins.isEnabled()) {
                this.setItem(slot, (new ItemBuilder(Material.BOOK)).setTitle("§a" + plugins.getName()).setLore(new String[]{"§7Autori: §a" + authors.replaceFirst(", ", ""), "§7Versione: §a" + version, "§7Dipendenze: §a" + dependencies.replaceFirst(", ", ""), "§7Dipendenze leggere: §a" + softdependencies.replaceFirst(", ", ""), "§7Sito web: §a" + website}).build());
            } else {
                this.setItem(slot, (new ItemBuilder(Material.BOOK)).setTitle("§c" + plugins.getName()).setLore(new String[]{"§7Autori: §a" + plugins.getDescription().getAuthors().toString(), "§7Versione: §a" + plugins.getDescription().getVersion(), "§7Dipendenze: §a" + plugins.getDescription().getDepend(), "§7Dipendenze leggere: §a" + plugins.getDescription().getSoftDepend(), "§7Sito web: §a" + plugins.getDescription().getWebsite()}).build());
            }

            ++slot;
        }

    }
}
