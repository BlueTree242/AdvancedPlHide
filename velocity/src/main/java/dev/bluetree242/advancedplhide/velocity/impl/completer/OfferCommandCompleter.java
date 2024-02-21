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

package dev.bluetree242.advancedplhide.velocity.impl.completer;

import com.velocitypowered.proxy.protocol.packet.TabCompleteResponsePacket;
import dev.bluetree242.advancedplhide.CommandCompleter;

public class OfferCommandCompleter implements CommandCompleter {
    private final OfferCompleterList list;
    private final String name;

    public OfferCommandCompleter(TabCompleteResponsePacket.Offer offer, OfferCompleterList list, boolean legacy) {
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
