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

package tk.bluetree242.advancedplhide.velocity.listener.packet;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.proxy.protocol.packet.TabCompleteRequest;
import com.velocitypowered.proxy.protocol.packet.TabCompleteResponse;
import dev.simplix.protocolize.api.Direction;
import dev.simplix.protocolize.api.Protocolize;
import dev.simplix.protocolize.api.listener.AbstractPacketListener;
import dev.simplix.protocolize.api.listener.PacketReceiveEvent;
import dev.simplix.protocolize.api.listener.PacketSendEvent;
import tk.bluetree242.advancedplhide.CompleterModifier;
import tk.bluetree242.advancedplhide.utils.UsedMap;
import tk.bluetree242.advancedplhide.velocity.AdvancedPlHideVelocity;
import tk.bluetree242.advancedplhide.velocity.VelocityPlayer;
import tk.bluetree242.advancedplhide.velocity.impl.completer.OfferCompleterList;
import tk.bluetree242.advancedplhide.velocity.impl.subcompleter.OfferSubCommandCompleterList;

import java.util.UUID;

public class VelocityPacketListener extends AbstractPacketListener<TabCompleteResponse> {
    private final UsedMap<UUID, String> commandsWaiting = new UsedMap<>();
    private final AdvancedPlHideVelocity core;

    public VelocityPacketListener(AdvancedPlHideVelocity core) {
        super(TabCompleteResponse.class, Direction.UPSTREAM, 0);
        this.core = core;
        Protocolize.listenerProvider().registerListener(new VelocityPacketListener.RequestListener());
    }


    @Override
    public void packetReceive(PacketReceiveEvent<TabCompleteResponse> e) {
        //it is impossible to get this packet
    }

    @Override
    public void packetSend(PacketSendEvent<TabCompleteResponse> e) {
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
                CompleterModifier.handleCompleter(list, new VelocityPlayer(core, player));
            } else {
                OfferSubCommandCompleterList list = new OfferSubCommandCompleterList(e.packet().getOffers(), notCompleted);
                CompleterModifier.handleSubCompleter(list, new VelocityPlayer(core, player));
                if (list.isCancelled()) e.cancelled(true);
            }
        } else {
            if ((!notCompleted.contains(" "))) {
                OfferCompleterList list = new OfferCompleterList(e.packet().getOffers(), false);
                CompleterModifier.handleCompleter(list, new VelocityPlayer(core, player));
            } else {
                OfferSubCommandCompleterList list = new OfferSubCommandCompleterList(e.packet().getOffers(), notCompleted);
                CompleterModifier.handleSubCompleter(list, new VelocityPlayer(core, player));
                if (list.isCancelled()) e.cancelled(true);
            }
        }
    }

    public class RequestListener extends AbstractPacketListener<TabCompleteRequest> {

        protected RequestListener() {
            super(TabCompleteRequest.class, Direction.UPSTREAM, Integer.MAX_VALUE);
        }

        @Override
        public void packetReceive(PacketReceiveEvent<TabCompleteRequest> e) {
            if (!e.cancelled())
                commandsWaiting.put(e.player().uniqueId(), e.packet().getCommand());
        }

        @Override
        public void packetSend(PacketSendEvent<TabCompleteRequest> e) {

        }
    }
}
