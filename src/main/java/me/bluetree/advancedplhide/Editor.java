package me.bluetree.advancedplhide;

import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.CommandNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Editor {

    public static void removeBlacklist(List<String> commands, String prefix, Player player) {
        AdvancedPlHide core = (AdvancedPlHide) Bukkit.getPluginManager().getPlugin("AdvancedPlHide");
        if (player.hasPermission("plhide.bypassblacklist")) return;
        List<String> blacklist = core.config.blacklist() ? new ArrayList<>() : new ArrayList<>(commands);
        core.config.groups().forEach((name, cmds) -> {
            if (player.hasPermission("plhide.group." + name)) {
                if (core.config.blacklist()) {
                    for (String s : cmds.blacklist) {
                        blacklist.add("/" +s);
                    }
                } else {
                    for (String s : cmds.blacklist) {
                        blacklist.remove("/" +s);
                    }
                }
            }
        });
        for (String command : commands.toArray(new String[0])) {
            if (blacklist.contains(command) ){
                commands.remove(command);
            } else if (blacklist.contains(prefix + command)) {
                commands.remove(command);
            }
        }
    }


    public static void removeBlacklist(Collection realSuggestions, Collection suggestions, Player player) {
        AdvancedPlHide core = (AdvancedPlHide) Bukkit.getPluginManager().getPlugin("AdvancedPlHide");
        if (player.hasPermission("plhide.bypassblacklist")) return;
        List<String> blacklist = core.config.blacklist() ? new ArrayList<>() : new ArrayList<>(nodesToStrings(suggestions));
        core.config.groups().forEach((name, cmds) -> {
            if (player.hasPermission("plhide.group." + name)) {
                if (core.config.blacklist()) {
                    for (String s : cmds.blacklist) {
                        blacklist.add(s);
                    }
                } else {
                    for (String s : cmds.blacklist) {
                        blacklist.remove(s);
                    }
                }
            }
        });
        for (Object c : suggestions) {
            CommandNode command = (CommandNode) c;
            if (blacklist.contains(command.getName())) realSuggestions.remove(command);
        }
    }

    private static List<String> nodesToStrings(Collection suggestions) {
        List<String> result = new ArrayList<>();
        for (Object s : suggestions) {
            CommandNode suggestion = (CommandNode) s;
            result.add(suggestion.getName());
        }
        return result;
    }

    public static void removeBlacklist(Suggestions suggestions, Player player) {
        AdvancedPlHide core = (AdvancedPlHide) Bukkit.getPluginManager().getPlugin("AdvancedPlHide");
        if (player.hasPermission("plhide.bypassblacklist")) return;
        List<String> blacklist = core.config.blacklist() ? new ArrayList<>() : new ArrayList<>(suggestionsToStrings(suggestions));
        core.config.groups().forEach((name, cmds) -> {
            if (player.hasPermission("plhide.group." + name)) {
                if (core.config.blacklist()) {
                    for (String s : cmds.blacklist) {
                        blacklist.add(s);
                    }
                } else {
                    for (String s : cmds.blacklist) {
                        blacklist.remove(s);
                    }
                }
            }
        });
        for (Suggestion command : suggestions.getList().toArray(new Suggestion[0])) {
            if (blacklist.contains(command.getText())) suggestions.getList().remove(command);
        }
    }

    private static List<String> suggestionsToStrings(Suggestions suggestions) {
        List<String> result = new ArrayList<>();
        for (Suggestion suggestion : suggestions.getList()) {
            result.add(suggestion.getText());
        }
        return result;
    }
}
