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

package tk.bluetree242.advancedplhide.impl.completer;

import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import tk.bluetree242.advancedplhide.CommandCompleterList;

import java.util.ArrayList;

public class RootNodeCommandCompleter extends CommandCompleterList {
    private final RootCommandNode node;

    public RootNodeCommandCompleter(RootCommandNode root) {
        for (Object child : root.getChildren()) {
            add(new RootCommandCompleter((CommandNode) child, this));
        }
        this.node = root;
    }

    @Override
    public RootCommandNode export() {
        return node;
    }

    @Override
    public boolean remove(Object e) {
        if (!(e instanceof RootCommandCompleter))
            throw new IllegalArgumentException("May only remove RootCommandCompleter");
        RootCommandCompleter completer = (RootCommandCompleter) e;
        System.out.println(completer.getName());
        for (Object child : new ArrayList<>(node.getChildren())) {
            CommandNode real = (CommandNode) child;
            if (completer.getName().equalsIgnoreCase(real.getName())) {
                super.remove(completer);
                return node.getChildren().remove(real);
            }
        }
        return false;
    }

}
