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

package dev.bluetree242.advancedplhide.impl.completer;

import com.mojang.brigadier.suggestion.Suggestion;
import dev.bluetree242.advancedplhide.CommandCompleter;

public class SuggestionCommandCompleter implements CommandCompleter {
    private final Suggestion suggestion;
    private final SuggestionCommandCompleterList list;

    public SuggestionCommandCompleter(Suggestion suggestion, SuggestionCommandCompleterList list) {
        this.list = list;
        this.suggestion = suggestion;
    }

    @Override
    public String getName() {
        return suggestion.getText();
    }

    @Override
    public void remove() {
        list.remove(this);
    }
}
