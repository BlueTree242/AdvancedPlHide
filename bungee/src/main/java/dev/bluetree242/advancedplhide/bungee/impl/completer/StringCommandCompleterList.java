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

package dev.bluetree242.advancedplhide.bungee.impl.completer;

import dev.bluetree242.advancedplhide.CommandCompleter;
import dev.bluetree242.advancedplhide.CommandCompleterList;

import java.util.ArrayList;
import java.util.List;

public class StringCommandCompleterList extends CommandCompleterList {
    private final List<String> list;

    public StringCommandCompleterList(List<String> list) {
        this.list = list;
        for (String s : list) {
            add(new StringCommandCompleter(s, this));
        }
    }


    @Override
    public List<String> export() {
        List<String> cmds = new ArrayList<>();
        for (CommandCompleter completer : this) {
            cmds.add("/" + completer.getName());
        }
        return cmds;
    }

    @Override
    public boolean remove(Object o) {
        if (!(o instanceof CommandCompleter)) throw new IllegalArgumentException("May only Remove CommandCompleter");
        CommandCompleter completer = (CommandCompleter) o;
        list.remove("/" + completer.getName());
        return super.remove(o);
    }
}
