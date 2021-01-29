package com.minexd.zoot.profile.staff.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AltsCommand {

    @Command(names = "alts", async = true, permission = "static.staff.alts")
    public static void alts(CommandSender sender, @Param(name = "player") Profile profile) {
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }
        profile.save();
        List<Profile> alts = new ArrayList<>();
        if (!Bukkit.getOfflinePlayer(profile.getUuid()).isOnline()) {
            alts = Profile.getByIpAddress(profile.getCurrentAddress());
            //for (String ipAddress: profile.getIpAddresses())
                //alts.addAll(Profile.getByIpAddress(ipAddress));
        } else
            for (UUID uuid: profile.getKnownAlts())
                alts.add(Profile.getByUuid(uuid));

        /*
        for (UUID altUuid : profile.getKnownAlts()) {
            Profile altProfile = Profile.getByUuid(altUuid);
            if (altProfile != null && altProfile.isLoaded()) {
                alts.add(altProfile);
            }
        }
*/
        if (alts.isEmpty()) {
            sender.sendMessage(CC.RED + "This player has no known alt accounts.");
        } else {
            StringBuilder builder = new StringBuilder();
            for (Profile altProfile : alts) {
                if (altProfile.getName().equals(profile.getName()))
                    continue;
                builder.append(altProfile.getName());
                builder.append(", ");
            }
            sender.sendMessage(CC.DARK_AQUA + "Alts: " + CC.RESET + builder.toString());
        }
    }

}
