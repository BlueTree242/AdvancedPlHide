package tk.bluetree242.advancedplhide;

public interface CommandCompleter {

    /**
     * @return The name of the command, if the completer is `/help` this would return `help`
     */
    String getName();

    /**
     * This method is only used if this is included in a CommandCompleterList
     *
     * @throws IllegalStateException if this is not in a list
     */
    void remove();
}
