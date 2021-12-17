package tk.bluetree242.advancedplhide.velocity.impl;

import tk.bluetree242.advancedplhide.CommandCompleter;

public class StringCommandCompleter implements CommandCompleter {
    private final String cmd;
    private final StringCommandCompleteList list;
    public StringCommandCompleter(String cmd, StringCommandCompleteList list) {
        this.cmd = cmd;
        this.list = list;
    }
    @Override
    public String getName() {
        return cmd;
    }

    @Override
    public void remove() {
        list.remove(this);
    }
}
