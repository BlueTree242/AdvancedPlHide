package tk.bluetree242.advancedplhide.bukkit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.mojang.brigadier.suggestion.Suggestions;
import tk.bluetree242.advancedplhide.CompleterModifier;
import tk.bluetree242.advancedplhide.bukkit.impl.StringCommandCompleterList;
import tk.bluetree242.advancedplhide.bukkit.impl.SuggestionCommandCompleterList;

public class PacketListener extends PacketAdapter {

    private AdvancedPlHideBukkit core;

    public PacketListener(AdvancedPlHideBukkit core) {
        super(core, ListenerPriority.NORMAL, PacketType.Play.Server.TAB_COMPLETE, PacketType.Play.Client.TAB_COMPLETE, PacketType.Play.Server.COMMANDS);
        this.core = core;
    }

    public void onPacketSending(PacketEvent e) {
        if (e.getPacketType() == PacketType.Play.Server.TAB_COMPLETE) {
            onTabcomplete(e);
        } else if (e.getPacketType() == PacketType.Play.Server.COMMANDS) {

        }
    }

    private void onTabcomplete(PacketEvent e) {
        if (!core.isLegacy()) {
            StructureModifier<Suggestions> matchModifier = e.getPacket().getSpecificModifier(Suggestions.class);
            Suggestions suggestionsOrigin = matchModifier.read(0);
            SuggestionCommandCompleterList suggestions = new SuggestionCommandCompleterList(suggestionsOrigin);
            if (suggestionsOrigin.getRange().getStart() == 1) {
                CompleterModifier.removePluginPrefix(suggestions);
            }
            matchModifier.write(0, suggestions.export());
        } else {
            StructureModifier<String[]> matchModifier = e.getPacket().getSpecificModifier(String[].class);
            String[] suggestionsOrigin = matchModifier.read(0);
            StringCommandCompleterList suggestions = new StringCommandCompleterList(suggestionsOrigin);
            CompleterModifier.removePluginPrefix(suggestions);
            matchModifier.write(0, suggestions.export());
        }
    }

    public void onPacketReceiving(PacketEvent e) {

    }
}
