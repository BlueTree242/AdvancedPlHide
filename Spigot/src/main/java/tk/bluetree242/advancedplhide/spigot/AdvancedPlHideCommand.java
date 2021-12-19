package tk.bluetree242.advancedplhide.spigot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import tk.bluetree242.advancedplhide.AdvancedPlHide;
import tk.bluetree242.advancedplhide.Platform;
import tk.bluetree242.advancedplhide.exceptions.ConfigurationLoadException;

import java.util.ArrayList;
import java.util.List;

public class AdvancedPlHideCommand implements CommandExecutor {
    private final AdvancedPlHideSpigot core;
    public AdvancedPlHideCommand(AdvancedPlHideSpigot core) {
        this.core = core;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
         sender.sendMessage(ChatColor.GREEN + "Running AdvancedPlHide v." + ChatColor.YELLOW + core.getDescription().getVersion());
         return true;
        } else {
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (!sender.hasPermission("plhide.reload")) {
                        sender.sendMessage(ChatColor.RED + "You don't have permission to run this command");
                        return true;
                    } else {
                        Bukkit.getScheduler().runTaskAsynchronously(core, () -> {
                            try {
                                Platform.get().reloadConfig();
                                sender.sendMessage(ChatColor.GREEN + "Configuration Reloaded");
                            } catch (ConfigurationLoadException e) {
                                sender.sendMessage(ChatColor.RED + "Could not reload " + e.getConfigName());
                            }
                        });
                        return true;
                    }
                }
                }
        }
        sender.sendMessage(ChatColor.RED + "SubCommand not found");
        return true;
    }

    public static class TabCompleter implements org.bukkit.command.TabCompleter {

        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
            List<String> result = new ArrayList<>();
            List<String> arg1 = new ArrayList<>();
            if (args.length == 1)
            if (sender.hasPermission("plhide.reload")) {
                arg1.add("reload");
            }
            for (String s : arg1) {
                if (s.startsWith(args[0])) {
                    result.add(s);
                }
            }
            return result;
        }
    }
}
