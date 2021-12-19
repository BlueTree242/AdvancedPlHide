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

package tk.bluetree242.advancedplhide.velocity;

import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.command.PlayerAvailableCommandsEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.simplix.protocolize.api.Protocolize;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.slf4j.Logger;
import tk.bluetree242.advancedplhide.CompleterModifier;
import tk.bluetree242.advancedplhide.Platform;
import tk.bluetree242.advancedplhide.config.ConfManager;
import tk.bluetree242.advancedplhide.config.Config;
import tk.bluetree242.advancedplhide.exceptions.ConfigurationLoadException;
import tk.bluetree242.advancedplhide.impl.RootNodeCommandCompleter;

import javax.inject.Inject;
import java.nio.file.Path;

@Plugin(id = "advancedplhide",
        name = "AdvancedPlHide",
        description = AdvancedPlHideVelocity.DESCRIPTION,
        version = AdvancedPlHideVelocity.VERSION,
        authors = {"BlueTree242"},
        dependencies = {@Dependency(id = "protocolize")})
public class AdvancedPlHideVelocity extends Platform{
    public static final String DESCRIPTION = "{description}";
    public static final String VERSION = "{version}";
    public final ProxyServer server;
    public final Logger logger;
    public final Path dataDirectory;
    public Config config;
    protected ConfManager<Config> confManager;
    @Inject
    public AdvancedPlHideVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        confManager = ConfManager.create(dataDirectory, "config.yml", Config.class);
        Platform.setPlatform(this);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent e) {
        reloadConfig();
        CommandMeta meta = server.getCommandManager().metaBuilder("advancedplhidevelocity")
                // Specify other aliases (optional)
                .aliases("aphv", "apv", "plhidev", "phv")
                .build();
        server.getCommandManager().register(meta, new AdvancedPlHideCommand(this));
        Protocolize.listenerProvider().registerListener(new PacketListener());

    }

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public void reloadConfig() throws ConfigurationLoadException {
        confManager.reloadConfig();
        config = confManager.getConfigData();
    }

    @Subscribe
    public void onCommandExecute(CommandExecuteEvent e) {
        if (e.getCommandSource() instanceof ConsoleCommandSource) return;
        String cmd = "/" + e.getCommand().split(" ")[0];
        if (cmd.equalsIgnoreCase("/plugins") || cmd.equalsIgnoreCase("/pl") || cmd.equalsIgnoreCase("/bukkit:pl") || cmd.equalsIgnoreCase("/bukkit:plugins")) {
            if (!e.getCommandSource().hasPermission("plhide.command.use")) {
                Component response =  LegacyComponentSerializer.legacy('&').deserialize(config.pl_message());
                e.getCommandSource().sendMessage(response);
                e.setResult(CommandExecuteEvent.CommandResult.denied());
            }
        }
    }

    @Subscribe
    public void onCommands(PlayerAvailableCommandsEvent e) {
        RootNodeCommandCompleter node = new RootNodeCommandCompleter(e.getRootNode());
        CompleterModifier.handleCompleter(node);
    }





}
