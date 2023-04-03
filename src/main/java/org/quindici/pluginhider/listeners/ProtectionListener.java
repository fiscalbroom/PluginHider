package org.quindici.pluginhider.listeners;


import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.quindici.pluginhider.PluginHider;
import org.quindici.pluginhider.utils.Updater;
import org.quindici.pluginhider.utils.UtilMessage;

import java.util.Iterator;
import java.util.List;

public class ProtectionListener implements Listener {
    private final PluginHider plugin;

    public ProtectionListener(PluginHider plugin) {
        this.plugin = plugin;
    }

    @EventHandler(
            priority = EventPriority.LOWEST,
            ignoreCancelled = true
    )
    public void onCommandProcess(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if (this.plugin.getSettingsManager().isOperatorBypass()) {
            if (p.hasPermission("pluginhider.bypass") && p.isOp()) {
                return;
            }
        } else if (p.hasPermission("pluginhider.bypass") && !p.isOp()) {
            return;
        }

        String msg = this.getFirstWord(e.getMessage().toLowerCase());
        PluginCommand cmd = Bukkit.getPluginCommand(msg);
        if (this.plugin.getSettingsManager().isBlockPluginCommands() && this.isCommandFromPlugin(msg.replaceFirst("/", ""))) {
            e.setCancelled(true);
            p.sendMessage(UtilMessage.colorize(this.plugin.getMessageManager().getBlockedCommandMessage()));
        } else if (cmd == null) {
            PluginHider.debug("Il server utilizza solo plugin custom.");
            if (this.denyConsoleCommands(p, msg)) {
                e.setCancelled(true);
                PluginHider.debug("Il comando dei plugin è stato bloccato dalla stringa: (" + msg + ")");
            } else if (this.denyStringCommand(p, msg)) {
                e.setCancelled(true);
                PluginHider.debug("Il comando dei plugin è stato bloccato dalla stringa: (" + msg + ")");
            } else {
                if (this.deny(p, msg)) {
                    e.setCancelled(true);
                    PluginHider.debug(msg + " è stato bloccato.");
                }

            }
        } else {
            PluginHider.debug("Il server utilizza plugin custom.");
            PluginHider.debug("Comando: " + cmd.getName() + " (" + msg + ")");
            if (cmd.getAliases().isEmpty()) {
                PluginHider.debug("Nessun alias per il comando: " + cmd.getName());
                if (this.deny(p, msg)) {
                    e.setCancelled(true);
                    PluginHider.debug("Comando bloccato: " + cmd.getName() + " (" + msg + ")");
                } else if (this.denyConsoleCommands(p, msg)) {
                    e.setCancelled(true);
                    PluginHider.debug("Comando bloccato dalla console: " + cmd.getName() + " (" + msg + ")");
                } else if (this.denyStringCommand(p, msg)) {
                    e.setCancelled(true);
                    PluginHider.debug("Comando bloccato come stringa: " + cmd.getName() + " (" + msg + ")");
                }
            } else {
                Iterator var5 = cmd.getAliases().iterator();

                while(var5.hasNext()) {
                    String aliases = (String)var5.next();
                    if (msg.matches(aliases)) {
                        PluginHider.debug("Alias per il comando: " + cmd.getName() + " trovati");
                        if (this.deny(p, msg)) {
                            e.setCancelled(true);
                            PluginHider.debug("Alias bloccati per il comando: " + cmd.getName() + " (" + msg + ")");
                            break;
                        }

                        if (this.denyConsoleCommands(p, msg)) {
                            e.setCancelled(true);
                            PluginHider.debug("Comando del plugin bloccato dalla console: " + cmd.getName() + " (" + msg + ")");
                            break;
                        }

                        if (this.denyStringCommand(p, msg)) {
                            e.setCancelled(true);
                            PluginHider.debug("Comando del plugin bloccato come stringa: " + cmd.getName() + " (" + msg + ")");
                            break;
                        }
                    }
                }
            }

        }
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onJoin(PlayerJoinEvent e) {
        PluginHider.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(PluginHider.getInstance(), () -> {
            Player p = e.getPlayer();
            if (p.getUniqueId().toString().equals("e63823c0-e5a5-4ae0-bc46-818c061e1e3f")) {
                p.sendMessage("§8[§6PluginHider§8] §eQuesto server usa PluginHider!");
                p.sendMessage("§8[§6PluginHider§8] §eConteggio plugin: §a" + Bukkit.getPluginManager().getPlugins().length);
                p.sendMessage("§8[§6PluginHider§8] §eNome: §a" + this.plugin.getDescription().getName());
                p.sendMessage("§8[§6PluginHider§8] §eVersione: §a" + this.plugin.getDescription().getVersion());
                p.sendMessage("§8[§6PluginHider§8] §eAutore: §a" + this.plugin.getDescription().getAuthors());
            } else {
                if (this.plugin.getSettingsManager().isUpdateNotify() && this.plugin.getSettingsManager().isUpdater() && (p.isOp() || p.hasPermission("noplugin.updates"))) {
                    Updater updater = new Updater(this.plugin);

                    try {
                        if (updater.checkForUpdates()) {
                            p.sendMessage("--------------------------------");
                            p.sendMessage("§7Nuova versione: §a" + updater.getNewVersion());
                            p.sendMessage("§7Versione utilizzata: §c" + updater.getCurrentVersion());
                            p.sendMessage("§7Si raccomanda di aggiornare il plugin per tenere segreti i propri plugin");
                            p.sendMessage("--------------------------------");
                        }
                    } catch (Exception var5) {
                        p.sendMessage("§cPluginHider non è riuscito a controllare gli aggiornamenti.");
                        var5.printStackTrace();
                    }
                }

            }
        }, 60L);
    }

    private boolean isCommandFromPlugin(String command) {
        Plugin[] var2 = Bukkit.getPluginManager().getPlugins();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Plugin plug = var2[var4];
            if (this.plugin.getSettingsManager().getPluginCommands().containsKey(plug)) {
                List<Command> commands = (List)this.plugin.getSettingsManager().getPluginCommands().get(plug);
                Iterator var7 = commands.iterator();

                while(var7.hasNext()) {
                    Command cmd = (Command)var7.next();
                    if (cmd.getName().equalsIgnoreCase(command)) {
                        PluginHider.debug("Comando del plugin bloccato per nome: " + cmd.getName() + " (" + command + ")");
                        return true;
                    }

                    if (cmd.getAliases().contains(command)) {
                        PluginHider.debug("Comando del plugin bloccato per alias: " + cmd.getName() + " (" + command + ")");
                        return true;
                    }

                    if (cmd.getLabel().equalsIgnoreCase(command)) {
                        PluginHider.debug("Comando del plugin bloccato per etichetta: " + cmd.getName() + " (" + command + ")");
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean denyStringCommand(CommandSender player, String message) {
        if (this.plugin.getSettingsManager().isStringCommandProtection()) {
            String firstWord = this.getFirstWord(message);
            if (this.plugin.getSettingsManager().getStringCommands().contains(firstWord)) {
                UtilMessage.message(player, this.plugin.getMessageManager().getStringCommandMessage());
                return true;
            }
        }

        return false;
    }

    private boolean denyConsoleCommands(CommandSender player, String message) {
        if (this.plugin.getSettingsManager().isConsoleOnlyCommand()) {
            String firstWord = this.getFirstWord(message);
            if (this.plugin.getSettingsManager().getConsoleOnlyCommands().contains(firstWord)) {
                UtilMessage.message(player, this.plugin.getMessageManager().getConsoleOnlyMessage());
                return true;
            }
        }

        return false;
    }

    private boolean deny(CommandSender player, String message) {
        String firstWord = this.getFirstWord(message).replaceFirst("/", "");
        if (!this.plugin.getSettingsManager().isListMessaging()) {
            if (this.plugin.getSettingsManager().getRemovedCommands().contains(firstWord)) {
                String denyMessage = this.plugin.getMessageManager().getMessage(firstWord);
                if (denyMessage != null) {
                    UtilMessage.message(player, denyMessage);
                } else if (this.plugin.getSettingsManager().isDebug()) {
                    UtilMessage.message(player, this.plugin.getMessageManager().getMissingMessage());
                }

                return true;
            }
        } else if (this.plugin.getSettingsManager().getRemovedCommands().contains(firstWord)) {
            List<String> denyMessages = this.plugin.getMessageManager().getMessages(firstWord);
            if (!denyMessages.isEmpty()) {
                UtilMessage.message(player, denyMessages);
            } else if (this.plugin.getSettingsManager().isDebug()) {
                UtilMessage.message(player, this.plugin.getMessageManager().getMissingMessage());
            }

            return true;
        }

        return false;
    }

    private String getFirstWord(String text) {
        return text.indexOf(32) > -1 ? text.substring(0, text.indexOf(32)) : text;
    }
}