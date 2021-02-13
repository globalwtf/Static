package com.minexd.zoot.util.duration;

import net.frozenorb.qlib.command.ParameterType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class DurationTypeAdapter implements ParameterType<Duration> {

    @Override
    public Duration transform(CommandSender commandSender, String s) {
        return Duration.fromString(s);
    }

    @Override
    public List<String> tabComplete(Player player, Set<String> set, String s) {
        return null;
    }
}

