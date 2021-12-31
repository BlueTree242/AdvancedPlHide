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

package tk.bluetree242.advancedplhide.velocity.impl.completer;

import com.velocitypowered.proxy.protocol.packet.TabCompleteResponse;
import tk.bluetree242.advancedplhide.CommandCompleter;
import tk.bluetree242.advancedplhide.CommandCompleterList;

import java.util.ArrayList;
import java.util.List;

public class OfferCompleterList extends CommandCompleterList {
    private final List<TabCompleteResponse.Offer> offers;
    private final boolean legacy;

    public OfferCompleterList(List<TabCompleteResponse.Offer> offers, boolean legacy) {
        this.offers = offers;
        this.legacy = legacy;
        for (TabCompleteResponse.Offer offer : offers) {
            add(new OfferCommandCompleter(offer, this, legacy));
        }
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

}
