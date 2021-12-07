package me.bluetree.advancedplhide.config;

import me.bluetree.advancedplhide.AdvancedPlHide;
import me.bluetree.advancedplhide.Group;
import org.bukkit.Bukkit;
import space.arim.dazzleconf.error.BadValueException;
import space.arim.dazzleconf.serialiser.Decomposer;
import space.arim.dazzleconf.serialiser.FlexibleType;
import space.arim.dazzleconf.serialiser.ValueSerialiser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupSerialiser implements ValueSerialiser<Group> {
    public AdvancedPlHide core = (AdvancedPlHide) Bukkit.getPluginManager().getPlugin("AdvancedPlHide");

    @Override
    public Class<Group> getTargetClass() {
        return Group.class;
    }

    @Override
    public Group deserialise(FlexibleType flexibleType) throws BadValueException {
        return new Group((List<String>) flexibleType.getObject(List.class));
    }

    @Override
    public Object serialise(Group value, Decomposer decomposer) {
        List<String> defaultList = new ArrayList<>();
        defaultList.add("example1");
        defaultList.add("example2");
        return value.blacklist;
    }
}
