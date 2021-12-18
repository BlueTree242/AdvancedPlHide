package tk.bluetree242.advancedplhide.velocity;

import com.mojang.brigadier.tree.CommandNode;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.command.PlayerAvailableCommandsEvent;
import com.velocitypowered.api.event.player.TabCompleteEvent;
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
import tk.bluetree242.advancedplhide.impl.RootCommandCompleter;
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
    public void onProxyInitialization(ProxyInitializeEvent event) {
        confManager.reloadConfig();
        config = confManager.getConfigData();
        Protocolize.listenerProvider().registerListener(new PacketListener());
    }

    @Override
    public Config getConfig() {
        return config;
    }

    @Subscribe
    public void onCommandExecute(CommandExecuteEvent e) {
        if (e.getCommandSource() instanceof ConsoleCommandSource) return;
        String msg = "/" + e.getCommand();
        if (msg.startsWith("/plugins") || msg.startsWith("/pl") || msg.startsWith("/bukkit:pl") || msg.startsWith("/bukkit:plugins")) {
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
