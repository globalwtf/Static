package com.minexd.zoot.util.adapter;

import net.frozenorb.qlib.command.ParameterType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GameModeParameter implements ParameterType<GameMode> {

    public GameMode transform(final CommandSender sender, final String source) {
        if (!source.equals("-0*toggle*0-") || !(sender instanceof Player)) {
            for (final GameMode mode : GameMode.values()) {
                if (mode.name().equalsIgnoreCase(source)) {
                    return mode;
                }
                if (String.valueOf(mode.getValue()).equalsIgnoreCase(source)) {
                    return mode;
                }
                if (StringUtils.startsWithIgnoreCase(mode.name(), source)) {
                    return mode;
                }
            }
            sender.sendMessage(ChatColor.RED + "No gamemode with the name " + source + " found.");
            return null;
        }
        if (((Player)sender).getGameMode() != GameMode.CREATIVE) {
            return GameMode.CREATIVE;
        }
        return GameMode.SURVIVAL;
    }

    public List<String> tabComplete(final Player sender, final Set<String> flags, final String source) {
        final List<String> completions = new ArrayList<String>();
        for (final GameMode mode : GameMode.values()) {
            if (StringUtils.startsWithIgnoreCase(mode.name(), source)) {
                completions.add(mode.name());
            }
        }
        return completions;
    }
}
