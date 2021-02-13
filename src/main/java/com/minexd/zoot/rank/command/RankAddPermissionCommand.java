package com.minexd.zoot.rank.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.rank.Rank;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.command.CommandSender;

public class RankAddPermissionCommand {

    @Command(names = {"rank addpermission", "rank addperm"}, permission = "static.admin.rank", async = true)
    public static void addperm(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "permission") String permission) {
        if (rank == null) {
            sender.sendMessage(Locale.RANK_NOT_FOUND.format());
            return;
        }

        permission = permission.toLowerCase().trim();

        if (rank.getPermissions().contains(permission)) {
            sender.sendMessage(CC.RED + "That rank is already assigned that permission.");
            return;
        }

        for (Rank childRank : rank.getInherited()) {
            if (childRank.hasPermission(permission)) {
                sender.sendMessage(CC.RED + "That rank is inheriting that permission from the " +
                        rank.getColor() + rank.getDisplayName() + " rank.");
                return;
            }
        }

        rank.getPermissions().add(permission);
        rank.save();
        rank.refresh();

        sender.sendMessage(CC.GREEN + "Successfully added permission to rank.");
    }

}
