package tk.bluetree242.advancedplhide.impl;

import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import tk.bluetree242.advancedplhide.CommandCompleter;
import tk.bluetree242.advancedplhide.CommandCompleterList;

import java.util.ArrayList;

public class RootNodeCommandCompleter extends CommandCompleterList {
    private final RootCommandNode node;
    private boolean canAdd = false;
    public RootNodeCommandCompleter(RootCommandNode root) {
        canAdd = true;
        for (Object child : root.getChildren()) {
            add(new RootCommandCompleter((CommandNode) child, this));
        }
        canAdd = false;
        this.node = root;
    }
    @Override
    public RootCommandNode export() {
        return node;
    }
    @Override
    public boolean remove(Object e) {
        if (!(e instanceof RootCommandCompleter)) throw new IllegalArgumentException("May only remove RootCommandCompleter");
        RootCommandCompleter completer = (RootCommandCompleter) e;
        for (Object child : new ArrayList<>(node.getChildren())) {
            CommandNode real = (CommandNode) child;
            if (completer.getName().equalsIgnoreCase(real.getName())) {
                super.remove(completer);
               return node.getChildren().remove(real);
            }
        }
        return false;
    }
    @Override
    public boolean add(CommandCompleter e) {
        if (!canAdd) throw new UnsupportedOperationException();
        return super.add(e);
    }
}
