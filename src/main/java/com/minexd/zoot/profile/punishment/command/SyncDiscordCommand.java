package com.minexd.zoot.profile.punishment.command;

import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.command.CommandSender;

public class SyncDiscordCommand {

    @Command(names = "synctest", permission = "zoot.twofa")
    public void syncCommand(CommandSender sender, @Param(name = "password") String password) {

    }
}
