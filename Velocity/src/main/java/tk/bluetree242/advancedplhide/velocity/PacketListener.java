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

import java.util.HashMap;
import java.util.UUID;

public class PacketListener extends AbstractPacketListener<TabCompleteResponse> {
    protected PacketListener() {
        super(TabCompleteResponse.class, Direction.UPSTREAM, 0);
        Protocolize.listenerProvider().registerListener(new PacketListener.RequestListener());
    }
    private final HashMap<UUID, String> commandsWaiting = new HashMap<>();

    @Override
    public void packetReceive(PacketReceiveEvent<TabCompleteResponse> e) {

        //we don't need this currently
    }

    @Override
    public void packetSend(PacketSendEvent<TabCompleteResponse> e) {
        boolean legacy = e.player().protocolVersion() <= 340;
        if (legacy) {
            OfferCompleterList list = new OfferCompleterList(e.packet().getOffers(), legacy);
            CompleterModifier.handleCompleter(list);
        } else if (e.packet().getStart() == 1) {
            String str = commandsWaiting.get(e.player().uniqueId());
            if (!str.contains(" ") && str.startsWith("/")) {
                commandsWaiting.remove(e.player().uniqueId());
                OfferCompleterList list = new OfferCompleterList(e.packet().getOffers(), legacy);
                CompleterModifier.handleCompleter(list);
            }
        }
    }

    public class RequestListener extends AbstractPacketListener<TabCompleteRequest>{

        protected RequestListener() {
            super(TabCompleteRequest.class, Direction.UPSTREAM, 0);
        }

        @Override
        public void packetReceive(PacketReceiveEvent<TabCompleteRequest> e) {
            boolean legacy = e.player().protocolVersion() <= 340;
            if (legacy) {
                commandsWaiting.put(e.player().uniqueId(), e.packet().getCommand());
            }
        }

        @Override
        public void packetSend(PacketSendEvent<TabCompleteRequest> e) {

        }
    }
}
