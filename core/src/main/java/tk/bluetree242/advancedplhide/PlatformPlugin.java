/*
 *  LICENSE
 * AdvancedPlHide
 * -------------
 * Copyright (C) 2021 - 2022 BlueTree242
 * -------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 *  END
 */

package tk.bluetree242.advancedplhide;

import com.github.kevinsawicki.http.HttpRequest;
import org.json.JSONObject;
import tk.bluetree242.advancedplhide.config.ConfManager;
import tk.bluetree242.advancedplhide.config.Config;
import tk.bluetree242.advancedplhide.exceptions.ConfigurationLoadException;
import tk.bluetree242.advancedplhide.impl.version.UpdateCheckResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Platform is the platform the plugin runs on, we can also call it the plugin core as it contains some methods related to the plugin itself too
 *
 * @see AdvancedPlHide#get()
 * @see PlatformPlugin#get()
 */
public abstract class PlatformPlugin {
    private static PlatformPlugin platformPlugin = null;

    private ConfManager<Config> confManager;
    private Config config;


    public static PlatformPlugin get() {
        return platformPlugin;
    }

    public static void setPlatform(PlatformPlugin val) {
        if (platformPlugin != null) throw new IllegalStateException("Platform already set");
        platformPlugin = val;
    }

    public Config getConfig() {
        return config;
    }

    public void reloadConfig() throws ConfigurationLoadException {
        confManager.reloadConfig();
        config = confManager.getConfigData();
        loadGroups();
    }

    public void initConfigManager() {
        confManager = ConfManager.create(getDataFolder().toPath(), "config.yml", Config.class);
    }

    public abstract void loadGroups();

    public abstract File getDataFolder();

    public abstract List<Group> getGroups();

    public abstract Group getGroup(String name);

    public abstract String getPluginForCommand(String s);

    public Group mergeGroups(List<Group> groups) {
        List<String> tabcomplete = new ArrayList<>();
        for (Group group : groups) {
            tabcomplete.addAll(group.getOriginCompleters());
        }
        List<String> names = new ArrayList<>();
        for (Group group : groups) {
            names.add(group.getName());
        }
        String name = "Merged Group: " + String.join(", ", names);
        return new Group(name, tabcomplete);
    }

    public abstract String getVersionConfig();

    public String getCurrentBuild() {
        return new JSONObject(getVersionConfig()).getString("buildNumber");
    }

    public String getCurrentVersion() {
        return new JSONObject(getVersionConfig()).getString("version");
    }

    public String getBuildDate() {
        return new JSONObject(getVersionConfig()).getString("buildDate");
    }

    public UpdateCheckResult updateCheck() {
        try {
            HttpRequest req = HttpRequest.post("https://advancedplhide.ml/updatecheck");
            req.part("version", getCurrentVersion());
            req.part("buildNumber", getCurrentBuild());
            req.part("buildDate", getBuildDate());
            req.part("devUpdatechecker", getConfig().dev_updatechecker() + "");
            String response = req.body();
            JSONObject json = new JSONObject(response);
            return new UpdateCheckResult(json.getInt("versions_behind"), json.isNull("message") ? null : json.getString("message"), json.isNull("type") ? "INFO" : json.getString("type"), json.getString("downloadUrl"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public abstract Type getType();

    public enum Type {
        SPIGOT("Spigot"), VELOCITY("Velocity"), BUNGEE("Bungee");
        private final String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
