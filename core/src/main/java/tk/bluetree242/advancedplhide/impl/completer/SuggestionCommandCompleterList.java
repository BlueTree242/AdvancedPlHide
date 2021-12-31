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

package tk.bluetree242.advancedplhide.impl.completer;

import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import tk.bluetree242.advancedplhide.CommandCompleter;
import tk.bluetree242.advancedplhide.CommandCompleterList;

public class SuggestionCommandCompleterList extends CommandCompleterList {
    private final Suggestions suggestions;

    public SuggestionCommandCompleterList(Suggestions suggestions) {
        this.suggestions = suggestions;
        for (Suggestion suggestion : suggestions.getList()) {
            add(new SuggestionCommandCompleter(suggestion, this));
        }
    }

    @Override
    public Suggestions export() {
        return suggestions;
    }


    @Override
    public boolean remove(Object e) {
        if (!(e instanceof CommandCompleter)) throw new IllegalArgumentException("May only remove a CommandCompleter");
        CommandCompleter completer = (CommandCompleter) e;
        for (Suggestion suggestion : suggestions.getList()) {
            if (suggestion.getText().equalsIgnoreCase(completer.getName())) {
                super.remove(completer);
                return suggestions.getList().remove(suggestion);
            }
        }
        return false;
    }
}
