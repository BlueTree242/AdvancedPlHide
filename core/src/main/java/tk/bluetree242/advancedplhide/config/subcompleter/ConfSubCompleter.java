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

package tk.bluetree242.advancedplhide.config.subcompleter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ConfSubCompleter {
    private final String name;
    private final String[] args;

    /**
     *
     * @param cmd - command in config, the full string with 0 modifications
     * @return {@link ConfSubCompleter} of this command
     */
    public static ConfSubCompleter of(@NotNull String cmd) {
        String[] split = cmd.trim().split(" ");
        String name = split[0];
        List<String> args = new ArrayList<>(Arrays.asList(split));
        args.remove(name);
        return new ConfSubCompleter(name, args.toArray(new String[0]));
    }

    public ConfSubCompleter(@NotNull String name,@NotNull String[] args) {
        this.name = name;
        this.args = args;
    }

    public String getName() {
        return name;
    }

    public String[] getArgs() {
        return args;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConfSubCompleter)) return false;
        ConfSubCompleter that = (ConfSubCompleter) o;
        return getName().equals(that.getName()) && Arrays.equals(getArgs(), that.getArgs());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getName());
        result = 31 * result + Arrays.hashCode(getArgs());
        return result;
    }

    @Override
    public String toString() {
        return name + " " + String.join(" ", args);
    }
}
