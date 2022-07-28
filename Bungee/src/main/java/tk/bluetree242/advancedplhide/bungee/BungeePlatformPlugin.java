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

package tk.bluetree242.advancedplhide.bungee;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import tk.bluetree242.advancedplhide.PlatformPlugin;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

public class BungeePlatformPlugin extends PlatformPlugin {
    
    private final AdvancedPlHideBungee core;

    public BungeePlatformPlugin(AdvancedPlHideBungee core) {
        this.core = core;
    }

    @Override
    public void runSafe(Runnable runnable) {
        ProxyServer.getInstance().getScheduler().runAsync(core, runnable);
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
        return null;
    }

    @Override
    public String getVersionConfig() {
        try {
            return new String(ByteStreams.toByteArray(core.getResourceAsStream("version-config.json")));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public Type getType() {
        return Type.BUNGEE;
    }
}
