package com.minexd.zoot.profile.punishment.command;

import com.minexd.zoot.Zoot;
import com.minexd.zoot.network.packet.PacketCreateDiscSyncCode;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.entity.Player;

public class DiscordSyncCommand {

    @Command(names = "sync", permission = "", async = true)
    public static void discordSyncCommand(Player player) {
        String password = randomString();
        Zoot.get().getPidgin().sendPacket(new PacketCreateDiscSyncCode(player.getUniqueId(), password));
        player.sendMessage(CC.translate("&7Please enter this in the #sync channel of our discord.:&7\n " +
        	    "&7Discord: &bhttps://coral.gg/discord&a\n" + password));
        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.setSync(password);
        profile.save();
    }

    public static String randomString() {
        return RandomStringUtils.random(7, true, true);
    }
}
