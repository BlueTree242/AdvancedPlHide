package tk.bluetree242.advancedplhide.velocity.impl;

import com.velocitypowered.proxy.protocol.packet.TabCompleteResponse;
import tk.bluetree242.advancedplhide.CommandCompleter;
import tk.bluetree242.advancedplhide.CommandCompleterList;

import java.util.ArrayList;
import java.util.List;

public class OfferCompleterList extends CommandCompleterList {
    private final List<TabCompleteResponse.Offer> offers;
    private boolean canAdd = false;
    public OfferCompleterList(List<TabCompleteResponse.Offer> offers) {
        this.offers = offers;
        canAdd = true;
        for (TabCompleteResponse.Offer offer : offers) {
            add(new OfferCommandCompleter(offer, this));
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
            if (offer.getText().equalsIgnoreCase(completer.getName())) {
                super.remove(completer);
                return offers.remove(offer);
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
