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

package tk.bluetree242.advancedplhide;

import tk.bluetree242.advancedplhide.config.subcompleter.ConfSubCompleter;
import tk.bluetree242.advancedplhide.config.subcompleter.ConfSubCompleterList;
import tk.bluetree242.advancedplhide.impl.group.GroupCompleter;
import tk.bluetree242.advancedplhide.platform.PlatformPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Group that inherit some completers.
 * **Handling infinite loops of group parents:**
 * Parent groups inherited would not be repeated.
 */
public class Group {
    private final String name;
    private final List<CommandCompleter> completers;
    private final ConfSubCompleterList subCompleters;
    private final List<String> originCompleters;
    private final PlatformPlugin core;
    public Group(PlatformPlugin core, String name, List<String> completers) {
        this.core = core;
        this.originCompleters = completers;
        this.name = name;
        List<CommandCompleter> completersFinal = new ArrayList<>();
        ConfSubCompleterList subCompletersFinal = new ConfSubCompleterList();
        for (String completer : completers) {
            String[] split = completer.trim().split(" ");
            if (split.length == 1) completersFinal.add(new GroupCompleter(split[0]));
            else {
                subCompletersFinal.add(ConfSubCompleter.of(completer));
            }
        }
        this.completers = completersFinal;
        this.subCompleters = subCompletersFinal;
    }

    /**
     * @return Name of group
     */
    public String getName() {
        return name;
    }

    /**
     * @return list of completer strings. the raw ones in config
     */
    public List<String> getOriginCompleters() {
        return originCompleters;
    }

    /**
     * @return list of sub completers
     */
    public ConfSubCompleterList getSubCompleters() {
        return subCompleters;
    }


    /**
     * @return List of commands for group, either used as whitelist or blacklist. The commands are considered not in a list. this will not contain any sub commands
     */
    public List<CommandCompleter> getCompleteCommands() {
        return completers;
    }


    @Override
    public String toString() {
        return getName();
    }

    public boolean canSee(PlatformPlayer player, String cmd) {
        if (cmd.contains(":") && core.getConfig().remove_plugin_prefix()) return false;
        boolean whitelist = player.isWhitelist();
        for (CommandCompleter command : getCompleteCommands()) {
            String name = command.getName();
            if (name.startsWith("from:")) {
                String plugin = name.replaceFirst("from:", "");
                if (core.getPluginForCommand(name).equals(plugin)) if (whitelist) return true;
            } else if (name.equalsIgnoreCase(cmd)) {
                if (whitelist) return true;
            }
        }
        return false;
    }

}
