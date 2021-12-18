package tk.bluetree242.advancedplhide.velocity.impl;

import com.velocitypowered.proxy.protocol.packet.TabCompleteResponse;
import tk.bluetree242.advancedplhide.CommandCompleter;

public class OfferCommandCompleter implements CommandCompleter {
    private final OfferCompleterList list;
    private final TabCompleteResponse.Offer offer;

    public OfferCommandCompleter(TabCompleteResponse.Offer offer, OfferCompleterList list) {
        this.list = list;
        this.offer = offer;
    }

    @Override
    public String getName() {
        return offer.getText();
    }

    @Override
    public void remove() {
        list.remove(this);
    }
}
