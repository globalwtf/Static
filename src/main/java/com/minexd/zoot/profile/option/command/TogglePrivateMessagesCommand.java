package com.minexd.zoot.profile.option.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.profile.Profile;
import net.frozenorb.qlib.command.Command;
import org.bukkit.entity.Player;

public class TogglePrivateMessagesCommand {

    @Command(names = {"togglepm", "togglepms", "tpm", "tpms"}, permission = "")
    public static void tpm(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.getOptions().setReceivingNewConversations(!profile.getOptions().isReceivingNewConversations());
        profile.getConversations().expireAllConversations();

        if (profile.getOptions().isReceivingNewConversations()) {
            player.sendMessage(Locale.OPTIONS_PRIVATE_MESSAGES_ENABLED.format());
        } else {
            player.sendMessage(Locale.OPTIONS_PRIVATE_MESSAGES_DISABLED.format());
        }
    }
}
