package tk.bluetree242.advancedplhide.config;

import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.sorter.AnnotationBasedSorter;

public interface Config {

    @AnnotationBasedSorter.Order(10)
    @ConfComments("#Message to send when the player tries to run /plugins, bypass: plhide.command.use")
    @ConfDefault.DefaultString("&cWe won't show you the plugin list for sure.")
    String pl_message();
}
