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

package tk.bluetree242.advancedplhide.bungee.listener.packet;

import dev.simplix.protocolize.api.Direction;
import dev.simplix.protocolize.api.Protocolize;
import dev.simplix.protocolize.api.listener.AbstractPacketListener;
import dev.simplix.protocolize.api.listener.PacketReceiveEvent;
import dev.simplix.protocolize.api.listener.PacketSendEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.packet.Commands;
import net.md_5.bungee.protocol.packet.TabCompleteRequest;
import net.md_5.bungee.protocol.packet.TabCompleteResponse;
import tk.bluetree242.advancedplhide.CompleterModifier;
import tk.bluetree242.advancedplhide.bungee.AdvancedPlHideBungee;
import tk.bluetree242.advancedplhide.bungee.impl.completer.StringCommandCompleterList;
import tk.bluetree242.advancedplhide.bungee.impl.subcompleter.StringSubCommandCompleterList;
import tk.bluetree242.advancedplhide.impl.completer.RootNodeCommandCompleter;
import tk.bluetree242.advancedplhide.impl.completer.SuggestionCommandCompleterList;
import tk.bluetree242.advancedplhide.impl.subcompleter.SuggestionSubCommandCompleterList;
import tk.bluetree242.advancedplhide.utils.Constants;
import tk.bluetree242.advancedplhide.utils.UsedMap;

import java.util.UUID;

public class BungeePacketListener extends AbstractPacketListener<TabCompleteResponse> {
    private final UsedMap<UUID, String> commandsWaiting = new UsedMap<>();

    public BungeePacketListener() {
        super(TabCompleteResponse.class, Direction.UPSTREAM, 0);
        Protocolize.listenerProvider().registerListener(new BungeePacketListener.RequestListener());
        Protocolize.listenerProvider().registerListener(new CommandsListener());
    }


    @Override
    public void packetReceive(PacketReceiveEvent<TabCompleteResponse> e) {
        //it is impossible to get this packet
    }

    @Override
    public void packetSend(PacketSendEvent<TabCompleteResponse> e) {
        boolean legacy = e.player().protocolVersion() <= 340;
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(e.player().uniqueId());
        if (player == null || !player.isConnected()) {
            e.cancelled(true);
            return;
        }
        TabCompleteResponse packet = e.packet();
        String notCompleted = this.commandsWaiting.get(e.player().uniqueId());
        if (notCompleted == null) notCompleted = "/";
        if (!notCompleted.trim().startsWith("/")) notCompleted = "/" + notCompleted;
        if (legacy) {
            if (!notCompleted.contains(" ")) {
                StringCommandCompleterList list = new StringCommandCompleterList(packet.getCommands());
                CompleterModifier.handleCompleter(list, AdvancedPlHideBungee.getGroupForPlayer(player), player.hasPermission(Constants.WHITELIST_MODE_PERMISSION));
            } else {
                StringSubCommandCompleterList list = new StringSubCommandCompleterList(packet.getCommands(), notCompleted);
                CompleterModifier.handleSubCompleter(list, AdvancedPlHideBungee.getGroupForPlayer(player), player.hasPermission(Constants.SUB_WHITELIST_MODE_PERMISSION));
                if (list.isCancelled()) e.cancelled(true);
            }
        } else {
            if (!notCompleted.contains(" ")) {
                SuggestionCommandCompleterList list = new SuggestionCommandCompleterList(packet.getSuggestions());
                CompleterModifier.handleCompleter(list, AdvancedPlHideBungee.getGroupForPlayer(player), player.hasPermission("plhide.whitelist-mode"));
            } else {
                SuggestionSubCommandCompleterList suggestions = new SuggestionSubCommandCompleterList(e.packet().getSuggestions(), notCompleted);
                CompleterModifier.handleSubCompleter(suggestions, AdvancedPlHideBungee.getGroupForPlayer(player), player.hasPermission(Constants.SUB_WHITELIST_MODE_PERMISSION));
                if (suggestions.isCancelled()) e.cancelled(true);
            }
        }
    }

    public static class CommandsListener extends AbstractPacketListener<Commands> {

        protected CommandsListener() {
            super(Commands.class, Direction.UPSTREAM, 0);
        }

        @Override
        public void packetReceive(PacketReceiveEvent<Commands> e) {

        }

        @Override
        public void packetSend(PacketSendEvent<Commands> e) {
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(e.player().uniqueId());
            if (player == null || !player.isConnected()) {
                e.cancelled(true);
                return;
            }
            Commands packet = e.packet();
            RootNodeCommandCompleter completer = new RootNodeCommandCompleter(packet.getRoot());
            CompleterModifier.handleCompleter(completer, AdvancedPlHideBungee.getGroupForPlayer(player), player.hasPermission(Constants.WHITELIST_MODE_PERMISSION));
        }
    }

    public class RequestListener extends AbstractPacketListener<TabCompleteRequest> {

        protected RequestListener() {
            super(TabCompleteRequest.class, Direction.UPSTREAM, Integer.MAX_VALUE);
        }

        @Override
        public void packetReceive(PacketReceiveEvent<TabCompleteRequest> e) {
            if (!e.cancelled())
                commandsWaiting.put(e.player().uniqueId(), e.packet().getCursor());
        }

        @Override
        public void packetSend(PacketSendEvent<TabCompleteRequest> e) {

        }
    }
}

