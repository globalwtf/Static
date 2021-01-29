package com.minexd.zoot.profile.punishment.command;

import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.profile.punishment.AuthCode;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import org.bukkit.command.CommandSender;

public class ShowAuthKeys {

    @Command(names = "showauthkeys", permission = "zoot.owner")
    public static void ShowAuthKeysCommand(CommandSender sender) {
        for (AuthCode key : Profile.getAuthCodes()) {
            sender.sendMessage(CC.MENU_BAR);
            sender.sendMessage(key.getAuthKey());
            sender.sendMessage(key.getDiscordID());
            sender.sendMessage(String.valueOf(key.getAuthCreatedAt()));
            sender.sendMessage(CC.MENU_BAR);
        }
    }
}
