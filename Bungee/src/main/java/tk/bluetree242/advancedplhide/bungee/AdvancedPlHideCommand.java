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

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import tk.bluetree242.advancedplhide.Platform;
import tk.bluetree242.advancedplhide.exceptions.ConfigurationLoadException;

import java.util.concurrent.TimeUnit;

public class AdvancedPlHideCommand extends Command {
    private final AdvancedPlHideBungee core;

    public AdvancedPlHideCommand(AdvancedPlHideBungee core) {
        super("advancedplhidebungee", null, "aphb", "plhideb");
        this.core = core;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GREEN + "Running AdvancedPlHide v." + ChatColor.YELLOW + core.getDescription().getVersion());
            return;
        } else {
            if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("plhide.reload")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission to run this command");
                } else {
                    ProxyServer.getInstance().getScheduler().schedule(core, () -> {
                        try {
                            Platform.get().reloadConfig();
                            sender.sendMessage(ChatColor.GREEN + "Configuration Reloaded");
                        } catch (ConfigurationLoadException e) {
                            sender.sendMessage(ChatColor.RED + "Could not reload " + e.getConfigName());
                        }
                    }, 0, TimeUnit.SECONDS);
                }
                return;
            }
        }
        sender.sendMessage(ChatColor.RED + "SubCommand not found");
    }
}
