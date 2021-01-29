package com.minexd.zoot.essentials.command;

import com.minexd.zoot.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import net.frozenorb.qlib.command.Command;
import org.bukkit.entity.Player;

@CommandMeta(label = "sunset", permission = "")
public class SunsetCommand {

    public void execute(Player player) {
        player.setPlayerTime(12000, false);
        player.sendMessage(CC.GREEN + "It's now sunset.");
    }

}
