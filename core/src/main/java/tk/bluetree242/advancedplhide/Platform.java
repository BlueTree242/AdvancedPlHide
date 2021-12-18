package tk.bluetree242.advancedplhide;

import tk.bluetree242.advancedplhide.config.Config;

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

}
