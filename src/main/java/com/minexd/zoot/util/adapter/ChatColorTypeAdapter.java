package com.minexd.zoot.util.adapter;

import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.ParameterType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ChatColorTypeAdapter implements ParameterType<ChatColor> {

    @Override
    public ChatColor transform(CommandSender commandSender, String s) {
        return CC.getColorFromName(s);
    }

    @Override
    public List<String> tabComplete(Player player, Set<String> set, String s) {
        final String compare = s.trim().toLowerCase();

        List<String> completed = new ArrayList<>();

        for (String colorName : CC.getColorNames()) {
            if (colorName.startsWith(compare)) {
                completed.add(colorName);
            }
        }

        return completed;
    }
}
