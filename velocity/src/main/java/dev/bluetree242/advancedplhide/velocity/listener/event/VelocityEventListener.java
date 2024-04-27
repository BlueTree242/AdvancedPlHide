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

package dev.bluetree242.advancedplhide.velocity.listener.event;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.command.PlayerAvailableCommandsEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import dev.bluetree242.advancedplhide.CompleterModifier;
import dev.bluetree242.advancedplhide.PlatformPlugin;
import dev.bluetree242.advancedplhide.impl.completer.RootNodeCommandCompleter;
import dev.bluetree242.advancedplhide.impl.version.UpdateCheckResult;
import dev.bluetree242.advancedplhide.utils.Constants;
import dev.bluetree242.advancedplhide.velocity.AdvancedPlHideVelocity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class VelocityEventListener {
    private final AdvancedPlHideVelocity core;

    public VelocityEventListener(AdvancedPlHideVelocity core) {
        this.core = core;
    }

    @Subscribe
    public void onPlayerJoin(PostLoginEvent e) {
        if (e.getPlayer().hasPermission("plhide.updatechecker")) {
            core.server.getScheduler().buildTask(core, () -> {
                try {
                    UpdateCheckResult result = AdvancedPlHideVelocity.get().updateCheck();
                    Component msg = result.getVersionsBehind() == 0 ? null : LegacyComponentSerializer.legacy('&').deserialize("&e[APH&r-&2Velocity&e] " + Constants.DEFAULT_BEHIND.replace("{versions}", result.getVersionsBehind() + "").replace("{download}", result.getUpdateUrl()));
                    if (result.getMessage() != null) {
                        msg = LegacyComponentSerializer.legacy('&').deserialize("&e[APH&r-&2Velocity&e] &c" + result.getMessage());
                    }
                    if (msg != null) {
                        msg = msg.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, result.getUpdateUrl()));
                        e.getPlayer().sendMessage(msg);
                    }
                } catch (Throwable ex) {
                    core.getLogger().error(String.format("Could not check for updates: %s", ex.getMessage()));
                }
            }).schedule();
        }
    }

    @Subscribe
    public void onCommandExecute(CommandExecuteEvent e) {
        if (e.getCommandSource() instanceof ConsoleCommandSource) return;
        String cmd = "/" + e.getCommand().split(" ")[0];
        if (cmd.equalsIgnoreCase("/plugins") || cmd.equalsIgnoreCase("/pl") || cmd.equalsIgnoreCase("/bukkit:pl") || cmd.equalsIgnoreCase("/bukkit:plugins")) {
            if (!e.getCommandSource().hasPermission("plhide.command.use")) {
                if (!PlatformPlugin.get().getConfig().pl_message().isEmpty()) {
                    Component response = LegacyComponentSerializer.legacy('&').deserialize(PlatformPlugin.get().getConfig().pl_message());
                    e.getCommandSource().sendMessage(response);
                }
                e.setResult(CommandExecuteEvent.CommandResult.denied());
            }
        }
    }

    @Subscribe
    public void onCommands(PlayerAvailableCommandsEvent e) {
        RootNodeCommandCompleter node = new RootNodeCommandCompleter(e.getRootNode());
        CompleterModifier.handleCompleter(node, core.getGroupForPlayer(e.getPlayer()), e.getPlayer().hasPermission(Constants.WHITELIST_MODE_PERMISSION));
    }
}
