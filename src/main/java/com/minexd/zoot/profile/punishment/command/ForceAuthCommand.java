package com.minexd.zoot.profile.punishment.command;

import com.minexd.zoot.profile.Profile;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ForceAuthCommand {

    @Command(names = "forceauth", permission = "op", async = true)
    public static void forceAuthCommand(CommandSender sender, @Param(name = "target") Player target) {
        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage("This is a console only command!");
            return;
        }
        Profile targetP = Profile.getByUuid(target.getUniqueId());
        targetP.setLastAuthenticated(System.currentTimeMillis());
        targetP.getOptions().setTwoFA(false);
    }
}
