package tk.bluetree242.advancedplhide.spigot.impl.cmd;

import tk.bluetree242.advancedplhide.CommandCompleter;

public class StringCommandCompleter implements CommandCompleter {
    private final String name;
    private final StringCommandCompleterList list;

    public StringCommandCompleter(String name, StringCommandCompleterList list) {
        this.list = list;
        this.name = name.replaceFirst("/", "");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void remove() {
        list.remove(this);
    }
}
