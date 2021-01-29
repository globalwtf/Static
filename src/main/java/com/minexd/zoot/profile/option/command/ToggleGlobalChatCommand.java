package com.minexd.zoot.profile.option.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.profile.Profile;
import net.frozenorb.qlib.command.Command;
import org.bukkit.entity.Player;

public class ToggleGlobalChatCommand {

    @Command(names = {"toggleglobalchat", "tgc", "togglepublicchat", "tpc"}, permission = "")
    public static void tgc(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.getOptions().setPublicChatEnabled(!profile.getOptions().isPublicChatEnabled());

        if (profile.getOptions().isPublicChatEnabled()) {
            player.sendMessage(Locale.OPTIONS_GLOBAL_CHAT_ENABLED.format());
        } else {
            player.sendMessage(Locale.OPTIONS_GLOBAL_CHAT_DISABLED.format());
        }
    }

}
