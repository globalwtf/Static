package com.minexd.zoot.profile.staff.command;

import com.minexd.zoot.profile.staff.menu.StaffOptionsMenu;
import net.frozenorb.qlib.command.Command;
import org.bukkit.entity.Player;

public class StaffOptionsCommands {

    @Command(names = {"staffsettings", "msettings"}, permission = "static.staff")
    public static void onSettings(Player player) {
        new StaffOptionsMenu().openMenu(player);
    }
}
