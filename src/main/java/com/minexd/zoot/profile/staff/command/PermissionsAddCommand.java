package com.minexd.zoot.profile.staff.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.util.CC;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PermissionsAddCommand {
    @Command(names = {"permissions add", "addperm", "permission add"}, permission = "static.playerperms.add", async = true)
    public static void execute(Player sender, @Param(name = "player") Profile profile, @Param(name = "permission") String permission) {
        if (profile == null) {
            sender.sendMessage(Locale.PLAYER_NOT_FOUND.format());
            return;
        }

        if (profile.getPermissions().contains(permission)) {
            sender.sendMessage(CC.translate("&cThat player already has the permission " + permission + "."));
            return;
        }

        profile.getPermissions().add(permission);
        profile.save();

        if (profile.getPlayer() != null) {
            profile.setupBukkitPlayer(profile.getPlayer());
        }

        sender.sendMessage(CC.translate("&cSuccessfully added the permission " + permission + " to " + profile.getColoredUsername() + "&c."));
    }
}
