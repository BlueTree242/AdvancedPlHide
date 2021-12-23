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

package tk.bluetree242.advancedplhide.bungee;

import com.google.common.io.ByteStreams;
import dev.simplix.protocolize.api.Protocolize;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import tk.bluetree242.advancedplhide.CommandCompleter;
import tk.bluetree242.advancedplhide.Group;
import tk.bluetree242.advancedplhide.Platform;
import tk.bluetree242.advancedplhide.config.ConfManager;
import tk.bluetree242.advancedplhide.config.Config;
import tk.bluetree242.advancedplhide.exceptions.ConfigurationLoadException;
import tk.bluetree242.advancedplhide.impl.group.GroupCompleter;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvancedPlHideBungee extends Plugin implements Listener {
    public Config config;
    protected ConfManager<Config> confManager = ConfManager.create(getDataFolder().toPath(), "config.yml", Config.class);
    private PacketListener listener;
    private Map<String, String> map = new HashMap<>();
    private List<Group> groups = new ArrayList<>();

    public void onEnable() {
        reloadConfig();
        Protocolize.listenerProvider().registerListener(listener = new PacketListener(this));
        Platform.setPlatform(new Impl());
        ProxyServer.getInstance().getPluginManager().registerListener(this, this);
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new AdvancedPlHideCommand(this));
    }



    public void onDisable() {
        Protocolize.listenerProvider().unregisterListener(listener);
    }

    public void loadGroups() {
        groups = new ArrayList<>();
        config.groups().forEach((name, val) -> {
            List<CommandCompleter> tabcomplete = new ArrayList<>();
            for (String s : val.tabcomplete()) {
                tabcomplete.add(new GroupCompleter(s));
            }
            if (getGroup(name) == null)
                groups.add(new Group(name, tabcomplete));
            else {
                getLogger().warning("Group " + name + " is repeated.");
            }
        });
        if (getGroup("default") == null) {
            getLogger().warning("Group default was not found. If someone has no permission for any group, no group applies on them");
        }

    }

    public static Group getGroupForPlayer(ProxiedPlayer player) {
        Platform core = Platform.get();
        if (player.hasPermission("plhide.no-group")) return null;
        List<Group> groups = new ArrayList<>();
        for (Group group : core.getGroups()) {
            if (player.hasPermission("plhide.group." + group.getName())) {
                groups.add(group);
            }
        }
        Group group = groups.isEmpty()? core.getGroup("default") : core.mergeGroups(groups);
        return group;
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

    public void reloadConfig() {
        confManager.reloadConfig();
        config = confManager.getConfigData();
        loadGroups();

    }

    public class Impl extends Platform {

        @Override
        public Config getConfig() {
            return config;
        }

        @Override
        public void reloadConfig() throws ConfigurationLoadException {
            AdvancedPlHideBungee.this.reloadConfig();
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
        public String getVersionConfig() {
            try {
                return new String(ByteStreams.toByteArray(getResourceAsStream("version-config.json")));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    @EventHandler
    public void onChat(ChatEvent e) {
        if (e.getMessage().startsWith("/")) {
            if (e.getSender() instanceof ProxiedPlayer) {
                ProxiedPlayer sender = (ProxiedPlayer) e.getSender();
                if (sender.hasPermission("plhide.command.use")) return;
                String cmd = e.getMessage().toLowerCase().split(" ")[0];
                if (cmd.equalsIgnoreCase("/plugins") || cmd.equalsIgnoreCase("/pl") || cmd.equalsIgnoreCase("/bukkit:pl") || cmd.equalsIgnoreCase("/bukkit:plugins")) {
                    e.setCancelled(true);
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.pl_message()));
                }
            }
        }
    }

}
