package com.minexd.zoot.profile.punishment.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.Zoot;
import com.minexd.zoot.cache.RedisPlayerData;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.profile.punishment.menu.PunishmentsListMenu;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Flag;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PermsCheckCommand {

    @Command(names = {"permscheck", "pc"}, permission = "static.staff.pcheck", async = true)
    public static void check(Player player, @Param(name = "player") Profile profile) {
        if (profile == null || !profile.isLoaded()) {
            player.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.translate("&9" + profile.getActiveRank().getPrefix() + profile.getColoredUsername() + "'s &7Permissions" ));
        player.sendMessage(CC.CHAT_BAR);
        profile.getPermissions().forEach(permission ->
                player.sendMessage(CC.GRAY + "* " + CC.WHITE + permission));
    }

}
