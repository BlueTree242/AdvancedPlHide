/*
 * LICENSE
 * AdvancedPlHide
 * -------------
 * Copyright (C) 2021 - 2025 BlueTree242
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
 * You should have received a copy of the GNU General
 * Public License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * END
 */

package dev.bluetree242.advancedplhide.spigot.paper;

import com.destroystokyo.paper.event.brigadier.AsyncPlayerSendCommandsEvent;
import com.destroystokyo.paper.event.brigadier.AsyncPlayerSendSuggestionsEvent;
import dev.bluetree242.advancedplhide.CompleterModifier;
import dev.bluetree242.advancedplhide.Group;
import dev.bluetree242.advancedplhide.impl.completer.RootNodeCommandCompleter;
import dev.bluetree242.advancedplhide.impl.completer.SuggestionCommandCompleterList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.function.Function;


@SuppressWarnings("ALL")
public class PaperEventListener implements Listener {
    private final Function<Player, Group> getGroup;

    public PaperEventListener(Function<Player, Group> getGroup) {
        this.getGroup = getGroup;
    }

    public static boolean isAvailable() {
        try {
            Class.forName("com.destroystokyo.paper.event.brigadier.AsyncPlayerSendCommandsEvent");
            Class.forName("com.destroystokyo.paper.event.brigadier.AsyncPlayerSendSuggestionsEvent");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSuggestions(AsyncPlayerSendSuggestionsEvent e) {
        Group group = getGroup.apply(e.getPlayer());
        SuggestionCommandCompleterList suggestions = new SuggestionCommandCompleterList(e.getSuggestions());
        CompleterModifier.handleCompleter(suggestions, group, e.getPlayer().hasPermission("plhide.whitelist-mode"));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommands(AsyncPlayerSendCommandsEvent<?> e) {
        Group group = getGroup.apply(e.getPlayer());
        RootNodeCommandCompleter suggestions = new RootNodeCommandCompleter(e.getCommandNode());
        CompleterModifier.handleCompleter(suggestions, group, e.getPlayer().hasPermission("plhide.whitelist-mode"));
    }
}
