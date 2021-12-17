package tk.bluetree242.advancedplhide;

import java.util.ArrayList;

public class CommandModifier {

    public static void removePluginPrefix(CommandCompleterList list) {
        for (CommandCompleter completer : new ArrayList<>(list)) {
            if (completer.getName().contains(":")) completer.remove();
        }
    }
}
