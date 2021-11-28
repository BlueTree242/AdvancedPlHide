package me.bluetree.advancedplhide;


import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AdvancedPlHide extends JavaPlugin implements Listener {
    private ProtocolManager protocolManager;
    private PacketListener listener = new PacketListener(this);

    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    public void onEnable() {
        protocolManager.addPacketListener(new PacketListener(this));
        getServer().getPluginManager().registerEvents(this, this);

    }

    public void onDisable() {
        protocolManager.removePacketListener(listener);
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        if (e.getPlayer().hasPermission("plhide.command")) return;
        if (e.getMessage().startsWith("/plugins") || e.getMessage().startsWith("/pl") || e.getMessage().startsWith("/bukkit:pl") || e.getMessage().startsWith("/bukkit:plugins")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "We won't show you the plugin list for sure");
        }
    }
}
