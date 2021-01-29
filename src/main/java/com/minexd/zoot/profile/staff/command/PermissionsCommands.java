package com.minexd.zoot.profile.staff.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.command.CommandSender;

public class PermissionsCommands {

    @Command(names = {"permissions add", "addperm", "permission add"}, permission = "static.playerperms", async = true)
    public static void add(CommandSender sender, @Param(name = "player") Profile profile, @Param(name = "permission") String permission) {
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

        sender.sendMessage(CC.translate("&cSuccessfully added the permission " + permission + " to " + profile.getColoredUsername() + "&a."));
    }

    @Command(names = {"permissions remove", "removeperm", "permission remove"}, permission = "static.playerperms.remove", async = true)
    public static void remove(CommandSender sender, @Param(name = "player") Profile profile, @Param(name = "permission") String permission) {
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

        sender.sendMessage(CC.translate("&cSuccessfully removed the permission " + permission + " from " + profile.getColoredUsername() + "&a."));
    }

}
