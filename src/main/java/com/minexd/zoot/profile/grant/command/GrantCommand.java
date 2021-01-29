package com.minexd.zoot.profile.grant.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.profile.grant.menu.GrantCreateMenu;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.entity.Player;

public class GrantCommand {

    @Command(names = "grant", async = true, permission = "static.grants.add")
    public static void grant(Player sender, @Param(name = "player") Profile profile) {
        Profile executor = Profile.getByUuid(sender.getUniqueId());

        if (executor == null || !executor.isLoaded() || profile == null || !profile.isLoaded()) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        new GrantCreateMenu(profile).openMenu(sender);
    }

}