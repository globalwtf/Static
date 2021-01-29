package com.minexd.zoot.profile.punishment.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.Zoot;
import com.minexd.zoot.ZootAPI;
import com.minexd.zoot.network.packet.PacketBroadcastPunishment;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.profile.punishment.Punishment;
import com.minexd.zoot.profile.punishment.PunishmentType;
import com.minexd.zoot.util.CC;
import com.minexd.zoot.util.duration.Duration;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Flag;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class IPBanCommand {

    @Command(names = {"banip", "ipban"}, permission = "static.staff.ipban", async = true)
    public static void ipban(CommandSender sender, @Flag(value = "s") boolean silent, @Param(name = "player") Profile profile, @Param(name = "duration") Duration duration, @Param(name = "reason", wildcard = true) String reason) {
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        if(sender instanceof Player && !sender.hasPermission("static.bypass")) {
            if (ZootAPI.getRankOfPlayer(((Player) sender)).getWeight() < profile.getActiveRank().getWeight()) {
                sender.sendMessage(ChatColor.RED + "You don't have the power to ban that player.");
                return;
            }
        }

        if (profile.getActivePunishmentByType(PunishmentType.IPBAN) != null) {
            sender.sendMessage(CC.RED + "That player is already ipbanned.");
            return;
        }

        if (duration.getValue() == -1) {
            sender.sendMessage(CC.RED + "That duration is not valid.");
            sender.sendMessage(CC.RED + "Example: [perm/1y1m1w1d]");
            return;
        }

        String staffName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender)
                .getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";

        Punishment punishment = new Punishment(UUID.randomUUID(), PunishmentType.IPBAN, System.currentTimeMillis(),
                reason, duration.getValue());

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
