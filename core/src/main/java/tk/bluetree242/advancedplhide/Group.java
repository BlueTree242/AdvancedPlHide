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

import java.util.List;

/**
 * Group that inherit some completers.
 * **Handling infinite loops of group parents:**
 * Parent groups inherited would not be repeated.
 */
public class Group {
    private final String name;
    private final List<CommandCompleter> completers;

    public Group(String name, List<CommandCompleter> completers) {
        this.name = name;
        this.completers = completers;
    }

    /**
     * @return Name of group
     */
    public String getName() {
        return name;
    }


    /**
     * @return List of commands for group, either used as whitelist or blacklist. The commands are considered not in a list.
     */
    public List<CommandCompleter> getTabComplete() {
        return completers;
    }


    @Override
    public String toString() {
        return getName();
    }

}
