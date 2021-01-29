package com.minexd.zoot.profile.staff.command;

import com.minexd.zoot.Zoot;
import com.minexd.zoot.ZootAPI;
import com.minexd.zoot.network.packet.PacketStaffChat;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class StaffChatCommand {

    @Command(names = {"staffchat", "sc"}, permission = "static.staff")
    public static void sc(Player player, @Param(name = "message", wildcard = true, defaultValue = "§") String message) {
        if(message.equals("§")) {
            Profile profile = Profile.getProfiles().get(player.getUniqueId());
            profile.getStaffOptions().staffChatModeEnabled(!profile.getStaffOptions().staffChatModeEnabled());
            if (profile.getStaffOptions().staffChatModeEnabled())
                profile.getStaffOptions().adminChatModeEnabled(false);
            player.sendMessage(profile.getStaffOptions().staffChatModeEnabled() ?
                    CC.GREEN + "You are now talking in staff chat." : CC.RED + "You are no longer talking in staff chat.");
        }else {
            Profile profile = Profile.getProfiles().get(player.getUniqueId());

            if (!profile.getStaffOptions().staffModeEnabled()) {
                player.sendMessage(CC.RED + "You are not in staff mode.");
                return;
            }

            Zoot.get().getPidgin().sendPacket(new PacketStaffChat(ZootAPI.getColoredName(player),
                    Bukkit.getServerId(), message));
        }
    }


}
