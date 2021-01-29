package com.minexd.zoot.profile.option.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.profile.Profile;
import net.frozenorb.qlib.command.Command;
import org.bukkit.entity.Player;

public class ToggleSoundsCommand {

    @Command(names = {"togglesounds", "sounds"}, permission = "")
    public static void sounds(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.getOptions().setPlayingMessageSounds(!profile.getOptions().isPlayingMessageSounds());

        if (profile.getOptions().isPlayingMessageSounds()) {
            player.sendMessage(Locale.OPTIONS_PRIVATE_MESSAGE_SOUND_ENABLED.format());
        } else {
            player.sendMessage(Locale.OPTIONS_PRIVATE_MESSAGE_SOUND_DISABLED.format());
        }
    }
}
