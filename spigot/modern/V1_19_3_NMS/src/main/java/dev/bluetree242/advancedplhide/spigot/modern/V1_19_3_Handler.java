/*
 *  LICENSE
 * AdvancedPlHide
 * -------------
 * Copyright (C) 2021 - 2024 BlueTree242
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

package dev.bluetree242.advancedplhide.spigot.modern;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.mojang.brigadier.tree.RootCommandNode;
import dev.bluetree242.advancedplhide.CompleterModifier;
import dev.bluetree242.advancedplhide.Group;
import dev.bluetree242.advancedplhide.impl.completer.RootNodeCommandCompleter;
import net.minecraft.commands.Commands;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.network.protocol.game.ClientboundCommandsPacket;

public class V1_19_3_Handler implements ModernHandler {
    @Override
    public void handle(PacketEvent packetEvent, Group group, boolean whitelist) {
        ClientboundCommandsPacket packet = (ClientboundCommandsPacket) packetEvent.getPacket().getHandle();
        RootCommandNode nodeOrigin = packet.getRoot(Commands.createValidationContext(VanillaRegistries.createLookup())); //get the command node out
        RootNodeCommandCompleter node = new RootNodeCommandCompleter(nodeOrigin);
        CompleterModifier.handleCompleter(node, group, whitelist);
        //noinspection unchecked
        packetEvent.setPacket(new PacketContainer(PacketType.Play.Server.COMMANDS, new ClientboundCommandsPacket(node.export())));//put the modified root node in a new packet because it's not really possible to modify
    }
}
