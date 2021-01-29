package com.minexd.zoot.profile.conversation.command;

import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.profile.conversation.Conversation;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ReplyCommand {

    @Command(names = {"reply", "r"}, permission = "")
    public static void reply(Player player, @Param(name = "message", wildcard = true) String message) {
        Profile playerProfile = Profile.getByUuid(player.getUniqueId());
        Conversation conversation = playerProfile.getConversations().getLastRepliedConversation();

        if (conversation != null) {
            if (conversation.validate()) {
                conversation.sendMessage(player, Bukkit.getPlayer(conversation.getPartner(player.getUniqueId())), message);
            } else {
                player.sendMessage(CC.RED + "You can no longer reply to that player.");
            }
        } else {
            player.sendMessage(CC.RED + "You have nobody to reply to.");
        }
    }

}
