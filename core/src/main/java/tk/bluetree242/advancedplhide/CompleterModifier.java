package tk.bluetree242.advancedplhide;

import java.util.ArrayList;

public class CompleterModifier {

    public static void removePluginPrefix(CommandCompleterList list) {
        for (CommandCompleter completer : new ArrayList<>(list)) {
            if (completer.getName().contains(":")) completer.remove();
        }
    }

    public static void handleCompleter(CommandCompleterList list) {
        removePluginPrefix(list);
    }
}
