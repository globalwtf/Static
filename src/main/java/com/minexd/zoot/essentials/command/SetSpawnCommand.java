package com.minexd.zoot.essentials.command;

import com.minexd.zoot.Zoot;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import org.bukkit.entity.Player;

public class SetSpawnCommand {

    @Command(names = "setspawn", permission = "static.setspawn")
    public static void setspawn(Player player) {
        Zoot.get().getEssentials().setSpawn(player.getLocation());
        player.sendMessage(CC.GREEN + "You updated this world's spawn.");
    }

}
