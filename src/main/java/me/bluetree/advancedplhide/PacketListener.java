package me.bluetree.advancedplhide;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.server.TemporaryPlayer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.function.Consumer;

public class PacketListener extends PacketAdapter {
    private    AdvancedPlHide core;
    public PacketListener(AdvancedPlHide core) {
        super(core, ListenerPriority.NORMAL, PacketType.Play.Server.TAB_COMPLETE,PacketType.Play.Client.TAB_COMPLETE, PacketType.Play.Server.COMMANDS);
        this.core = core;
    }
    private final HashMap<UUID, String> incomingCommand = new HashMap<>();

    @Override
    public void onPacketSending(PacketEvent e) {
        if (e.getPlayer() == null || e.getPlayer() instanceof TemporaryPlayer) return;
        PacketContainer packetContainer = e.getPacket();
        if (e.getPacketType() == PacketType.Play.Server.TAB_COMPLETE) {
            if (core.isLegacy()) {
                StructureModifier<String[]> matchModifier = packetContainer.getSpecificModifier(String[].class);
                String[] matchedCommands = matchModifier.read(0);
                ArrayList<String> allowedCommands = new ArrayList(Arrays.asList((Object[])matchedCommands));
                String str = this.incomingCommand.get(e.getPlayer().getUniqueId());
                if (str.contains(" ") || !str.startsWith("/")) return;

                for (String matchedCommand : new ArrayList<>(allowedCommands)) {
                    String[] split = matchedCommand.split(":");
                    if (split.length >= 2) {
                        allowedCommands.remove(matchedCommand);
                    }
                }
                removeBlacklist(allowedCommands, str, e.getPlayer());
                matchModifier.write(0, allowedCommands.toArray(new String[allowedCommands.size()]));
            } else {
                StructureModifier<Suggestions> matchModifier = packetContainer.getSpecificModifier(Suggestions.class);
                Suggestions matchedCommands = matchModifier.read(0);
            Suggestions allowedCommands = new Suggestions(matchedCommands.getRange(), new ArrayList<>(matchedCommands.getList()));
            if (matchedCommands.getRange().getStart() != 1) return;
            for (Suggestion matchedCommand : matchedCommands.getList()) {
                String[] split = matchedCommand.getText().split(":");
                if (split.length >= 2) {
                    allowedCommands.getList().remove(matchedCommand);
                }
                removeBlacklist(allowedCommands, e.getPlayer());
            }

                matchModifier.write(0, allowedCommands);
            }
        } else {

            StructureModifier<RootCommandNode> matchModifier = packetContainer.getSpecificModifier(RootCommandNode.class);
            RootCommandNode matchedCommands = matchModifier.read(0);
            Collection map = matchedCommands.getChildren();
            for (Object o : Arrays.asList(map.toArray(new Object[map.size()]))) {
                CommandNode matchedCommand = (CommandNode) o;
                String[] split = matchedCommand.getName().split(":");
                if (split.length >= 2) {
                    map.remove(matchedCommand);
                }
            }
            removeBlacklist(map, Arrays.asList(map.toArray(new Object[map.size()])), e.getPlayer());
        }
    }

    public void onPacketReceiving(PacketEvent paramPacketEvent) {
        if (paramPacketEvent.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) {
            if (core.isLegacy()) {
                this.incomingCommand.put(paramPacketEvent.getPlayer().getUniqueId(), paramPacketEvent.getPacket().getStrings()
                        .read(0).trim());
            }
        }
    }

    private void  removeBlacklist(List<String> commands,String prefix , Player player) {
        if (player.hasPermission("plhide.bypassblacklist")) return;
        if (core.config.blacklist()) {
            core.config.groups().forEach((key, val) -> {
                if (player.hasPermission("plhide.group." + key.toLowerCase())) {
                    for (String s : val.blacklist) {
                        for (String suggestion : commands.toArray(new String[0])) {

                            if (suggestion.equalsIgnoreCase(s)|| (prefix + suggestion).equalsIgnoreCase(s)) {
                                commands.remove(suggestion);
                            }
                        }
                    }
                }
            });
        } else {
            for (String suggestion : commands.toArray(new String[0])) {
                for (String s : core.config.groups().keySet()) {
                    Group group = core.config.groups().get(s);
                    if (!group.blacklist.contains(suggestion) || group.blacklist.contains(prefix + suggestion)) {
                        if (player.hasPermission("plhide.group." + s))
                            commands.remove(suggestion);
                    }
                }

            }
        }
    }

    private void removeBlacklist(Collection realSuggestions, Collection suggestions, Player player) {
        if (player.hasPermission("plhide.bypassblacklist")) return;
        if (core.config.blacklist()) {
            core.config.groups().forEach((key, val) -> {
                if (player.hasPermission("plhide.group." + key.toLowerCase())) {
                    for (String s : val.blacklist) {
                        for (Object st : suggestions) {
                            CommandNode suggestion = (CommandNode) st;

                            if (suggestion.getName().equalsIgnoreCase(s) ) {
                                realSuggestions.remove(suggestion);
                            }
                        }
                    }
                }
            });
        } else {
            for (Object st : suggestions) {
                CommandNode suggestion = (CommandNode) st;
                for (String s : core.config.groups().keySet()) {
                    Group group = core.config.groups().get(s);
                    if (!group.blacklist.contains(suggestion.getName())) {
                        if (player.hasPermission("plhide.group." + s))
                            realSuggestions.remove(suggestion);
                    }
                }

            }
        }
    }

    private void removeBlacklist(Suggestions suggestions, Player player) {
        if (player.hasPermission("plhide.bypassblacklist")) return;
        if (core.config.blacklist()) {
            core.config.groups().forEach((key, val) -> {
                if (player.hasPermission("plhide.group." + key.toLowerCase())) {
                    for (String s : val.blacklist) {
                        for (Suggestion suggestion : suggestions.getList().toArray(new Suggestion[0])) {
                            if (suggestion.getText().equalsIgnoreCase(s)) {
                                suggestions.getList().remove(suggestion);
                            }
                        }
                    }
                }
            });
        } else {
            for (Suggestion suggestion : suggestions.getList().toArray(new Suggestion[0])) {
                for (String s : core.config.groups().keySet()) {
                    Group group = core.config.groups().get(s);
                    if (!group.blacklist.contains(suggestion.getText())) {
                        if (player.hasPermission("plhide.group." + s))
                        suggestions.getList().remove(suggestion);
                    }
                }

            }
        }
    }
}
