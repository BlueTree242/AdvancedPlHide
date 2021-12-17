package tk.bluetree242.advancedplhide.bukkit.impl.cmd;

import com.mojang.brigadier.suggestion.Suggestion;
import tk.bluetree242.advancedplhide.CommandCompleter;

public class SuggestionCommandCompleter implements CommandCompleter {
    private final Suggestion suggestion;
    private final SuggestionCommandCompleterList list;

    public SuggestionCommandCompleter(Suggestion suggestion, SuggestionCommandCompleterList list) {
        this.list = list;
        this.suggestion = suggestion;
    }

    @Override
    public String getName() {
        return suggestion.getText();
    }

    @Override
    public void remove() {
        list.remove(this);
    }
}
