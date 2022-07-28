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

import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;
import tk.bluetree242.advancedplhide.PlatformPlugin;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;

public class SpigotPlatformPlugin extends PlatformPlugin{
    private final AdvancedPlHideSpigot core;

    public SpigotPlatformPlugin(AdvancedPlHideSpigot core) {
        this.core = core;
    }


    @Override
    public void runSafe(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(core, runnable);
    }

    @Override
    public String translateColorCodes(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    @Override
    public void logInfo(String s) {
        core.getLogger().info(s);
    }

    @Override
    public void logWarning(String s) {
        core.getLogger().warning(s);
    }

    @Override
    public void logError(String s) {
        core.getLogger().severe(s);
    }

    @Override
    public File getDataFolder() {
        return core.getDataFolder();
    }

    @Override
    public String getPluginForCommand(String s) {
        Command command = getCommandMap().getCommand(s);
        if (!(command instanceof PluginIdentifiableCommand)) return "minecraft";
        Plugin plugin = ((PluginIdentifiableCommand) command).getPlugin();
        if (plugin == null) return null;
        return plugin.getName();
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

    @Override
    public String getVersionConfig() {
        try {
            return new String(ByteStreams.toByteArray(core.getResource("version-config.json")));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public PlatformPlugin.Type getType() {
        return PlatformPlugin.Type.SPIGOT;
    }
}
