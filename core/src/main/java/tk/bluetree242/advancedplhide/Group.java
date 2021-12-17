package tk.bluetree242.advancedplhide;

import java.util.List;

public interface Group {

    String getName();

    List<Group> getParents();

    List<CommandCompleter> getCommandsBlacklisted();

}
