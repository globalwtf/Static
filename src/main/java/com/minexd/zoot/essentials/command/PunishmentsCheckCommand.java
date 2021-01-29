package com.minexd.zoot.essentials.command;

import com.minexd.zoot.profile.Profile;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.entity.Player;

public class PunishmentsCheckCommand {

    @Command(names = {"puncheck", "checkpunishments"}, permission = "static.admin.check")
    public static void onCommand(Player sender, @Param(name = "target") Profile target) {
        if(target == null);

    }
}
