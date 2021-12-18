package tk.bluetree242.advancedplhide.velocity;

import com.velocitypowered.proxy.protocol.packet.TabCompleteRequest;
import com.velocitypowered.proxy.protocol.packet.TabCompleteResponse;
import dev.simplix.protocolize.api.Direction;
import dev.simplix.protocolize.api.Protocolize;
import dev.simplix.protocolize.api.listener.AbstractPacketListener;
import dev.simplix.protocolize.api.listener.PacketReceiveEvent;
import dev.simplix.protocolize.api.listener.PacketSendEvent;
import tk.bluetree242.advancedplhide.CompleterModifier;
import tk.bluetree242.advancedplhide.velocity.impl.OfferCompleterList;

public class PacketListener extends AbstractPacketListener<TabCompleteResponse> {
    protected PacketListener() {
        super(TabCompleteResponse.class, Direction.UPSTREAM, 0);
        Protocolize.listenerProvider().registerListener(new PacketListener.RequestListener());
    }

    @Override
    public void packetReceive(PacketReceiveEvent<TabCompleteResponse> e) {
        //we don't need this currently
    }

    @Override
    public void packetSend(PacketSendEvent<TabCompleteResponse> e) {
        if (e.packet().getStart() == 1) {
            OfferCompleterList list = new OfferCompleterList(e.packet().getOffers());
            CompleterModifier.handleCompleter(list);
        }
    }

    public class RequestListener extends AbstractPacketListener<TabCompleteRequest>{

        protected RequestListener() {
            super(TabCompleteRequest.class, Direction.UPSTREAM, 0);
        }

        @Override
        public void packetReceive(PacketReceiveEvent<TabCompleteRequest> packetReceiveEvent) {
        }

        @Override
        public void packetSend(PacketSendEvent<TabCompleteRequest> packetSendEvent) {

        }
    }
}
