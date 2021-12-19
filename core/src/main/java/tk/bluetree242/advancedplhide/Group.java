package tk.bluetree242.advancedplhide;

import java.util.List;

/**
 * Group that inherit some completers. Should throw {@link IllegalStateException} if one of the group parents is parent of this, to prevent infinite loops
 */
public interface Group {

    /**
     *
     * @return Name of group
     */
    String getName();

    /**
     *
     * @return List of parents that this group inherit
     */
    List<Group> getParents();

    /**
     *
     * @return List of commands blacklisted, this is still same if the player is whitelist mode
     * @param all if should return all blacklisted commands even from parent groups
     */
    List<CommandCompleter> getCommandsBlacklisted(boolean all);

}
