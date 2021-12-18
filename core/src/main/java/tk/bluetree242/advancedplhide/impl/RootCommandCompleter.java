package tk.bluetree242.advancedplhide.impl;

import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import tk.bluetree242.advancedplhide.CommandCompleter;

public class RootCommandCompleter implements CommandCompleter {
    private final CommandNode node;
    private final RootNodeCommandCompleter list;
    public RootCommandCompleter(CommandNode node, RootNodeCommandCompleter list) {
        this.list = list;
        this.node = node;
    }
    @Override
    public String getName() {
        return node.getName();
    }

    @Override
    public void remove() {
        list.remove(this);
    }
}
