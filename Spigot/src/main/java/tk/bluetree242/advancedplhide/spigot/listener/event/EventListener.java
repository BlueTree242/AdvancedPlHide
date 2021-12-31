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

package tk.bluetree242.advancedplhide.spigot.listener.event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import tk.bluetree242.advancedplhide.Platform;
import tk.bluetree242.advancedplhide.impl.version.UpdateCheckResult;
import tk.bluetree242.advancedplhide.spigot.AdvancedPlHideSpigot;
import tk.bluetree242.advancedplhide.utils.Constants;

public class EventListener implements Listener {
    private final AdvancedPlHideSpigot core;

    public EventListener(AdvancedPlHideSpigot core) {
        this.core = core;
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        if (e.getPlayer().hasPermission("plhide.command.use")) return;
        String cmd = e.getMessage().toLowerCase().split(" ")[0];
        if (cmd.equalsIgnoreCase("/plugins") || cmd.equalsIgnoreCase("/pl") || cmd.equalsIgnoreCase("/bukkit:pl") || cmd.equalsIgnoreCase("/bukkit:plugins")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Platform.get().getConfig().pl_message()));
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (e.getPlayer().hasPermission("plhide.updatechecker")) {
            Bukkit.getScheduler().runTask(core, () -> {
                UpdateCheckResult result = Platform.get().get().updateCheck();
                if (result == null) return;
                String msg = result.getVersionsBehind() == 0 ? null : ChatColor.translateAlternateColorCodes('&', "&e[APH-&2Spigot&e] " + Constants.DEFAULT_BEHIND.replace("{versions}", result.getVersionsBehind() + "").replace("{download}", result.getUpdateUrl()));
                if (result.getMessage() != null) {
                    msg = ChatColor.translateAlternateColorCodes('&', "&e[APH-&2Spigot&e] &c" + result.getMessage());
                }
                if (msg != null) {
                    e.getPlayer().sendMessage(msg);
                }
            });
        }
    }
}
