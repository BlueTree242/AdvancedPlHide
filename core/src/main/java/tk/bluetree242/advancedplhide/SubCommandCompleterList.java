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

package tk.bluetree242.advancedplhide;

import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class SubCommandCompleterList extends ArrayList<SubCommandCompleter> {

    @Override
    public void forEach(Consumer e) {
        for (SubCommandCompleter commandCompleter : new ArrayList<>(this)) {
            e.accept(commandCompleter);
        }

    }

    /**
     * @return Exported value of the original sub-completer list
     */
    public abstract Object export();


    /**
     * @return Array of args, does not include the command, this never includes parts that aren't completed by the player (part which all completers are supposed to complete
     */
    public abstract String[] getArgs();

    /**
     * @return Name of the command used, without /
     */
    public abstract String getName();


    public void removeAll() {
        for (SubCommandCompleter subCommandCompleter : new ArrayList<>(this)) {
            subCommandCompleter.remove();
        }
    }

    /**
     * Never use this method, most likely won't affect the list final result
     * @deprecated 90% won't affect the list final result
     */
    @Deprecated
    public final boolean add(SubCommandCompleter s) {
        return super.add(s);
    }
}
