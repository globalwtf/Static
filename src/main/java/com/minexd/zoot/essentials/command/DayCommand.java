package com.minexd.zoot.essentials.command;

import com.minexd.zoot.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import net.frozenorb.qlib.command.Command;
import org.bukkit.entity.Player;

@CommandMeta(label = "day", permission = "")
public class DayCommand {

    public void execute(Player player) {
        player.setPlayerTime(6000L, false);
        player.sendMessage(CC.GREEN + "It's now day time.");
    }

}