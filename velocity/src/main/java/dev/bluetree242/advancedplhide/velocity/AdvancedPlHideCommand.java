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

package dev.bluetree242.advancedplhide.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import dev.bluetree242.advancedplhide.PlatformPlugin;
import dev.bluetree242.advancedplhide.exceptions.ConfigurationLoadException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AdvancedPlHideCommand implements SimpleCommand {
    private final AdvancedPlHideVelocity core;

    public AdvancedPlHideCommand(AdvancedPlHideVelocity core) {
        this.core = core;
    }

    @Override
    public void execute(Invocation e) {
        String[] args = e.arguments();
        CommandSource sender = e.source();
        if (args.length == 0) {
            sender.sendMessage(LegacyComponentSerializer.legacy('&').deserialize("&aRunning AdvancedPlHide v.&e" + AdvancedPlHideVelocity.VERSION));
            return;
        } else {
            if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("plhide.reload")) {
                    sender.sendMessage(Component.text("You don't have permission to run this command").color(NamedTextColor.RED));
                } else {
                    core.server.getScheduler().buildTask(core, () -> {
                        try {
                            PlatformPlugin.get().reloadConfig();
                            sender.sendMessage(Component.text("Configuration Reloaded").color(NamedTextColor.GREEN));
                        } catch (ConfigurationLoadException ev) {
                            sender.sendMessage(Component.text("Could not reload " + ev.getConfigName()).color(NamedTextColor.RED));
                        }
                    }).schedule();
                }
                return;
            }
        }
        sender.sendMessage(Component.text("SubCommand not found").color(NamedTextColor.RED));
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        return CompletableFuture.supplyAsync(() -> {
            List<String> result = new ArrayList<>();
            if (invocation.arguments().length <= 2) {
                List<String> arg1 = new ArrayList<>();
                if (invocation.source().hasPermission("plhide.reload")) {
                    arg1.add("reload");
                }
                for (String s : arg1) {
                    if (invocation.arguments().length != 0) {
                        if (s.startsWith(invocation.arguments()[0])) {
                            result.add(s);
                        }
                    } else {
                        result.add(s);
                    }
                }
            }
            return result;
        });
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return SimpleCommand.super.hasPermission(invocation);
    }
}
