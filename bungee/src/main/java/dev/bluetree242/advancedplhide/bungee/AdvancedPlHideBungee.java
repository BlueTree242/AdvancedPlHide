/*
 *  LICENSE
 * AdvancedPlHide
 * -------------
 * Copyright (C) 2021 - 2024 BlueTree242
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

package dev.bluetree242.advancedplhide.bungee;

import dev.bluetree242.advancedplhide.Group;
import dev.bluetree242.advancedplhide.PlatformPlugin;
import dev.bluetree242.advancedplhide.bungee.listener.event.BungeeEventListener;
import dev.bluetree242.advancedplhide.bungee.listener.packet.BungeePacketListener;
import dev.bluetree242.advancedplhide.impl.version.UpdateCheckResult;
import dev.bluetree242.advancedplhide.utils.Constants;
import dev.simplix.protocolize.api.Protocolize;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdvancedPlHideBungee extends Plugin implements Listener {
    private final AdvancedPlHideBungee.Impl platformPlugin = new Impl();
    private BungeePacketListener listener;
    private List<Group> groups = new ArrayList<>();

    public Group getGroupForPlayer(ProxiedPlayer player) {
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
        PlatformPlugin.setPlatform(platformPlugin);
        platformPlugin.initConfigManager();
    }

    public void onEnable() {
        platformPlugin.reloadConfig();
        Protocolize.listenerProvider().registerListener(listener = new BungeePacketListener(this));
        getProxy().getPluginManager().registerListener(this, new BungeeEventListener(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new AdvancedPlHideCommand(this));
        new Metrics(this, 13709);
        ProxyServer.getInstance().getConsole().sendMessage(ChatColor.translateAlternateColorCodes('&', Constants.startupMessage()));
        performStartUpdateCheck();
    }

    public void onDisable() {
        Protocolize.listenerProvider().unregisterListener(listener);
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
            getLogger().warning("Group default was not found. If someone has no permission for any group, no group applies on them");
        }

    }

    public void performStartUpdateCheck() {
        ProxyServer.getInstance().getScheduler().runAsync(this, () -> {
            try {
                UpdateCheckResult result = Impl.get().updateCheck();
                String msg = result.getVersionsBehind() == 0 ?
                        ChatColor.translateAlternateColorCodes('&', Constants.DEFAULT_UP_TO_DATE) :
                        ChatColor.translateAlternateColorCodes('&', Constants.DEFAULT_BEHIND.replace("{versions}", result.getVersionsBehind() + "")
                                .replace("{download}", result.getUpdateUrl()));
                if (result.getMessage() != null) {
                    msg = ChatColor.translateAlternateColorCodes('&', result.getMessage());
                }
                switch (result.getLoggerType()) {
                    case "INFO":
                        getLogger().info(msg);
                        break;
                    case "WARNING":
                        getLogger().warning(msg);
                        break;
                    case "ERROR":
                        getLogger().severe(msg);
                        break;
                }
            } catch (Throwable ex) {
                getLogger().severe(String.format("Could not check for updates: %s", ex.getMessage()));
            }
        });
    }

    public Group getGroup(String name) {
        for (Group group : groups) {
            if (group.getName().equals(name)) return group;
        }
        return null;
    }

    public List<Group> getGroups() {
        return groups;
    }


    public class Impl extends PlatformPlugin {
        @Override
        public void loadGroups() {
            AdvancedPlHideBungee.this.loadGroups();
        }

        @Override
        public File getDataFolder() {
            return AdvancedPlHideBungee.this.getDataFolder();
        }

        @Override
        public List<Group> getGroups() {
            return AdvancedPlHideBungee.this.getGroups();
        }

        @Override
        public Group getGroup(String name) {
            return AdvancedPlHideBungee.this.getGroup(name);
        }

        @Override
        public String getPluginForCommand(String s) {
            return null;
        }

        @Override
        public Type getType() {
            return Type.BUNGEE;
        }
    }

}
