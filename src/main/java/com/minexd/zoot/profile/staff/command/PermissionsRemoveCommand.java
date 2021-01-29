package com.minexd.zoot.profile.staff.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.util.CC;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class PermissionsRemoveCommand {

    @Command (names = {"permissions remove", "removeperm", "permission remove"}, permission = "zoot.admin.permissions.remove", async = true)
    public void execute(CommandSender sender, @Param(name = "player") Profile profile, @Param(name = "permission") String permission) {
        if (profile == null) {
            sender.sendMessage(Locale.PLAYER_NOT_FOUND.format());
            return;
        }

        if (!profile.getPermissions().contains(permission)) {
            sender.sendMessage(CC.translate( "&cThat player doesn't have the permission " + permission + "."));
            return;
        }

        profile.getPermissions().remove(permission);
        profile.save();

        if (profile.getPlayer() != null) {
            profile.setupBukkitPlayer(profile.getPlayer());
        }

        sender.sendMessage(CC.translate("&aSuccessfully removed the permission " + permission + " from " + profile.getColoredUsername() + "&a."));
    }
}
