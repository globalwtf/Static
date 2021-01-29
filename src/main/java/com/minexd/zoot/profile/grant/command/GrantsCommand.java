package com.minexd.zoot.profile.grant.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.profile.grant.menu.GrantsListMenu;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.entity.Player;

public class GrantsCommand {

    @Command(names = "grants", async = true, permission = "static.grants.show")
    public static void grants(Player player, @Param(name = "player") Profile profile) {
        if (profile == null || !profile.isLoaded()) {
            player.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        new GrantsListMenu(profile).openMenu(player);
    }

}