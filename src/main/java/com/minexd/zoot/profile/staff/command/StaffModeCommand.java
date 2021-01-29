package com.minexd.zoot.profile.staff.command;

import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import org.bukkit.entity.Player;

public class StaffModeCommand {

    @Command(names = {"staffmessages", "tsm", "sm"}, permission = "static.staff")
    public static void tsm(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.getStaffOptions().staffModeEnabled(!profile.getStaffOptions().staffModeEnabled());

        player.sendMessage(profile.getStaffOptions().staffModeEnabled() ?
                CC.GREEN + "You are now viewing staff related messages." : CC.RED + "You are no longer viewing staff related messages.");
    }


}
