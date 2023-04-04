package org.quindici.pluginhider.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.quindici.pluginhider.ui.Menu;
import org.quindici.pluginhider.utils.ItemBuilder;

import java.util.Iterator;

public class PM1 extends Menu {
    public PM1() {
        super(54, "Menu 1 di 3");
        this.createGUI();
        this.setItem(53, (new ItemBuilder(Material.PAPER)).setTitle("§aProssima pagina").build(), (player, itemStack, type) -> {
            Menu m = new PM2();
            m.open(player);
        });
    }

    private void createGUI() {
        int slot = 0;
        Plugin[] var2 = Bukkit.getPluginManager().getPlugins();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Plugin plugins = var2[var4];
            if (slot == 45) {
                break;
            }

            String version = plugins.getDescription().getVersion();
            String website;
            if (plugins.getDescription().getWebsite() == null) {
                website = "Nessun sitoWeb Impostato.";
            } else {
                website = plugins.getDescription().getWebsite();
            }

            String authors;
            Iterator var11;
            String s;
            if (plugins.getDescription().getAuthors().isEmpty()) {
                authors = "Nessun autore trovato.";
            } else {
                authors = "";

                for(var11 = plugins.getDescription().getAuthors().iterator(); var11.hasNext(); authors = authors + "§7, §a" + s) {
                    s = (String)var11.next();
                }
            }

            String dependencies;
            if (plugins.getDescription().getDepend().isEmpty()) {
                dependencies = "Nessuna dipendenza trovata.";
            } else {
                dependencies = "";

                for(var11 = plugins.getDescription().getDepend().iterator(); var11.hasNext(); dependencies = dependencies + "§7, §a" + s) {
                    s = (String)var11.next();
                }
            }

            String softdependencies;
            if (plugins.getDescription().getSoftDepend().isEmpty()) {
                softdependencies = "Nessuna dipendenza opzionale trovata.";
            } else {
                softdependencies = "";

                for(var11 = plugins.getDescription().getSoftDepend().iterator(); var11.hasNext(); softdependencies = softdependencies + "§7, §a" + s) {
                    s = (String)var11.next();
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
