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

package dev.bluetree242.advancedplhide.spigot;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketEvent;
import dev.bluetree242.advancedplhide.Group;
import dev.bluetree242.advancedplhide.PlatformPlugin;
import dev.bluetree242.advancedplhide.impl.version.UpdateCheckResult;
import dev.bluetree242.advancedplhide.spigot.listener.event.SpigotEventListener;
import dev.bluetree242.advancedplhide.spigot.listener.packet.SpigotPacketListener;
import dev.bluetree242.advancedplhide.spigot.modern.ModernHandler;
import dev.bluetree242.advancedplhide.spigot.modern.V1_19_3_Handler;
import dev.bluetree242.advancedplhide.spigot.modern.V1_19_Handler;
import dev.bluetree242.advancedplhide.spigot.modern.V1_20_5_Handler;
import dev.bluetree242.advancedplhide.spigot.paper.PaperEventListener;
import dev.bluetree242.advancedplhide.utils.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class AdvancedPlHideSpigot extends JavaPlugin {
    private final AdvancedPlHideSpigot.Impl platformPlugin = new Impl();
    private ProtocolLibHookHandler protocolLibHookHandler;
    private boolean legacy = false;
    private List<Group> groups;
    private ModernHandler modernHandler;

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
        PlatformPlugin.setPlatform(platformPlugin);
        platformPlugin.initConfigManager();
    }

    public void onEnable() {
        platformPlugin.reloadConfig();
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', Constants.startupMessage()));

        if (!PaperEventListener.isAvailable() && !getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
            getLogger().severe("Plugin could not be enabled because ProtocolLib is not enabled/installed and this is not a Paper 1.20.6+ server.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        } else if (!PaperEventListener.isAvailable()) {
            useProtocolLib();
        } else {
            getLogger().info("This server supports Paper's dedicated events for tab-completion and ProtocolLib will not be used.");
            getServer().getPluginManager().registerEvents(new PaperEventListener(this::getGroupForPlayer), this);
        }

        getServer().getPluginManager().registerEvents(new SpigotEventListener(this), this);

        AdvancedPlHideCommand command = new AdvancedPlHideCommand(this);
        getServer().getPluginCommand("advancedplhide").setExecutor(command);
        getServer().getPluginCommand("advancedplhide").setTabCompleter(command);

        new Metrics(this, 13707);
        performStartUpdateCheck();
    }

    public void useProtocolLib() {
        String mv = Bukkit.getServer().getBukkitVersion();
        legacy = (mv.startsWith("1.8") || mv.startsWith("1.9") || mv.startsWith("1.10") || mv.startsWith("1.11") || mv.startsWith("1.12"));
        if (!legacy) {
            boolean modernNeeded = !(mv.startsWith("1.13") || mv.startsWith("1.14") || mv.startsWith("1.15") || mv.startsWith("1.16") || mv.startsWith("1.17") || mv.startsWith("1.18"));
            if (!modernNeeded) {
                modernHandler = (packetEvent, group, whitelist) -> {
                    throw new UnsupportedOperationException();
                };
            } else if (mv.startsWith("1.19.1")) {
                modernHandler = new V1_19_Handler();
            } else if (mv.startsWith("1.19.3") || mv.startsWith("1.19.4") || (mv.startsWith("1.20") && Integer.parseInt(mv.substring(5, 6)) <= 4)) { // Expect 1.19.3+
                modernHandler = new V1_19_3_Handler();
            } else {
                modernHandler = new V1_20_5_Handler();
            }
        }
        protocolLibHookHandler = new ProtocolLibHookHandler();
        protocolLibHookHandler.hook();
    }

    public void onDisable() {
        if (protocolLibHookHandler != null) protocolLibHookHandler.unhook();
    }

    public boolean isLegacy() {
        return legacy;
    }

    public ModernHandler getModernHandler() {
        return modernHandler;
    }

    public void performStartUpdateCheck() {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
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

    public CommandMap getCommandMap() {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            return (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
        } catch (Exception e) {
            return null;
        }
    }

    private class ProtocolLibHookHandler {
        private final SpigotPacketListener listener = new SpigotPacketListener(AdvancedPlHideSpigot.this);

        public void hook() {
            ProtocolLibrary.getProtocolManager().addPacketListener(listener);
        }

        public void unhook() {
            ProtocolLibrary.getProtocolManager().removePacketListener(listener);
        }
    }

    public class Impl extends PlatformPlugin {

        @Override
        public void loadGroups() {
            AdvancedPlHideSpigot.this.loadGroups();
        }

        @Override
        public File getDataFolder() {
            return AdvancedPlHideSpigot.this.getDataFolder();
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
        public Type getType() {
            return Type.SPIGOT;
        }
    }
}
