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

package tk.bluetree242.advancedplhide.config.subcompleter;

import java.util.ArrayList;

public class ConfSubCompleterList extends ArrayList<ConfSubCompleter> {

    /**
     * @param cmd Command to fetch all completers of
     * @return {@link ConfSubCompleterList} of completers with this command
     */
    public ConfSubCompleterList ofCommand(String cmd) {
        ConfSubCompleterList result = new ConfSubCompleterList();
        for (ConfSubCompleter completer : this) {
            if (completer.getName().equalsIgnoreCase(cmd)) {
                result.add(completer);
            }
        }
        return result;
    }

    /**
     * This method tries to find commands with this args, respects *
     *
     * @param args args of the command
     * @return list of sub completers that have this args
     */
    public ConfSubCompleterList ofArgs(String[] args) {
        ConfSubCompleterList result = new ConfSubCompleterList();
        for (ConfSubCompleter completer : this) {
            boolean equal = true;
            int length = 0;
            if (completer.getArgs().length == args.length) {
                for (String arg : completer.getArgs()) {
                    if (equal != false) {
                        if (args[length].equalsIgnoreCase(arg) || arg.equals("*")) {
                            equal = true;
                        } else equal = false;
                    }
                    length++;
                }
            } else {
                equal = false;
            }
            if (equal) result.add(completer);
        }
        return result;
    }
}
