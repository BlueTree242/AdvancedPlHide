/*
 *  LICENSE
 * AdvancedPlHide
 * -------------
 * Copyright (C) 2021 - 2021 BlueTree242
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

import org.json.JSONObject;
import tk.bluetree242.advancedplhide.config.Config;
import tk.bluetree242.advancedplhide.exceptions.ConfigurationLoadException;
import tk.bluetree242.advancedplhide.impl.version.UpdateCheckResult;
import tk.bluetree242.advancedplhide.utils.HttpPostMultipart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Platform is the platform the plugin runs on, we can also call it the plugin core as it contains some methods related to the plugin itself too
 *
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

    public abstract List<Group> getGroups();

    public abstract Group getGroup(String name);

    public abstract String getPluginForCommand(String s);

    public Group mergeGroups(List<Group> groups) {
        List<CommandCompleter> tabcomplete = new ArrayList<>();
        for (Group group : groups) {
            for (CommandCompleter completer : group.getTabComplete()) {
                tabcomplete.add(completer);
            }
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
            Map<String, String> headers = new HashMap<>();
            headers.put("User-Agent", "APH/Java");
            HttpPostMultipart req = new HttpPostMultipart("https://advancedplhide.ml/updatecheck", "utf-8", headers);
            req.addFormField("version", getCurrentVersion());
            req.addFormField("buildNumber", getCurrentBuild());
            req.addFormField("buildDate", getBuildDate());
            req.addFormField("devUpdatechecker", getConfig().dev_updatechecker() + "");
            String response = req.finish();
            JSONObject json = new JSONObject(response);
            return new UpdateCheckResult(json.getInt("versions_behind"), json.isNull("versions_behind") ? null : json.getString("message"), json.isNull("type") ? "INFO" : json.getString("type"), json.getString("downloadUrl"));
        } catch (Exception e) {
            return null;
        }
    }
}
