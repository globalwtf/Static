package com.minexd.zoot.profile;

import net.frozenorb.qlib.command.ParameterType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProfileTypeAdapter implements ParameterType<Profile> {

    @Override
    public Profile transform(CommandSender commandSender, String s) {
        Profile profile = Profile.getByUsername(s);
        if(profile == null) commandSender.sendMessage(ChatColor.RED + "There is no such player with the name \"" + s + "\".");
        return profile;
    }

    @Override
    public List<String> tabComplete(Player player, Set<String> set, String s) {
        List<String> completed = new ArrayList<>();

        for (Profile profile : Profile.getProfiles().values()) {
            if (profile.getName() == null) continue;

            if (profile.getName().toLowerCase().startsWith(s.toLowerCase())) {
                completed.add(profile.getName());
            }
        }

        return completed;
    }
}
