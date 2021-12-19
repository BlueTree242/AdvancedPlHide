package tk.bluetree242.advancedplhide.velocity.impl;

import com.velocitypowered.proxy.protocol.packet.TabCompleteResponse;
import tk.bluetree242.advancedplhide.CommandCompleter;
import tk.bluetree242.advancedplhide.CommandCompleterList;

import java.util.ArrayList;
import java.util.List;

public class OfferCompleterList extends CommandCompleterList {
    private final List<TabCompleteResponse.Offer> offers;
    private boolean canAdd = false;
    private final boolean legacy;
    public OfferCompleterList(List<TabCompleteResponse.Offer> offers, boolean legacy) {
        this.offers = offers;
        this.legacy = legacy;
        canAdd = true;
        for (TabCompleteResponse.Offer offer : offers) {
            add(new OfferCommandCompleter(offer, this, legacy));
        }
        canAdd = false;
    }
    @Override
    public List<TabCompleteResponse.Offer> export() {
        return offers;
    }

    @Override
    public boolean remove(Object e) {
        if (!(e instanceof CommandCompleter)) throw new IllegalArgumentException("May only remove CommandCompleter");
        CommandCompleter completer = (CommandCompleter) e;
        for (TabCompleteResponse.Offer offer : new ArrayList<>(offers)) {
            if (!legacy) {
                if (offer.getText().equalsIgnoreCase(completer.getName())) {
                    super.remove(completer);
                    return offers.remove(offer);
                }
            } else {
                if (offer.getText().equalsIgnoreCase("/" + completer.getName())) {
                    super.remove(completer);
                    return offers.remove(offer);
                }
            }
        }
        return false;
    }

    @Override
    public boolean add(CommandCompleter e) {
        if (!canAdd)
        throw new IllegalStateException("May not add to this type of Completer");
        return super.add(e);
    }
}
