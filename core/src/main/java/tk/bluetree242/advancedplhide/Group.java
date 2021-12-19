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
 * Group that inherit some completers. Should throw {@link IllegalStateException} if one of the group parents is parent of this, to prevent infinite loops
 */
public interface Group {

    /**
     *
     * @return Name of group
     */
    String getName();

    /**
     *
     * @return List of parents that this group inherit
     */
    List<Group> getParents();

    /**
     *
     * @return List of commands blacklisted, this is still same if the player is whitelist mode
     * @param all if should return all blacklisted commands even from parent groups
     */
    List<CommandCompleter> getCommandsBlacklisted(boolean all);

}
