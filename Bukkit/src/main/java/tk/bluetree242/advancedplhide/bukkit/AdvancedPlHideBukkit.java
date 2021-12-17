package tk.bluetree242.advancedplhide.bukkit;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;
import tk.bluetree242.advancedplhide.config.ConfManager;
import tk.bluetree242.advancedplhide.config.Config;


public class AdvancedPlHideBukkit extends JavaPlugin implements Listener {
    public Config config;
    protected ConfManager<Config> confManager = ConfManager.create(getDataFolder().toPath(), "config.yml", Config.class);
    private ProtocolManager protocolManager;
    private PacketListener listener = new PacketListener(this);
    private boolean legacy = false;


    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    public void onEnable() {
        confManager.reloadConfig();
        config = confManager.getConfigData();
        protocolManager.addPacketListener(new PacketListener(this));
        getServer().getPluginManager().registerEvents(this, this);
        String str = Bukkit.getServer().getClass().getPackage().getName();
        str = str.substring(str.lastIndexOf("v"));
        legacy = (str.equals("v1_8_R3") || str.contains("v1_9_R") || str.contains("v1_10_R1") || str.contains("v1_11_R1") || str.contains("v1_12_R1"));
    }

    public void onDisable() {
        protocolManager.removePacketListener(listener);
    }

    public boolean isLegacy() {
        return legacy;
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        if (e.getPlayer().hasPermission("plhide.command.use")) return;
        String msg = e.getMessage().toLowerCase();
        if (msg.startsWith("/plugins") || msg.startsWith("/pl") || msg.startsWith("/bukkit:pl") || msg.startsWith("/bukkit:plugins")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', config.pl_message()));
        }
    }
}
