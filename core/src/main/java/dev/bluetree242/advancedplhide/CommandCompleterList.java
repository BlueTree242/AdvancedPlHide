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

package dev.bluetree242.advancedplhide;

import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class CommandCompleterList extends ArrayList<CommandCompleter> {


    @Override
    public void forEach(Consumer<? super CommandCompleter> e) {
        for (CommandCompleter commandCompleter : new ArrayList<>(this)) {
            e.accept(commandCompleter);
        }

    }

    /**
     * @return Exported value of the original completer list
     */
    public abstract Object export();

    /**
     * Never use this method, most likely won't affect the list final result
     *
     */
    public final boolean add(CommandCompleter s) {
        return super.add(s);
    }


}
