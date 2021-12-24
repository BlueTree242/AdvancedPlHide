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

package tk.bluetree242.advancedplhide.config;

import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.SubSection;
import space.arim.dazzleconf.sorter.AnnotationBasedSorter;

import java.util.List;
import java.util.Map;

public interface Config {

    @AnnotationBasedSorter.Order(10)
    @ConfComments("#Message to send when the player tries to run /plugins, bypass: plhide.command.use")
    @ConfDefault.DefaultString("&cWe won't show you the plugin list for sure.")
    String pl_message();

    @AnnotationBasedSorter.Order(20)
    @ConfComments("#Removes the plugin:command from tabcompleter, it function better than spigot does it")
    @ConfDefault.DefaultBoolean(true)
    Boolean remove_plugin_prefix();

    @AnnotationBasedSorter.Order(30)
    @ConfComments("") //space between the groups and the conf options up
    @ConfDefault.DefaultObject("tk.bluetree242.advancedplhide.config.ConfManager.defaultGroups")
    Map<String, @SubSection Group> groups();

    @AnnotationBasedSorter.Order(40)
    @ConfComments("# If you disable this, we will not notify you of new dev builds but of new major or beta updates")
    @ConfDefault.DefaultBoolean(true)
    boolean dev_updatechecker();

    interface Group {
        @AnnotationBasedSorter.Order(10)
        List<String> tabcomplete();
    }
}
