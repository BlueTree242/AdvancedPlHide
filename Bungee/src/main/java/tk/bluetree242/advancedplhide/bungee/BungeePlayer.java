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

package tk.bluetree242.advancedplhide.bungee;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import tk.bluetree242.advancedplhide.Group;
import tk.bluetree242.advancedplhide.platform.PlatformPlayer;

import java.util.UUID;

public class BungeePlayer implements PlatformPlayer {

    private final AdvancedPlHideBungee core;
    private final ProxiedPlayer player;

    public BungeePlayer(AdvancedPlHideBungee core, ProxiedPlayer player) {
        this.core = core;
        this.player = player;
    }

    @Override
    public boolean hasPermission(String s) {
        return player.hasPermission(s);
    }

    @Override
    public void sendMessage(String s) {
        //noinspection deprecation
        player.sendMessage(s);
    }

    @Override
    public UUID getUUID() {
        return player.getUniqueId();
    }

    @Override
    public Group getGroup() {
        return core.getGroupForPlayer(player);
    }
}
