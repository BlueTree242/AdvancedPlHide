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
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import tk.bluetree242.advancedplhide.CommandCompleter;
import tk.bluetree242.advancedplhide.Group;
import tk.bluetree242.advancedplhide.Platform;
import tk.bluetree242.advancedplhide.config.ConfManager;
import tk.bluetree242.advancedplhide.config.Config;
import tk.bluetree242.advancedplhide.exceptions.ConfigurationLoadException;
import tk.bluetree242.advancedplhide.impl.group.GroupCompleter;
import tk.bluetree242.advancedplhide.impl.version.UpdateCheckResult;
import tk.bluetree242.advancedplhide.spigot.listener.event.EventListener;
import tk.bluetree242.advancedplhide.spigot.listener.packet.PacketListener;
import tk.bluetree242.advancedplhide.utils.Constants;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class AdvancedPlHideSpigot extends JavaPlugin implements Listener {
    public Config config;
    protected ConfManager<Config> confManager = ConfManager.create(getDataFolder().toPath(), "config.yml", Config.class);
    private ProtocolManager protocolManager;
    private PacketListener listener = new PacketListener(this);
    private boolean legacy = false;
    private List<Group> groups;

    public static Group getGroupForPlayer(Player player) {
        Platform core = Platform.get();
        if (player.hasPermission("plhide.no-group")) return null;
        List<Group> groups = new ArrayList<>();
        for (Group group : core.getGroups()) {
            if (player.hasPermission("plhide.group." + group.getName())) {
                groups.add(group);
            }
        }
        Group group = groups.isEmpty() ? core.getGroup("default") : core.mergeGroups(groups);
        return group;
    }

    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
        Platform.setPlatform(new Impl());
    }

    public void onEnable() {
        reloadConfig();
        protocolManager.addPacketListener(new PacketListener(this));
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
        String str = Bukkit.getServer().getClass().getPackage().getName();
        str = str.substring(str.lastIndexOf("v"));
        legacy = (str.equals("v1_8_R3") || str.contains("v1_9_R") || str.contains("v1_10_R1") || str.contains("v1_11_R1") || str.contains("v1_12_R1"));
        getServer().getPluginCommand("advancedplhide").setExecutor(new AdvancedPlHideCommand(this));
        getServer().getPluginCommand("advancedplhide").setTabCompleter(new AdvancedPlHideCommand.TabCompleter());
        new Metrics(this, 13707);
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', Constants.startupMessage()));
        performStartUpdateCheck();
    }

    public void onDisable() {
        protocolManager.removePacketListener(listener);
    }

    public boolean isLegacy() {
        return legacy;
    }


    public void performStartUpdateCheck() {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            UpdateCheckResult result = Impl.get().updateCheck();
            if (result == null) {
                getLogger().severe("Could not check for updates");
                return;
            }
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
        });
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void loadGroups() {
        groups = new ArrayList<>();
        config.groups().forEach((name, val) -> {
            List<CommandCompleter> tabcomplete = new ArrayList<>();
            for (String s : val.tabcomplete()) {
                tabcomplete.add(new GroupCompleter(s));
            }
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

    @Override
    public void reloadConfig() throws ConfigurationLoadException {
        confManager.reloadConfig();
        config = confManager.getConfigData();
        loadGroups();
    }

    public CommandMap getCommandMap() {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            return commandMap;
        } catch (Exception e) {
            return null;
        }
    }


    public class Impl extends Platform {

        @Override
        public Config getConfig() {
            return config;
        }

        @Override
        public void reloadConfig() throws ConfigurationLoadException {
            AdvancedPlHideSpigot.this.reloadConfig();
        }

        @Override
        public List<Group> getGroups() {
            return AdvancedPlHideSpigot.this.getGroups();
        }

        @Override
        public Group getGroup(String name) {
            return AdvancedPlHideSpigot.this.getGroup(name);
        }

        @Override
        public String getPluginForCommand(String s) {
            Command command = getCommandMap().getCommand(s);
            if (!(command instanceof PluginIdentifiableCommand)) return "minecraft";
            Plugin plugin = ((PluginIdentifiableCommand) command).getPlugin();
            if (plugin == null) return null;
            return plugin.getName();
        }

        @Override
        public String getVersionConfig() {
            try {
                return new String(ByteStreams.toByteArray(getResource("version-config.json")));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        @Override
        public Type getType() {
            return Type.SPIGOT;
        }
    }


}
