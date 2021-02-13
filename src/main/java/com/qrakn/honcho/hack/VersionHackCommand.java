package com.qrakn.honcho.hack;

import com.qrakn.honcho.Honcho;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Arrays;

public class VersionHackCommand extends Command {
    private final Honcho honcho;

    public VersionHackCommand(final Honcho honcho) {
        super("versionhack", "Honcho version hack", "/vh <cmd>", Arrays.asList("vh", "eval"));
        this.honcho = honcho;
    }

    public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            final String fullCommand = StringUtils.join(args, " ");
            System.out.println("Executing VH command \"" + fullCommand + "\"");
            this.honcho.handleExecution(sender, "/" + fullCommand);
            return true;
        }
        return false;
    }
}
