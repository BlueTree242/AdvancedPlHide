/*
 *  LICENSE
 * AdvancedPlHide
 * -------------
 * Copyright (C) 2021 - 2021 BlueTree242
 * -------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 *  END
 */

package tk.bluetree242.advancedplhide.spigot;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;
import tk.bluetree242.advancedplhide.Platform;
import tk.bluetree242.advancedplhide.config.ConfManager;
import tk.bluetree242.advancedplhide.config.Config;
import tk.bluetree242.advancedplhide.exceptions.ConfigurationLoadException;


public class AdvancedPlHideSpigot extends JavaPlugin implements Listener {
    public Config config;
    protected ConfManager<Config> confManager = ConfManager.create(getDataFolder().toPath(), "config.yml", Config.class);
    private ProtocolManager protocolManager;
    private PacketListener listener = new PacketListener(this);
    private boolean legacy = false;


    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
        Platform.setPlatform(new Impl());
    }

    public void onEnable() {

        confManager.reloadConfig();
        config = confManager.getConfigData();
        protocolManager.addPacketListener(new PacketListener(this));
        getServer().getPluginManager().registerEvents(this, this);
        String str = Bukkit.getServer().getClass().getPackage().getName();
        str = str.substring(str.lastIndexOf("v"));
        legacy = (str.equals("v1_8_R3") || str.contains("v1_9_R") || str.contains("v1_10_R1") || str.contains("v1_11_R1") || str.contains("v1_12_R1"));
        getServer().getPluginCommand("advancedplhide").setExecutor(new AdvancedPlHideCommand(this));
        getServer().getPluginCommand("advancedplhide").setTabCompleter(new AdvancedPlHideCommand.TabCompleter());
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
        String cmd = e.getMessage().toLowerCase().split(" ")[0];
        if (cmd.equalsIgnoreCase("/plugins") || cmd.equalsIgnoreCase("/pl") || cmd.equalsIgnoreCase("/bukkit:pl") || cmd.equalsIgnoreCase("/bukkit:plugins")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', config.pl_message()));
        }
    }

    public class Impl extends Platform{

        @Override
        public Config getConfig() {
            return config;
        }

        @Override
        public void reloadConfig() throws ConfigurationLoadException {
            confManager.reloadConfig();
            config = confManager.getConfigData();
        }
    }

}
