package com.minexd.zoot.profile.punishment.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.Zoot;
import com.minexd.zoot.network.packet.PacketBroadcastPunishment;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.profile.punishment.Punishment;
import com.minexd.zoot.profile.punishment.PunishmentType;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Flag;
import net.frozenorb.qlib.command.Param;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class KickCommand {

    @Command(names = "kick", permission = "static.staff.kick", async = true)
    public static void kick(CommandSender sender, @Flag(value = "s") boolean silent, @Param(name = "player") Player player, @Param(name = "reason", wildcard = true) String reason) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());

        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        String staffName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender)
                .getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";

        Punishment punishment = new Punishment(UUID.randomUUID(), PunishmentType.KICK, System.currentTimeMillis(),
                reason, -1);

        if (sender instanceof Player) {
            punishment.setAddedBy(((Player) sender).getUniqueId());
        }

        profile.getPunishments().add(punishment);
        profile.save();

        Zoot.get().getPidgin().sendPacket(new PacketBroadcastPunishment(punishment, staffName,
                profile.getColoredUsername(), profile.getUuid(), silent));

        new BukkitRunnable() {
            @Override
            public void run() {
                player.kickPlayer(punishment.getKickMessage());
            }
        }.runTask(Zoot.get());
    }

}
