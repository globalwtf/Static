package com.minexd.zoot.profile.punishment.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.Zoot;
import com.minexd.zoot.ZootAPI;
import com.minexd.zoot.network.packet.PacketBroadcastPunishment;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.profile.punishment.Punishment;
import com.minexd.zoot.profile.punishment.PunishmentType;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Flag;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class BlacklistCommand {

    @Command(names = "blacklist", permission = "static.admin.blacklist", async = true)
    public static void blacklist(CommandSender sender, @Flag(value = "s") boolean silent, @Param(name = "player") Profile profile, @Param(name = "reason", wildcard = true) String reason) {
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        if(sender instanceof Player && !sender.hasPermission("static.bypass.blacklist")) {
            if (ZootAPI.getRankOfPlayer(((Player) sender)).getWeight() < profile.getActiveRank().getWeight()) {
                sender.sendMessage(ChatColor.RED + "You don't have the power to ban that player.");
                return;
            }
        }

        if (profile.getActivePunishmentByType(PunishmentType.BLACKLIST) != null) {
            sender.sendMessage(CC.RED + "That player is already Blacklisted.");
            return;
        }

        String staffName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender)
                .getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";

        Punishment punishment = new Punishment(UUID.randomUUID(), PunishmentType.BLACKLIST, System.currentTimeMillis(),
                reason, Integer.MAX_VALUE);

        if (sender instanceof Player) {
            punishment.setAddedBy(((Player) sender).getUniqueId());
        }

        profile.getPunishments().add(punishment);
        profile.save();

        Zoot.get().getPidgin().sendPacket(new PacketBroadcastPunishment(punishment, staffName,
                profile.getColoredUsername(), profile.getUuid(), silent));

        Player player = profile.getPlayer();

        if (player != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.kickPlayer(punishment.getKickMessage());
                }
            }.runTask(Zoot.get());
        }
    }


}
