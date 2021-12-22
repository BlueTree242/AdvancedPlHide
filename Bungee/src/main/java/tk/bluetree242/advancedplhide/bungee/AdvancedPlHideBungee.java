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

package tk.bluetree242.advancedplhide.bungee;

import dev.simplix.protocolize.api.Protocolize;
import net.md_5.bungee.api.plugin.Plugin;
import tk.bluetree242.advancedplhide.CommandCompleter;
import tk.bluetree242.advancedplhide.Group;
import tk.bluetree242.advancedplhide.Platform;
import tk.bluetree242.advancedplhide.bungee.impl.group.BungeeGroup;
import tk.bluetree242.advancedplhide.config.ConfManager;
import tk.bluetree242.advancedplhide.config.Config;
import tk.bluetree242.advancedplhide.exceptions.ConfigurationLoadException;
import tk.bluetree242.advancedplhide.impl.group.GroupCompleter;

import java.util.ArrayList;
import java.util.List;

public class AdvancedPlHideBungee extends Plugin {
    public Config config;
    protected ConfManager<Config> confManager = ConfManager.create(getDataFolder().toPath(), "config.yml", Config.class);
    private PacketListener listener;
    ;
    private List<Group> groups = new ArrayList<>();

    public void onEnable() {
        reloadConfig();
        Protocolize.listenerProvider().registerListener(listener = new PacketListener(this));
        Platform.setPlatform(new Impl());
    }

    public void onDisable() {
        Protocolize.listenerProvider().unregisterListener(listener);
    }


    public void loadGroups() {
        groups = new ArrayList<>();
        config.groups().forEach((name, val) -> {
            List<CommandCompleter> tabcomplete = new ArrayList<>();
            for (String s : val.tabcomplete()) {
                tabcomplete.add(new GroupCompleter(s));
            }
            if (getGroup(name) == null)
                groups.add(new BungeeGroup(name, val.parent_groups(), val.priority(), tabcomplete, this));
            else {
                getLogger().warning("Group " + name + " is repeated.");
            }
        });
        if (getGroup("default") == null) {
            getLogger().warning("group default was not found, using virtual default group.");
            groups.add(new BungeeGroup("default", new ArrayList<>(), 0, new ArrayList<>(), this));
        }

    }

    public Group getGroup(String name) {
        for (Group group : groups) {
            if (group.getName().equals(name)) return group;
        }
        return null;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void reloadConfig() {
        confManager.reloadConfig();
        config = confManager.getConfigData();
        loadGroups();
    }

    public class Impl extends Platform {

        @Override
        public Config getConfig() {
            return config;
        }

        @Override
        public void reloadConfig() throws ConfigurationLoadException {
            AdvancedPlHideBungee.this.reloadConfig();
        }

        @Override
        public List<Group> getGroups() {
            return AdvancedPlHideBungee.this.getGroups();
        }

        @Override
        public Group getGroup(String name) {
            return AdvancedPlHideBungee.this.getGroup(name);
        }
    }


}
