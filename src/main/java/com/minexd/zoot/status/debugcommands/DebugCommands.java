package com.minexd.zoot.status.debugcommands;

import com.minexd.zoot.status.StatusHandler;
import net.frozenorb.qlib.command.Command;
import org.bukkit.entity.Player;

public class DebugCommands {

    @Command(names = "listservers", permission = "static.admin")
    public static void onList (Player player) {
        StatusHandler.getServers().forEach((s, zootServer) -> {
            zootServer.getPlayers();
        });
    }
}
