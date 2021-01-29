package com.minexd.zoot.essentials.command;

import com.minexd.zoot.Zoot;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand {

    @Command(names = "spawn", permission = "static.spawn")
    public static void spawn(CommandSender sender, @Param(name = "player", defaultValue = "self") Player target) {
        if(sender == target) {
            Zoot.get().getEssentials().teleportToSpawn(target);
            sender.sendMessage(CC.GREEN + "You teleported to this world's spawn.");
        }else {
            Zoot.get().getEssentials().teleportToSpawn(target);
            target.sendMessage(CC.GREEN + "You have teleported " + target.getName() + " to this world's spawn.");
            target.sendMessage(CC.GREEN + "You have been teleported to this world's spawn.");

        }
    }


}
