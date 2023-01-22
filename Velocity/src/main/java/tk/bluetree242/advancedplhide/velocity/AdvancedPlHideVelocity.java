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

package tk.bluetree242.advancedplhide.velocity;

import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.simplix.protocolize.api.Protocolize;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.slf4j.Logger;
import tk.bluetree242.advancedplhide.Group;
import tk.bluetree242.advancedplhide.PlatformPlugin;
import tk.bluetree242.advancedplhide.impl.version.UpdateCheckResult;
import tk.bluetree242.advancedplhide.utils.Constants;
import tk.bluetree242.advancedplhide.velocity.listener.event.VelocityEventListener;
import tk.bluetree242.advancedplhide.velocity.listener.packet.VelocityPacketListener;

import javax.inject.Inject;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Plugin(id = "advancedplhide",
        name = "AdvancedPlHide",
        description = AdvancedPlHideVelocity.DESCRIPTION,
        version = AdvancedPlHideVelocity.VERSION,
        authors = {"BlueTree242"},
        dependencies = {@Dependency(id = "protocolize")})
public class AdvancedPlHideVelocity extends PlatformPlugin {
    public static final String DESCRIPTION = "{description}";
    public static final String VERSION = "{version}";
    public final ProxyServer server;
    public final Logger logger;
    public final Path dataDirectory;
    private final Metrics.Factory metricsFactory;
    private List<Group> groups = new ArrayList<>();

    @Inject
    public AdvancedPlHideVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory, Metrics.Factory metricsFactory) {
        this.metricsFactory = metricsFactory;
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        PlatformPlugin.setPlatform(this);
        initConfigManager();
    }

    private static byte[] readFully(InputStream input) throws IOException {
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        return output.toByteArray();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent e) {
        reloadConfig();
        CommandMeta meta = server.getCommandManager().metaBuilder("advancedplhidevelocity")
                // Specify other aliases (optional)
                .aliases("aphv", "apv", "plhidev", "phv")
                .build();
        server.getCommandManager().register(meta, new AdvancedPlHideCommand(this));
        server.getEventManager().register(this, new VelocityEventListener(this));
        Protocolize.listenerProvider().registerListener(new VelocityPacketListener(this));
        metricsFactory.make(this, 13708);
        server.getConsoleCommandSource().sendMessage(LegacyComponentSerializer.legacy('&').deserialize(Constants.startupMessage()));
        performStartUpdateCheck();
    }

    public Group getGroupForPlayer(Player player) {
        if (player.hasPermission("plhide.no-group")) return null;
        List<Group> groups = new ArrayList<>();
        for (Group group : getGroups()) {
            if (player.hasPermission("plhide.group." + group.getName())) {
                groups.add(group);
            }
        }
        return groups.isEmpty() ? getGroup("default") : mergeGroups(groups);
    }

    public void loadGroups() {
        groups = new ArrayList<>();
        getConfig().groups().forEach((name, val) -> {
            if (getGroup(name) == null)
                groups.add(new Group(name, val.tabcomplete()));
            else {
                getLogger().warn("Group " + name + " is repeated.");
            }
        });
        if (getGroup("default") == null) {
            getLogger().warn("Group default was not found. If someone has no permission for any group, no group applies on them");
        }

    }

    public Logger getLogger() {
        return logger;
    }

    public Group getGroup(String name) {
        for (Group group : groups) {
            if (group.getName().equals(name)) return group;
        }
        return null;
    }

    @Override
    public String getPluginForCommand(String s) {
        return null;
    }

    @Override
    public String getVersionConfig() {
        try {
            return new String(readFully(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("version-config.json"))));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public Type getType() {
        return Type.VELOCITY;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void performStartUpdateCheck() {
        UpdateCheckResult result = updateCheck();
        if (result == null) {
            getLogger().error("Could not check for updates");
            return;
        }
        String msg = result.getVersionsBehind() == 0 ?
                LegacyComponentSerializer.legacy('&').deserialize(Constants.DEFAULT_UP_TO_DATE).content() :
                LegacyComponentSerializer.legacy('&').deserialize(Constants.DEFAULT_BEHIND.replace("{versions}", result.getVersionsBehind() + "")
                        .replace("{download}", result.getUpdateUrl())).content();
        if (result.getMessage() != null) {
            msg = LegacyComponentSerializer.legacy('&').deserialize(result.getMessage()).content();
        }

        switch (result.getLoggerType()) {
            case "INFO":
                logger.info(msg);
                break;
            case "WARNING":
                logger.warn(msg);
                break;
            case "ERROR":
                logger.error(msg);
                break;
        }
    }

    @Override
    public File getDataFolder() {
        return dataDirectory.toFile();
    }


}
