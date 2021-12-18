package tk.bluetree242.advancedplhide.spigot.impl.cmd;

import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import tk.bluetree242.advancedplhide.CommandCompleter;
import tk.bluetree242.advancedplhide.CommandCompleterList;

public class SuggestionCommandCompleterList extends CommandCompleterList {
    private final Suggestions suggestions;
    private boolean canAdd = true;

    public SuggestionCommandCompleterList(Suggestions suggestions) {
        this.suggestions = suggestions;
        for (Suggestion suggestion : suggestions.getList()) {
            add(new SuggestionCommandCompleter(suggestion, this));
        }
        canAdd = false;
    }

    @Override
    public Suggestions export() {
        return suggestions;
    }

    @Override
    public boolean add(CommandCompleter completer) {
        if (!canAdd)
            throw new IllegalStateException("SuggestionCommandCompleterList does not support add()");
        else return super.add(completer);
    }

    @Override
    public boolean remove(Object e) {
        if (!(e instanceof CommandCompleter)) throw new IllegalArgumentException("May only remove a CommandCompleter");
        CommandCompleter completer = (CommandCompleter) e;
        for (Suggestion suggestion : suggestions.getList()) {
            if (suggestion.getText().equalsIgnoreCase(completer.getName())) {
                super.remove(completer);
                return suggestions.getList().remove(suggestion);
            }
        }
        return false;
    }
}
