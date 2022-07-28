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

import dev.simplix.protocolize.api.Protocolize;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import tk.bluetree242.advancedplhide.Group;
import tk.bluetree242.advancedplhide.PlatformPlugin;
import tk.bluetree242.advancedplhide.bungee.listener.event.BungeeEventListener;
import tk.bluetree242.advancedplhide.bungee.listener.packet.BungeePacketListener;
import tk.bluetree242.advancedplhide.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class AdvancedPlHideBungee extends Plugin implements Listener {
    private BungeePacketListener listener;
    private final BungeePlatformPlugin platformPlugin = new BungeePlatformPlugin(this);
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
        platformPlugin.start();
    }

    public void onEnable() {
        platformPlugin.reloadConfig();
        Protocolize.listenerProvider().registerListener(listener = new BungeePacketListener(this));
        getProxy().getPluginManager().registerListener(this, new BungeeEventListener(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new AdvancedPlHideCommand(this));
        new Metrics(this, 13709);
        ProxyServer.getInstance().getConsole().sendMessage(ChatColor.translateAlternateColorCodes('&', Constants.startupMessage()));
    }

    public void onDisable() {
        Protocolize.listenerProvider().unregisterListener(listener);
    }


}
