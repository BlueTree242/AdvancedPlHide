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

package dev.bluetree242.advancedplhide.bungee.listener.event;

import dev.bluetree242.advancedplhide.PlatformPlugin;
import dev.bluetree242.advancedplhide.bungee.AdvancedPlHideBungee;
import dev.bluetree242.advancedplhide.impl.version.UpdateCheckResult;
import dev.bluetree242.advancedplhide.utils.Constants;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeEventListener implements Listener {
    private final AdvancedPlHideBungee core;


    public BungeeEventListener(AdvancedPlHideBungee core) {
        this.core = core;
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
                    if (!PlatformPlugin.get().getConfig().pl_message().isEmpty()) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', PlatformPlugin.get().getConfig().pl_message()));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent e) {
        if (e.getPlayer().hasPermission("plhide.updatechecker")) {
            ProxyServer.getInstance().getScheduler().runAsync(core, () -> {
                UpdateCheckResult result = PlatformPlugin.get().updateCheck();
                if (result == null) return;
                String msg = result.getVersionsBehind() == 0 ? null : ChatColor.translateAlternateColorCodes('&', "&e[APH-&2Bungee&e] " + Constants.DEFAULT_BEHIND.replace("{versions}", result.getVersionsBehind() + "").replace("{download}", result.getUpdateUrl()));
                if (result.getMessage() != null) {
                    msg = ChatColor.translateAlternateColorCodes('&', "&e[APH&2Bungee&e] &c" + result.getMessage());
                }
                if (msg != null) {
                    e.getPlayer().sendMessage(msg);
                }
            });
        }
    }
}
