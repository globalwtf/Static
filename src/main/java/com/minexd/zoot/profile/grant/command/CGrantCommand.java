package com.minexd.zoot.profile.grant.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.Zoot;
import com.minexd.zoot.network.packet.PacketAddGrant;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.profile.grant.Grant;
import com.minexd.zoot.profile.grant.event.GrantAppliedEvent;
import com.minexd.zoot.rank.Rank;
import com.minexd.zoot.util.CC;
import com.minexd.zoot.util.TimeUtil;
import com.minexd.zoot.util.duration.Duration;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CGrantCommand {

    @Command(names = "setrank", async = true, permission = "static.grants.add")
    public static void setrank(CommandSender sender, @Param(name = "player") Profile profile, @Param(name = "rank") Rank rank, @Param(name = "duration") Duration duration, @Param(name = "reason", wildcard = true) String reason) {
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }
        if (duration.getValue() == -1) {
            sender.sendMessage(CC.RED + "That duration is not valid.");
            sender.sendMessage(CC.RED + "Example: [perm/1y1m1w1d]");
            return;
        }

        UUID addedBy = sender instanceof Player ? ((Player) sender).getUniqueId() : null;
        Grant grant = new Grant(UUID.randomUUID(), rank, addedBy, System.currentTimeMillis(), reason,
                duration.getValue());

        profile.getGrants().add(grant);
        profile.save();
        profile.activateNextGrant();

        Zoot.get().getPidgin().sendPacket(new PacketAddGrant(profile.getUuid(), grant));

        sender.sendMessage(CC.GREEN + "You applied a `{rank}` grant to `{player}` for {time-remaining}."
                .replace("{rank}", rank.getDisplayName())
                .replace("{player}", profile.getName())
                .replace("{time-remaining}", duration.getValue() == Integer.MAX_VALUE ? "forever"
                        : TimeUtil.millisToRoundedTime(duration.getValue())));

        Player player = profile.getPlayer();

        if (player != null) {
            new GrantAppliedEvent(player, grant).call();
        }
    }

}
