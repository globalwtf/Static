package com.minexd.zoot.essentials.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Flag;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by vape on 5/28/2020 at 12:34 AM.
 */

@CommandMeta(label = "gma", permission = "static.gamemode")
public class GameModeAdventureCommand {

    public void execute(Player player) {
        player.setGameMode(GameMode.ADVENTURE);
        player.updateInventory();
        player.sendMessage(CC.GREEN + "Your gamemode has been updated.");
    }

    public void execute(CommandSender sender, @Flag("player") Player target) {
        if (target == null) {
            sender.sendMessage(Locale.PLAYER_NOT_FOUND.format());
            return;
        }

        target.setGameMode(GameMode.ADVENTURE);
        target.updateInventory();
        sender.sendMessage(CC.GREEN + "You have updated " + target.getName() + "'s gamemode.");
        target.sendMessage(CC.GREEN + "Your gamemode has been updated.");
    }

}