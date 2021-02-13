package com.minexd.zoot.rank.command;

import com.minexd.zoot.rank.Rank;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.command.CommandSender;

public class RankRemovePermissionCommand {

    @Command(names = {"rank removepermission", "rank removeperm", "rank deleteperm", "rank delperm"}, permission = "static.admin.rank", async = true)
    public static void removeperm(CommandSender sender, @Param(name = "rank") Rank rank, @Param(name = "permission") String permission) {
        permission = permission.toLowerCase().trim();

        if (!rank.getPermissions().contains(permission)) {
            sender.sendMessage(CC.RED + "That rank is not assigned that permission.");
        } else {
            rank.getPermissions().remove(permission);
            rank.save();
            rank.refresh();

            sender.sendMessage(CC.GREEN + "Successfully removed permission from rank.");
        }
    }

}
