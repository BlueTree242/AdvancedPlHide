package tk.bluetree242.advancedplhide.spigot;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.RootCommandNode;
import tk.bluetree242.advancedplhide.CompleterModifier;
import tk.bluetree242.advancedplhide.impl.RootCommandCompleter;
import tk.bluetree242.advancedplhide.impl.RootNodeCommandCompleter;
import tk.bluetree242.advancedplhide.spigot.impl.cmd.StringCommandCompleterList;
import tk.bluetree242.advancedplhide.spigot.impl.cmd.SuggestionCommandCompleterList;

import java.util.HashMap;
import java.util.UUID;

public class PacketListener extends PacketAdapter {

    private AdvancedPlHideSpigot core;
    private final HashMap<UUID, String> commandsWaiting = new HashMap<>();
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
                CompleterModifier.handleCompleter(suggestions);
            }
            matchModifier.write(0, suggestions.export());
        } else {
            StructureModifier<String[]> matchModifier = e.getPacket().getSpecificModifier(String[].class);
            String[] suggestionsOrigin = matchModifier.read(0);
            StringCommandCompleterList suggestions = new StringCommandCompleterList(suggestionsOrigin);
            String str = this.commandsWaiting.get(e.getPlayer().getUniqueId());
            if (!str.contains(" ") && str.startsWith("/")) {
                CompleterModifier.handleCompleter(suggestions);
            }
            matchModifier.write(0, suggestions.export());
        }
    }

    private void onCommands(PacketEvent e) {
        StructureModifier<RootCommandNode> matchModifier = e.getPacket().getSpecificModifier(RootCommandNode.class);
        RootCommandNode nodeOrigin = matchModifier.read(0);
        RootNodeCommandCompleter node = new RootNodeCommandCompleter(nodeOrigin);
        CompleterModifier.handleCompleter(node);
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
