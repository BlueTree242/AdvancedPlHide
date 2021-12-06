package me.bluetree.advancedplhide.config;

import me.bluetree.advancedplhide.Group;
import net.bytebuddy.implementation.bind.annotation.Default;
import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.sorter.AnnotationBasedSorter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Config {

    @AnnotationBasedSorter.Order(10)
    @ConfComments("#true if we hide the commands, false if we show the commands only")
    @ConfDefault.DefaultBoolean(true)
    Boolean blacklist();

    @AnnotationBasedSorter.Order(20)
    @ConfComments("#Groups with their commands to whitelist")
    @ConfDefault.DefaultObject("me.bluetree.advancedplhide.AdvancedPlHide.defaultMap")
    Map<String, Group> groups();


}
