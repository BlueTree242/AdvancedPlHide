package tk.bluetree242.advancedplhide;

import tk.bluetree242.advancedplhide.config.Config;
import tk.bluetree242.advancedplhide.exceptions.ConfigurationLoadException;

/**
 * Platform is the platform the plugin runs on, we can also call it the plugin core as it contains some methods related to the plugin itself too
 * @see AdvancedPlHide#get()
 * @see Platform#get()
 */
public abstract class Platform {
    private static Platform platform = null;
    public static Platform get() {
        return platform;
    }
    public static void setPlatform(Platform val) {
        if (platform != null) throw new IllegalStateException("Platform already set");
        platform = val;
    }
    public abstract Config getConfig();

    public abstract void reloadConfig() throws ConfigurationLoadException;

}
