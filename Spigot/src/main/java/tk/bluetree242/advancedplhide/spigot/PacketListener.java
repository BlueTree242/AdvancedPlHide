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

package tk.bluetree242.advancedplhide.spigot;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.RootCommandNode;
import tk.bluetree242.advancedplhide.CompleterModifier;
import tk.bluetree242.advancedplhide.impl.RootNodeCommandCompleter;
import tk.bluetree242.advancedplhide.impl.SuggestionCommandCompleterList;
import tk.bluetree242.advancedplhide.spigot.impl.cmd.StringCommandCompleterList;
import tk.bluetree242.advancedplhide.spigot.impl.group.SpigotGroup;

import java.util.HashMap;
import java.util.UUID;

public class PacketListener extends PacketAdapter {

    private final HashMap<UUID, String> commandsWaiting = new HashMap<>();
    private AdvancedPlHideSpigot core;

    public PacketListener(AdvancedPlHideSpigot core) {
        super(core, ListenerPriority.NORMAL, PacketType.Play.Server.TAB_COMPLETE, PacketType.Play.Client.TAB_COMPLETE, PacketType.Play.Server.COMMANDS);
        this.core = core;
    }

    public void onPacketSending(PacketEvent e) {

        if (e.getPacketType() == PacketType.Play.Server.TAB_COMPLETE) {
            onTabcomplete(e);
        } else if (e.getPacketType() == PacketType.Play.Server.COMMANDS) {
            onCommands(e);
        }
    }

    private void onTabcomplete(PacketEvent e) {
        if (!core.isLegacy()) {
            StructureModifier<Suggestions> matchModifier = e.getPacket().getSpecificModifier(Suggestions.class);
            Suggestions suggestionsOrigin = matchModifier.read(0);
            SuggestionCommandCompleterList suggestions = new SuggestionCommandCompleterList(suggestionsOrigin);
            if (suggestionsOrigin.getRange().getStart() == 1) {
                CompleterModifier.handleCompleter(suggestions, SpigotGroup.forPlayer(e.getPlayer()), e.getPlayer().hasPermission("plhide.blacklist-mode"));
            }
            matchModifier.write(0, suggestions.export());
        } else {
            StructureModifier<String[]> matchModifier = e.getPacket().getSpecificModifier(String[].class);
            String[] suggestionsOrigin = matchModifier.read(0);
            StringCommandCompleterList suggestions = new StringCommandCompleterList(suggestionsOrigin);
            String str = this.commandsWaiting.get(e.getPlayer().getUniqueId());
            if (!str.contains(" ") && str.startsWith("/")) {
                CompleterModifier.handleCompleter(suggestions, SpigotGroup.forPlayer(e.getPlayer()), e.getPlayer().hasPermission("plhide.blacklist-mode"));
            }
            matchModifier.write(0, suggestions.export());
        }
    }

    private void onCommands(PacketEvent e) {
        StructureModifier<RootCommandNode> matchModifier = e.getPacket().getSpecificModifier(RootCommandNode.class);
        RootCommandNode nodeOrigin = matchModifier.read(0);
        RootNodeCommandCompleter node = new RootNodeCommandCompleter(nodeOrigin);
        CompleterModifier.handleCompleter(node, SpigotGroup.forPlayer(e.getPlayer()), e.getPlayer().hasPermission("plhide.blacklist-mode"));
        matchModifier.write(0, node.export());
    }

    public void onPacketReceiving(PacketEvent e) {
        if (e.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) {
            if (core.isLegacy()) {
                this.commandsWaiting.put(e.getPlayer().getUniqueId(), e.getPacket().getStrings()
                        .read(0).trim());
            }
        }
    }
}
