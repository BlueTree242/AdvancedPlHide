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

package tk.bluetree242.advancedplhide.velocity.listener.event;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.command.PlayerAvailableCommandsEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import tk.bluetree242.advancedplhide.CompleterModifier;
import tk.bluetree242.advancedplhide.PlatformPlugin;
import tk.bluetree242.advancedplhide.impl.completer.RootNodeCommandCompleter;
import tk.bluetree242.advancedplhide.impl.version.UpdateCheckResult;
import tk.bluetree242.advancedplhide.utils.Constants;
import tk.bluetree242.advancedplhide.velocity.AdvancedPlHideVelocity;
import tk.bluetree242.advancedplhide.velocity.VelocityPlayer;

public class VelocityEventListener {
    private final AdvancedPlHideVelocity core;

    public VelocityEventListener(AdvancedPlHideVelocity core) {
        this.core = core;
    }

    @Subscribe
    public void onPlayerJoin(PostLoginEvent e) {
        if (e.getPlayer().hasPermission("plhide.updatechecker")) {
            new Thread(() -> {
                UpdateCheckResult result = AdvancedPlHideVelocity.get().updateCheck();
                if (result == null) return;
                Component msg = result.getVersionsBehind() == 0 ? null : LegacyComponentSerializer.legacy('&').deserialize("&e[APH-&2Velocity&e] " + Constants.DEFAULT_BEHIND.replace("{versions}", result.getVersionsBehind() + "").replace("{download}", result.getUpdateUrl()));
                if (result.getMessage() != null) {
                    msg = LegacyComponentSerializer.legacy('&').deserialize("&e[APH&2Velocity&e] &c" + result.getMessage());
                }
                if (msg != null) {
                    msg = msg.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, result.getUpdateUrl()));
                    e.getPlayer().sendMessage(msg);
                }
            }).start();
        }
    }

    @Subscribe
    public void onCommandExecute(CommandExecuteEvent e) {
        if (e.getCommandSource() instanceof ConsoleCommandSource) return;
        String cmd = "/" + e.getCommand().split(" ")[0];
        if (cmd.equalsIgnoreCase("/plugins") || cmd.equalsIgnoreCase("/pl") || cmd.equalsIgnoreCase("/bukkit:pl") || cmd.equalsIgnoreCase("/bukkit:plugins")) {
            if (!e.getCommandSource().hasPermission("plhide.command.use")) {
                Component response = LegacyComponentSerializer.legacy('&').deserialize(PlatformPlugin.get().getConfig().pl_message());
                e.getCommandSource().sendMessage(response);
                e.setResult(CommandExecuteEvent.CommandResult.denied());
            }
        }
    }

    @Subscribe
    public void onCommands(PlayerAvailableCommandsEvent e) {
        RootNodeCommandCompleter node = new RootNodeCommandCompleter(e.getRootNode());
        CompleterModifier.handleCompleter(node, new VelocityPlayer(core, e.getPlayer()));
    }
}
