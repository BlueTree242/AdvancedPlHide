package tk.bluetree242.advancedplhide.spigot.impl.cmd;

import tk.bluetree242.advancedplhide.CommandCompleter;
import tk.bluetree242.advancedplhide.CommandCompleterList;

import java.util.ArrayList;
import java.util.List;

public class StringCommandCompleterList extends CommandCompleterList {

    public StringCommandCompleterList(String[] list) {
        for (String s : list) {
            add(new StringCommandCompleter(s, this));
        }
    }


    @Override
    public String[] export() {
        List<String> cmds = new ArrayList<>();
        for (CommandCompleter completer : this) {
            cmds.add("/" + completer.getName());
        }
        return cmds.toArray(new String[0]);
    }
}
