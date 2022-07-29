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

package tk.bluetree242.advancedplhide.spigot.listener.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.mojang.brigadier.tree.RootCommandNode;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.protocol.game.ClientboundCommandsPacket;
import tk.bluetree242.advancedplhide.CompleterModifier;
import tk.bluetree242.advancedplhide.impl.completer.RootNodeCommandCompleter;
import tk.bluetree242.advancedplhide.spigot.SpigotPlayer;

public class ModernUtils { //utils for modern game, when the RootCommandNode was removed from COMMANDS packet


    protected static void handleModern(PacketEvent packetEvent, SpigotPlayer player) {
        ClientboundCommandsPacket packet = (ClientboundCommandsPacket) packetEvent.getPacket().getHandle();
        RootCommandNode nodeOrigin = packet.getRoot(new CommandBuildContext(RegistryAccess.BUILTIN.get())); //get the command node out
        RootNodeCommandCompleter node = new RootNodeCommandCompleter(nodeOrigin);
        CompleterModifier.handleCompleter(node, player);
        //noinspection unchecked
        packetEvent.setPacket(new PacketContainer(PacketType.Play.Server.COMMANDS, new ClientboundCommandsPacket(node.export())));//put the modified root node in a new packet because it's not really possible to modify
    }
}
