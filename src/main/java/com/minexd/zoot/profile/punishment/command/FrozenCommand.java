package com.minexd.zoot.profile.punishment.command;

import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FrozenCommand {

    @Command(names = {"ss", "freeze"}, permission = "static.staff.freeze")
    public static void freeze(CommandSender sender, @Param(name = "player") Player player) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());

        if (sender == player) {
            sender.sendMessage(CC.RED + "You cannot freeze your self");
            return;
        }
        profile.getOptions().setFrozen(!profile.getOptions().isFrozen());
        Bukkit.getOnlinePlayers().stream().filter(onlinePlayer ->
                onlinePlayer.hasPermission("static.staff")).forEach(onlinePlayer
                -> onlinePlayer.sendMessage(CC.translate("&9[S] " + player.getName() + " &aWas " + (profile.getOptions().isFrozen() ? "frozen" : "unfrozen") + " by " + sender.getName())));

        sender.sendMessage(profile.getOptions().isFrozen() ?
                CC.GREEN + "You have just frozen " + player.getDisplayName() + "." : CC.RED + "You have just unfrozen " + player.getDisplayName() + ".");

        if(!profile.getOptions().isFrozen()){
            player.sendMessage(CC.GREEN + "You have been unfrozen.");
            player.sendMessage(CC.GREEN + "enjoy the rest of your day.");
        }

    }

}
