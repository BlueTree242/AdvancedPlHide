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

package tk.bluetree242.advancedplhide.bungee;

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
import tk.bluetree242.advancedplhide.Platform;
import tk.bluetree242.advancedplhide.bungee.impl.completer.StringCommandCompleterList;
import tk.bluetree242.advancedplhide.impl.completer.RootNodeCommandCompleter;
import tk.bluetree242.advancedplhide.impl.completer.SelfExpiringHashMap;
import tk.bluetree242.advancedplhide.impl.completer.SuggestionCommandCompleterList;

import java.util.UUID;

public class PacketListener extends AbstractPacketListener<TabCompleteResponse> {
    private final SelfExpiringHashMap<UUID, String> commandsWaiting = new SelfExpiringHashMap<>();
    private final AdvancedPlHideBungee core;

    protected PacketListener(AdvancedPlHideBungee core) {
        super(TabCompleteResponse.class, Direction.UPSTREAM, 0);
        this.core = core;
        Protocolize.listenerProvider().registerListener(new PacketListener.RequestListener());
        Protocolize.listenerProvider().registerListener(new PacketListener.CommandsListener());
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
        if (legacy) {
            String str = commandsWaiting.get(e.player().uniqueId());
            if (str == null) str = "/";
            if (!str.contains(" ") && str.startsWith("/")) {
                StringCommandCompleterList list = new StringCommandCompleterList(packet.getCommands());
                CompleterModifier.handleCompleter(list, AdvancedPlHideBungee.getGroupForPlayer(player), player.hasPermission("plhide.blacklist-mode"));
            }
        } else {
            if (packet.getSuggestions().getRange().getStart() == 1) {
                SuggestionCommandCompleterList list = new SuggestionCommandCompleterList(packet.getSuggestions());
                CompleterModifier.handleCompleter(list, AdvancedPlHideBungee.getGroupForPlayer(player), player.hasPermission("plhide.blacklist-mode"));
            }
        }
    }

    public class RequestListener extends AbstractPacketListener<TabCompleteRequest> {

        protected RequestListener() {
            super(TabCompleteRequest.class, Direction.UPSTREAM, 0);
        }

        @Override
        public void packetReceive(PacketReceiveEvent<TabCompleteRequest> e) {
            boolean legacy = e.player().protocolVersion() <= 340;
            if (legacy) {
                commandsWaiting.put(e.player().uniqueId(), e.packet().getCursor(), 60000);
            }
        }

        @Override
        public void packetSend(PacketSendEvent<TabCompleteRequest> e) {

        }
    }

    public class CommandsListener extends AbstractPacketListener<Commands> {

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
            CompleterModifier.handleCompleter(completer, AdvancedPlHideBungee.getGroupForPlayer(player), player.hasPermission("plhide.blacklist-mode"));
        }
    }
}

