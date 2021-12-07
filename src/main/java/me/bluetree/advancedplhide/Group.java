package me.bluetree.advancedplhide;

import java.util.List;

public class Group {
    public final List<String> blacklist;

    public Group(List<String> blacklist) {
        this.blacklist = blacklist;
    }

    @Override
    public String toString() {
        return blacklist.toString();
    }
}
