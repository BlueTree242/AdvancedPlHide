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

package tk.bluetree242.advancedplhide.impl.version;

public class UpdateCheckResult {
    private final int versionsBehind;
    private final String message;
    private final String loggerType;
    private final String updateUrl;

    public UpdateCheckResult(int versionsBehind, String message, String loggerType, String updateUrl) {
        this.versionsBehind = versionsBehind;
        this.message = message;
        this.loggerType = loggerType;
        this.updateUrl = updateUrl;
    }

    public int getVersionsBehind() {
        return versionsBehind;
    }

    public String getMessage() {
        return message;
    }

    public String getLoggerType() {
        return loggerType;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }
}
