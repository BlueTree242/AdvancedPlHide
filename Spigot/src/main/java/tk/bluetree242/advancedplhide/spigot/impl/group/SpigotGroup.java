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

package tk.bluetree242.advancedplhide.spigot.impl.group;

import org.bukkit.entity.Player;
import tk.bluetree242.advancedplhide.CommandCompleter;
import tk.bluetree242.advancedplhide.Group;
import tk.bluetree242.advancedplhide.Platform;
import tk.bluetree242.advancedplhide.spigot.AdvancedPlHideSpigot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SpigotGroup implements Group {
    private final String name;
    private final List<CommandCompleter> tabcomplete;
    private final AdvancedPlHideSpigot core;
    private final int priority;
    private List<String> parents;

    public SpigotGroup(String name, List<String> parents, int priority, List<CommandCompleter> tabcomplete, AdvancedPlHideSpigot core) {
        this.name = name;
        this.parents = parents;
        this.priority = priority;
        this.tabcomplete = tabcomplete;
        this.core = core;
    }

    public static Group forPlayer(Player player) {
        Platform core = Platform.get();
        if (player.hasPermission("plhide.no-group")) return null;
        List<Group> groups = new ArrayList<>();
        for (Group group : core.getGroups()) {
            if (player.hasPermission("plhide.group." + group.getName())) {
                groups.add(group);
            }
        }
        groups.add(core.getGroup("default"));
        groups.sort(new Comparator<Group>() {
            @Override
            public int compare(Group o1, Group o2) {
                return o2.getPriority() - o2.getPriority();
            }
        });
        Group group = groups.get(0);

        return group;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Group> getParents() {
        List<Group> groups = new ArrayList<>();
        for (String parent : parents) {
            groups.add(core.getGroup(parent));
        }
        return groups;
    }

    @Override
    public List<CommandCompleter> getTabComplete(boolean all) {
        if (!all) return tabcomplete;
        List<CommandCompleter> list = new ArrayList<>(tabcomplete);
        List<Group> included = new ArrayList<>();
        included.add(this);
        for (Group parent : getParents()) {
            loadGroup(parent, list, included);
        }
        return list;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    private void loadGroup(Group group, List<CommandCompleter> list, List<Group> included) {
        if (!included.contains(group)) {
            included.add(group);
            for (CommandCompleter completer : group.getTabComplete(false)) {
                list.add(completer);
            }
            for (Group parent : group.getParents()) {
                loadGroup(parent, list, included);
            }
        }
    }
}
