package com.minexd.zoot.profile.punishment.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.Zoot;
import com.minexd.zoot.cache.RedisPlayerData;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.profile.punishment.menu.PunishmentsListMenu;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Flag;
import net.frozenorb.qlib.command.Param;
import org.bukkit.entity.Player;

public class CheckCommand {

    @Command(names = {"check", "c"}, permission = "static.staff.check", async = true)
    public static void check(Player player, @Param(name = "player") Profile profile) {
        if (profile == null || !profile.isLoaded()) {
            player.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        RedisPlayerData redisPlayerData = Zoot.get().getRedisCache().getPlayerData(profile.getUuid());

        if (redisPlayerData == null) {
            player.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        new PunishmentsListMenu(profile, redisPlayerData).openMenu(player);
    }

}
