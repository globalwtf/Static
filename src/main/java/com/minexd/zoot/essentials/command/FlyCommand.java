package com.minexd.zoot.essentials.command;

import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import org.bukkit.entity.Player;

public class FlyCommand {
    @Command(names = "fly", permission = "static.fly")
    public static void onCommand(Player player) {

        player.setAllowFlight(!player.getAllowFlight());
        player.sendMessage(CC.translate("&aYour flight has been updated to " + player.getAllowFlight()));
    }

}
