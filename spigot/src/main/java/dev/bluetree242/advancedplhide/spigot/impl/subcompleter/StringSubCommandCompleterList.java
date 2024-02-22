/*
 *  LICENSE
 * AdvancedPlHide
 * -------------
 * Copyright (C) 2021 - 2024 BlueTree242
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

package dev.bluetree242.advancedplhide.spigot.impl.subcompleter;

import dev.bluetree242.advancedplhide.SubCommandCompleter;
import dev.bluetree242.advancedplhide.SubCommandCompleterList;

import java.util.ArrayList;
import java.util.List;

public class StringSubCommandCompleterList extends SubCommandCompleterList {
    private final String command;
    private final String[] args;

    public StringSubCommandCompleterList(String[] suggestions, String notCompleted) {
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
    public String[] export() {
        List<String> result = new ArrayList<>();
        for (SubCommandCompleter sub : this) {
            result.add(sub.getText());
        }
        return result.toArray(new String[0]);
    }

    @Override
    public String[] getArgs() {
        return args;
    }

    @Override
    public String getName() {
        return command;
    }


}
