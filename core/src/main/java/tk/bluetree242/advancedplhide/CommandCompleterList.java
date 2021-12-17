package tk.bluetree242.advancedplhide;

import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class CommandCompleterList extends ArrayList<CommandCompleter> {


    @Override
    public void forEach(Consumer e) {
        for (CommandCompleter commandCompleter : new ArrayList<>(this)) {
            e.accept(commandCompleter);
        }

    }

    public abstract Object export();


}
