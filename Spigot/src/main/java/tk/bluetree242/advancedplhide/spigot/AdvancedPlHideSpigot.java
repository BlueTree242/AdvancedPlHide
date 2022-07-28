/*
 *  LICENSE
 * AdvancedPlHide
 * -------------
 * Copyright (C) 2021 - 2022 BlueTree242
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
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import tk.bluetree242.advancedplhide.Group;
import tk.bluetree242.advancedplhide.PlatformPlugin;
import tk.bluetree242.advancedplhide.spigot.listener.event.SpigotEventListener;
import tk.bluetree242.advancedplhide.spigot.listener.packet.SpigotPacketListener;
import tk.bluetree242.advancedplhide.utils.Constants;

import java.util.ArrayList;
import java.util.List;


public class AdvancedPlHideSpigot extends JavaPlugin implements Listener {
    private final SpigotPacketListener listener = new SpigotPacketListener(this);
    private ProtocolManager protocolManager;
    private boolean legacy = false;
    private List<Group> groups;
    private final  SpigotPlatformPlugin platformPlugin = new SpigotPlatformPlugin(this);

    public Group getGroupForPlayer(Player player) {
        if (player.hasPermission("plhide.no-group")) return null;
        List<Group> groups = new ArrayList<>();
        for (Group group : platformPlugin.getGroups()) {
            if (player.hasPermission("plhide.group." + group.getName())) {
                groups.add(group);
            }
        }
        return groups.isEmpty() ? platformPlugin.getGroup("default") : platformPlugin.mergeGroups(groups);
    }

    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
        PlatformPlugin.setPlatform(platformPlugin);
        platformPlugin.start();
    }

    public void onEnable() {
        platformPlugin.reloadConfig();
        protocolManager.addPacketListener(new SpigotPacketListener(this));
        getServer().getPluginManager().registerEvents(new SpigotEventListener(this), this);
        String str = Bukkit.getServer().getClass().getPackage().getName();
        str = str.substring(str.lastIndexOf("v"));
        legacy = (str.equals("v1_8_R3") || str.contains("v1_9_R") || str.contains("v1_10_R1") || str.contains("v1_11_R1") || str.contains("v1_12_R1"));
        getServer().getPluginCommand("advancedplhide").setExecutor(new AdvancedPlHideCommand(this));
        getServer().getPluginCommand("advancedplhide").setTabCompleter(new AdvancedPlHideCommand.TabCompleter());
        new Metrics(this, 13707);
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', Constants.startupMessage()));
    }

    public void onDisable() {
        protocolManager.removePacketListener(listener);
    }

    public boolean isLegacy() {
        return legacy;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void loadGroups() {
        groups = new ArrayList<>();
        platformPlugin.getConfig().groups().forEach((name, val) -> {
            if (getGroup(name) == null)
                groups.add(new Group(name, val.tabcomplete()));
            else {
                getLogger().warning("Group " + name + " is repeated.");
            }
        });
        if (getGroup("default") == null) {
            getLogger().warning("group default was not found. If someone has no permission for any group, no group applies on them");

        }

    }

    public Group getGroup(String name) {
        for (Group group : groups) {
            if (group.getName().equals(name)) return group;
        }
        return null;
    }



}
