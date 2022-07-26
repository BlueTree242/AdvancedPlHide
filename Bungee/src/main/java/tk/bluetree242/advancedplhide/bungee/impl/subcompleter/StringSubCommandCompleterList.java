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

package tk.bluetree242.advancedplhide.bungee.impl.subcompleter;

import tk.bluetree242.advancedplhide.SubCommandCompleter;
import tk.bluetree242.advancedplhide.SubCommandCompleterList;

import java.util.ArrayList;
import java.util.List;

public class StringSubCommandCompleterList extends SubCommandCompleterList {
    private final String command;
    private final String[] args;
    private final List<String> suggestions;

    public StringSubCommandCompleterList(List<String> suggestions, String notCompleted) {
        this.suggestions = suggestions;
        for (String suggestion : suggestions) {
            add(new StringSubCommandCompleter(this, suggestion));
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
    public List<String> export() {
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

    public boolean remove(Object e) {
        if (!(e instanceof SubCommandCompleter))
            throw new IllegalArgumentException("May only remove a SubCommandCompleter");
        SubCommandCompleter completer = (SubCommandCompleter) e;
        for (String suggestion : new ArrayList<>(suggestions)) {
            if (suggestion.equalsIgnoreCase(completer.getText())) {
                super.remove(completer);
                return suggestions.remove(suggestion);
            }
        }
        return false;
    }

}
