package com.minexd.zoot.profile.punishment.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.Zoot;
import com.minexd.zoot.network.packet.PacketBroadcastPunishment;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.profile.punishment.Punishment;
import com.minexd.zoot.profile.punishment.PunishmentType;
import com.minexd.zoot.util.CC;
import com.minexd.zoot.util.duration.Duration;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Flag;
import net.frozenorb.qlib.command.Param;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MuteCommand {

    @Command(names = "mute", permission = "static.staff.mute", async = true)
    public static void mute(CommandSender sender, @Flag(value = "s") boolean silent, @Param(name = "player") Profile profile, @Param(name = "duration") Duration duration, @Param(name = "reason", wildcard = true) String reason) {
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        if (profile.getActivePunishmentByType(PunishmentType.MUTE) != null) {
            sender.sendMessage(CC.RED + "That player is already muted.");
            return;
        }

        if (duration.getValue() == -1) {
            sender.sendMessage(CC.RED + "That duration is not valid.");
            sender.sendMessage(CC.RED + "Example: [perm/1y1m1w1d]");
            return;
        }

        String staffName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender)
                .getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";

        Punishment punishment = new Punishment(UUID.randomUUID(), PunishmentType.MUTE, System.currentTimeMillis(),
                reason, duration.getValue());

        if (sender instanceof Player) {
            punishment.setAddedBy(((Player) sender).getUniqueId());
        }

        profile.getPunishments().add(punishment);
        profile.save();

        Player player = profile.getPlayer();

        if (player != null) {
            String senderName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender).getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";
            player.sendMessage(CC.RED + "You have been " + punishment.getContext() + " by " +
                    senderName + CC.RED + ".");
            player.sendMessage(CC.RED + "The reason for this punishment: " + CC.WHITE +
                    punishment.getAddedReason());

            if (!punishment.isPermanent()) {
                player.sendMessage(CC.RED + "This mute will expire in " + CC.WHITE +
                        punishment.getTimeRemaining() + CC.RED + ".");
            }
        }

        Zoot.get().getPidgin().sendPacket(new PacketBroadcastPunishment(punishment, staffName,
                profile.getColoredUsername(), profile.getUuid(), silent));
    }


}
