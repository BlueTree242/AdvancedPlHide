package dev.bluetree242.advancedplhide.spigot.modern;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.RootCommandNode;
import dev.bluetree242.advancedplhide.CompleterModifier;
import dev.bluetree242.advancedplhide.Group;
import dev.bluetree242.advancedplhide.impl.completer.RootNodeCommandCompleter;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.network.protocol.game.ClientboundCommandSuggestionsPacket;
import net.minecraft.network.protocol.game.ClientboundCommandsPacket;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class V1_21_6_Handler implements ModernHandler {
    public static ClientboundCommandsPacket.NodeInspector<?> getCommandNodeInspector() {
        Field field = null;
        for (Field f : Commands.class.getDeclaredFields()) {
            if (ClientboundCommandsPacket.NodeInspector.class.isAssignableFrom(f.getType())) {
                field = f;
                field.setAccessible(true);
                break;
            }
        }
        if (field == null) throw new RuntimeException("COMMAND_NODE_INSPECTOR field not found");
        try {
            return (ClientboundCommandsPacket.NodeInspector<?>) field.get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleCommands(PacketEvent packetEvent, Group group, boolean whitelist) {
        ClientboundCommandsPacket packet = (ClientboundCommandsPacket) packetEvent.getPacket().getHandle();
        RootCommandNode<SharedSuggestionProvider> nodeOrigin = packet.getRoot(Commands.createValidationContext(VanillaRegistries.createLookup()), new CustomNodeBuilder());
        RootNodeCommandCompleter node = new RootNodeCommandCompleter(nodeOrigin);
        CompleterModifier.handleCompleter(node, group, whitelist);
        //noinspection unchecked
        packetEvent.setPacket(new PacketContainer(PacketType.Play.Server.COMMANDS, new ClientboundCommandsPacket(node.export(), getCommandNodeInspector())));
    }

    @Override
    public Suggestions getSuggestions(PacketEvent packetEvent) {
        ClientboundCommandSuggestionsPacket packet = (ClientboundCommandSuggestionsPacket) packetEvent.getPacket().getHandle();
        Suggestions suggestions = packet.toSuggestions();
        return new Suggestions(suggestions.getRange(), new ArrayList<>(suggestions.getList()));
    }

    @Override
    public void writeSuggestions(PacketEvent packetEvent, StructureModifier<Suggestions> modifier, Suggestions suggestions) {
        ClientboundCommandSuggestionsPacket packet = (ClientboundCommandSuggestionsPacket) packetEvent.getPacket().getHandle();
        packetEvent.setPacket(new PacketContainer(PacketType.Play.Server.TAB_COMPLETE, new ClientboundCommandSuggestionsPacket(packet.id(), suggestions)));
    }

    private static class CustomNodeBuilder implements ClientboundCommandsPacket.NodeBuilder<SharedSuggestionProvider> {
        @Override
        public @NotNull ArgumentBuilder<SharedSuggestionProvider, ?> createLiteral(@NotNull String s) {
            return LiteralArgumentBuilder.literal(s);
        }

        @Override
        public @NotNull ArgumentBuilder<SharedSuggestionProvider, ?> createArgument(@NotNull String s, @NotNull ArgumentType<?> argumentType, @Nullable ResourceLocation resourceLocation) {
            RequiredArgumentBuilder<SharedSuggestionProvider, ?> requiredArgumentBuilder = RequiredArgumentBuilder.argument(s, argumentType);
            if (resourceLocation != null) {
                requiredArgumentBuilder.suggests(SuggestionProviders.getProvider(resourceLocation));
            }
            return requiredArgumentBuilder;
        }

        @Override
        public @NotNull ArgumentBuilder<SharedSuggestionProvider, ?> configure(@NotNull ArgumentBuilder<SharedSuggestionProvider, ?> argumentBuilder, boolean b, boolean b1) {
            if (b) {
                argumentBuilder.executes(context -> 0);
            }
            return argumentBuilder;
        }
    }
}