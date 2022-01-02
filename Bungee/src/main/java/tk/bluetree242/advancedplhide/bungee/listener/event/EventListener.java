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

package tk.bluetree242.advancedplhide.bungee.listener.event;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.json.JSONObject;
import tk.bluetree242.advancedplhide.Platform;
import tk.bluetree242.advancedplhide.bungee.AdvancedPlHideBungee;
import tk.bluetree242.advancedplhide.impl.version.UpdateCheckResult;
import tk.bluetree242.advancedplhide.utils.Constants;

import java.util.concurrent.TimeUnit;

public class EventListener implements Listener {
    private final AdvancedPlHideBungee core;


    public EventListener(AdvancedPlHideBungee core) {
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
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Platform.get().getConfig().pl_message()));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent e) {
        if (e.getPlayer().hasPermission("plhide.updatechecker")) {
            ProxyServer.getInstance().getScheduler().runAsync(core, () -> {
                UpdateCheckResult result = Platform.get().updateCheck();
                if (result == null) return;
                String msg = result.getVersionsBehind() == 0 ? null : ChatColor.translateAlternateColorCodes('&', "&e[APH-&2Bungee&e] " + Constants.DEFAULT_BEHIND.replace("{versions}", result.getVersionsBehind() + "").replace("{download}", result.getUpdateUrl()));
                if (result.getMessage() != null) {
                    msg = ChatColor.translateAlternateColorCodes('&', "&e[APH-&2Bungee&e] &c" + result.getMessage());
                }
                if (msg != null) {
                    e.getPlayer().sendMessage(msg);
                }
            });
        }
        ProxyServer.getInstance().getScheduler().schedule(core, () -> {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("proxy");
            out.writeUTF("");
            e.getPlayer().getServer().getInfo().sendData( "aph:main", out.toByteArray());
        }, 1000, TimeUnit.MILLISECONDS);
    }

    @EventHandler
    public void on(PluginMessageEvent e) {
        if (!e.getTag().equals("aph:main") || !(e.getReceiver() instanceof ProxiedPlayer)) return;
        e.setCancelled(true);
        Server server = ((ProxiedPlayer) e.getReceiver()).getServer();
        ByteArrayDataInput in = ByteStreams.newDataInput(e.getData());
        String subChannel = in.readUTF();
        if (subChannel.equals("backend")) {
            JSONObject json = new JSONObject();
            if (!json.getBoolean("proxy")) {
                core.getLogger().severe("Proxy Mode is not enabled in config of " + server.getInfo().getName() + ". Please enable it, there are lots of bugs without it");

            }
        }
    }
}
