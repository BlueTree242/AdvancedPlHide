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

package tk.bluetree242.advancedplhide.impl.subcompleter;

import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import tk.bluetree242.advancedplhide.SubCommandCompleter;
import tk.bluetree242.advancedplhide.SubCommandCompleterList;

import java.util.ArrayList;
import java.util.List;

public class SuggestionSubCommandCompleterList extends SubCommandCompleterList {
    private final Suggestions suggestions;
    private final String command;
    private final String[] args;

    public SuggestionSubCommandCompleterList(Suggestions suggestions, String notCompleted) {
        this.suggestions = suggestions;
        for (Suggestion suggestion : suggestions.getList()) {
            add(new SuggestionSubCommandCompleter(this, suggestion.getText()));
        }
        String[] split = notCompleted.trim().split(" ");
        command = split[0].replaceFirst("/", "");
        List<String> list = new ArrayList<>();
        for (String s : split) {
            if (!s.equalsIgnoreCase("/" + command)) {
                if (notCompleted.endsWith(" "))
                    list.add(s);
                else {
                    if (!s.equals(split[split.length - 1])) {
                        list.add(s);
                    }
                }
            }
        }
        args = list.toArray(new String[0]);
    }

    @Override
    public Suggestions export() {
        return suggestions;
    }

    @Override
    public String[] getArgs() {
        return args;
    }

    @Override
    public String getName() {
        return command;
    }

    @Override
    public boolean remove(Object e) {
        if (!(e instanceof SubCommandCompleter))
            throw new IllegalArgumentException("May only remove a SubCommandCompleter");
        SubCommandCompleter completer = (SubCommandCompleter) e;
        for (Suggestion suggestion : suggestions.getList()) {
            if (suggestion.getText().equalsIgnoreCase(completer.getText())) {
                super.remove(completer);
                return suggestions.getList().remove(suggestion);
            }
        }
        return false;
    }
}
