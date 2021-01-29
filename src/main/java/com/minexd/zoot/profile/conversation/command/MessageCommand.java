package com.minexd.zoot.profile.conversation.command;

import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.profile.conversation.Conversation;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.entity.Player;

public class MessageCommand {

    @Command(names = {"message", "msg", "whisper", "tell", "t", "m"}, permission = "")
    public static void message(Player player, @Param(name = "target") Player target, @Param(name = "message", wildcard = true) String message) {
        if (player.equals(target)) {
            player.sendMessage(CC.RED + "You cannot message yourself!");
            return;
        }

        if (target == null) {
            player.sendMessage(CC.RED + "A player with that name could not be found.");
            return;
        }

        Profile playerProfile = Profile.getByUuid(player.getUniqueId());
        Profile targetProfile = Profile.getByUuid(target.getUniqueId());

        if (targetProfile.getConversations().canBeMessagedBy(player)) {
            Conversation conversation = playerProfile.getConversations().getOrCreateConversation(target);

            if (conversation.validate()) {
                conversation.sendMessage(player, target, message);
            } else {
                player.sendMessage(CC.RED + "That player is not receiving new conversations right now.");
            }
        } else {
            player.sendMessage(CC.RED + "That player is not receiving new conversations right now.");
        }
    }

}
