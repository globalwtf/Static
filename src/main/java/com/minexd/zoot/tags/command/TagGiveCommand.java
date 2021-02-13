package com.minexd.zoot.tags.command;

import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.tags.PlayerTag;
import com.minexd.zoot.tags.TagManager;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Flag;
import net.frozenorb.qlib.command.Param;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TagGiveCommand {

    @Command(names = "tag give", permission = "static.givetag")
    public static void give(CommandSender sender, @Param(name = "player") Player player, @Param(name = "id") String tagIdOrAll) {
        boolean all = tagIdOrAll.equalsIgnoreCase("all");
        PlayerTag playerTag = TagManager.getById(tagIdOrAll);

        if (playerTag == null && !all) {
            sender.sendMessage(CC.RED + "A tag does not exist with this ID.");
            return;
        }

        Profile profile = Profile.getByUuid(player.getUniqueId());
        if (profile == null) return;

        if (all) {
            profile.giveAllTags();
        } else profile.giveTag(playerTag);

        profile.save();

        if (all)
            sender.sendMessage(CC.GREEN + "Gave all tags to " + player.getDisplayName());
        else
            sender.sendMessage(CC.GREEN + "Gave " + playerTag.getId() + " to " + player.getDisplayName());
    }


}