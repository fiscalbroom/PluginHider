package org.quindici.pluginhider.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;
import org.bukkit.entity.Player;
import org.quindici.pluginhider.PluginHider;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.logging.Level;

public class ProtocolLibProtection {
    private final PluginHider instance;

    public ProtocolLibProtection(PluginHider plugin) {
        this.instance = plugin;
    }

    public void register() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new PacketAdapter(this.instance, ListenerPriority.LOWEST, new PacketType[]{PacketType.Play.Client.TAB_COMPLETE}) {
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) {
                    try {
                        Player p = event.getPlayer();
                        PacketContainer packet = event.getPacket();
                        String message = ((String)packet.getSpecificModifier(String.class).read(0)).toLowerCase();
                        if (org.quindici.pluginhider.listeners.ProtocolLibProtection.this.instance.getSettingsManager().isOperatorBypass()) {
                            if (p.hasPermission("pluginhider.bypass") || p.isOp()) {
                                PluginHider.debug(p.getName() + " tab complete per i player oppati: " + message);
                                return;
                            }
                        } else if (p.hasPermission("pluginhider.bypass") && !p.isOp()) {
                            PluginHider.debug(p.getName() + " tab complete per i player non oppati: " + message);
                            return;
                        }

                        if (org.quindici.pluginhider.listeners.ProtocolLibProtection.this.instance.getSettingsManager().hasCleanCommandList()) {
                            if (message.startsWith("") && !message.contains(" ")) {
                                org.quindici.pluginhider.listeners.ProtocolLibProtection.this.deny(p);
                            } else if (message.startsWith("") && message.contains(" ")) {
                                org.quindici.pluginhider.listeners.ProtocolLibProtection.this.deny(p);
                            }
                        } else {
                            Iterator var5 = org.quindici.pluginhider.listeners.ProtocolLibProtection.this.instance.getSettingsManager().getRemovedCommands().iterator();

                            do {
                                if (!var5.hasNext()) {
                                    return;
                                }

                                String cmd = (String)var5.next();
                                if (message.startsWith("/") && message.length() == 1) {
                                    org.quindici.pluginhider.listeners.ProtocolLibProtection.this.deny(p);
                                    return;
                                }

                                if (message.startsWith("/" + cmd) && message.contains(" ")) {
                                    org.quindici.pluginhider.listeners.ProtocolLibProtection.this.deny(p);
                                    return;
                                }
                            } while(!message.contains(":") || message.contains(" "));

                            org.quindici.pluginhider.listeners.ProtocolLibProtection.this.deny(p);
                        }
                    } catch (FieldAccessException var7) {
                        this.plugin.getLogger().log(Level.SEVERE, "Impossibile accedere al campo.", var7);
                    }
                }

            }
        });
    }

    private void deny(Player player) {
        PacketContainer closeTab = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.CLOSE_WINDOW);

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, closeTab);
            PluginHider.debug("Pacchetto del server inviato a " + player.getName());
        } catch (InvocationTargetException var4) {
            var4.printStackTrace();
        }

    }
}
