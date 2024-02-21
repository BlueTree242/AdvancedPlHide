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

package dev.bluetree242.advancedplhide.velocity.listener.packet;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.proxy.protocol.packet.TabCompleteRequestPacket;
import com.velocitypowered.proxy.protocol.packet.TabCompleteResponsePacket;
import dev.bluetree242.advancedplhide.CompleterModifier;
import dev.bluetree242.advancedplhide.utils.Constants;
import dev.bluetree242.advancedplhide.utils.UsedMap;
import dev.bluetree242.advancedplhide.velocity.AdvancedPlHideVelocity;
import dev.bluetree242.advancedplhide.velocity.impl.completer.OfferCompleterList;
import dev.bluetree242.advancedplhide.velocity.impl.subcompleter.OfferSubCommandCompleterList;
import dev.simplix.protocolize.api.Direction;
import dev.simplix.protocolize.api.Protocolize;
import dev.simplix.protocolize.api.listener.AbstractPacketListener;
import dev.simplix.protocolize.api.listener.PacketReceiveEvent;
import dev.simplix.protocolize.api.listener.PacketSendEvent;

import java.util.UUID;

public class VelocityPacketListener extends AbstractPacketListener<TabCompleteResponsePacket> {
    private final UsedMap<UUID, String> commandsWaiting = new UsedMap<>();
    private final AdvancedPlHideVelocity core;

    public VelocityPacketListener(AdvancedPlHideVelocity core) {
        super(TabCompleteResponsePacket.class, Direction.UPSTREAM, 0);
        this.core = core;
        Protocolize.listenerProvider().registerListener(new VelocityPacketListener.RequestListener());
    }


    @Override
    public void packetReceive(PacketReceiveEvent<TabCompleteResponsePacket> e) {
        //it is impossible to get this packet
    }

    @Override
    public void packetSend(PacketSendEvent<TabCompleteResponsePacket> e) {
        boolean legacy = e.player().protocolVersion() <= 340;
        Player player = core.server.getPlayer(e.player().uniqueId()).orElse(null);
        if (player == null || !player.isActive()) {
            e.cancelled(true);
            return;
        }
        String notCompleted = commandsWaiting.get(e.player().uniqueId());
        if (notCompleted == null) notCompleted = "/";
        if (!notCompleted.trim().startsWith("/")) notCompleted = "/" + notCompleted;
        if (legacy) {
            if (!notCompleted.contains(" ")) {
                OfferCompleterList list = new OfferCompleterList(e.packet().getOffers(), true);
                CompleterModifier.handleCompleter(list, core.getGroupForPlayer(player), player.hasPermission(Constants.WHITELIST_MODE_PERMISSION));
            } else {
                OfferSubCommandCompleterList list = new OfferSubCommandCompleterList(e.packet().getOffers(), notCompleted);
                CompleterModifier.handleSubCompleter(list, core.getGroupForPlayer(player), player.hasPermission(Constants.WHITELIST_MODE_PERMISSION));
                if (list.isCancelled()) e.cancelled(true);
            }
        } else {
            if ((!notCompleted.contains(" "))) {
                OfferCompleterList list = new OfferCompleterList(e.packet().getOffers(), false);
                CompleterModifier.handleCompleter(list, core.getGroupForPlayer(player), player.hasPermission(Constants.WHITELIST_MODE_PERMISSION));
            } else {
                OfferSubCommandCompleterList list = new OfferSubCommandCompleterList(e.packet().getOffers(), notCompleted);
                CompleterModifier.handleSubCompleter(list, core.getGroupForPlayer(player), player.hasPermission(Constants.WHITELIST_MODE_PERMISSION));
                if (list.isCancelled()) e.cancelled(true);
            }
        }
    }

    public class RequestListener extends AbstractPacketListener<TabCompleteRequestPacket> {

        protected RequestListener() {
            super(TabCompleteRequestPacket.class, Direction.UPSTREAM, Integer.MAX_VALUE);
        }

        @Override
        public void packetReceive(PacketReceiveEvent<TabCompleteRequestPacket> e) {
            if (!e.cancelled())
                commandsWaiting.put(e.player().uniqueId(), e.packet().getCommand());
        }

        @Override
        public void packetSend(PacketSendEvent<TabCompleteRequestPacket> e) {

        }
    }
}
