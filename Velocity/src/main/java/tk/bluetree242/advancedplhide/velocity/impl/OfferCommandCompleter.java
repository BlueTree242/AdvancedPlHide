package tk.bluetree242.advancedplhide.velocity.impl;

import com.velocitypowered.proxy.protocol.packet.TabCompleteResponse;
import tk.bluetree242.advancedplhide.CommandCompleter;

public class OfferCommandCompleter implements CommandCompleter {
    private final OfferCompleterList list;
    private final String name;

    public OfferCommandCompleter(TabCompleteResponse.Offer offer, OfferCompleterList list, boolean legacy) {
        this.list = list;
        this.name = legacy ? offer.getText().replaceFirst("/", "") : offer.getText();

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void remove() {
        list.remove(this);
    }
}
