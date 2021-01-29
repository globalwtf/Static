package com.minexd.zoot.profile.grant;

import com.minexd.zoot.Zoot;
import com.minexd.zoot.network.packet.PacketDeleteGrant;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.profile.grant.event.GrantAppliedEvent;
import com.minexd.zoot.profile.grant.event.GrantExpireEvent;
import com.minexd.zoot.profile.grant.procedure.GrantProcedure;
import com.minexd.zoot.profile.grant.procedure.GrantProcedureStage;
import com.minexd.zoot.profile.grant.procedure.GrantProcedureType;
import com.minexd.zoot.util.CC;
import com.minexd.zoot.util.TimeUtil;
import com.minexd.zoot.util.callback.TypeCallback;
import com.minexd.zoot.util.duration.Duration;
import net.frozenorb.qlib.menu.menus.ConfirmMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.concurrent.atomic.AtomicBoolean;

public class GrantListener implements Listener {

    @EventHandler
    public void onGrantAppliedEvent(GrantAppliedEvent event) {
        Player player = event.getPlayer();
        Grant grant = event.getGrant();

        player.sendMessage(CC.GREEN + ("A `{rank}` grant has been applied to you for {time-remaining}.")
                .replace("{rank}", grant.getRank().getDisplayName())
                .replace("{time-remaining}", grant.getDuration() == Integer.MAX_VALUE ?
                        "forever" : TimeUtil.millisToRoundedTime((grant.getAddedAt() + grant.getDuration()) -
                        System.currentTimeMillis())));

        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.setupBukkitPlayer(player);
    }

    @EventHandler
    public void onGrantExpireEvent(GrantExpireEvent event) {
        Player player = event.getPlayer();
        Grant grant = event.getGrant();

        player.sendMessage(CC.RED + ("Your `{rank}` grant has expired.")
                .replace("{rank}", grant.getRank().getDisplayName()));

        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.setupBukkitPlayer(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        if (!event.getPlayer().hasPermission("zoot.staff.grant")) {
            return;
        }

        GrantProcedure procedure = GrantProcedure.getByPlayer(event.getPlayer());

        if (procedure != null && procedure.getStage() == GrantProcedureStage.REQUIRE_TEXT) {
            event.setCancelled(true);

            if (event.getMessage().equalsIgnoreCase("cancel")) {
                procedure.cancel();
                GrantProcedure.getProcedures().remove(procedure);
                event.getPlayer().sendMessage(CC.RED + "You have cancelled the grant procedure.");
                return;
            }

            switch (procedure.getType()) {
                case REMOVE: {
                    AtomicBoolean removed = new AtomicBoolean(false);

                    new ConfirmMenu(CC.YELLOW + "Delete this grant?", data -> {
                        if (data) {
                            procedure.getGrant().setRemovedBy(event.getPlayer().getUniqueId());
                            procedure.getGrant().setRemovedAt(System.currentTimeMillis());
                            procedure.getGrant().setRemovedReason(event.getMessage());
                            procedure.getGrant().setRemoved(true);
                            procedure.finish();
                            event.getPlayer().sendMessage(CC.GREEN + "The grant has been removed.");

                            Zoot.get().getPidgin().sendPacket(new PacketDeleteGrant(procedure.getRecipient().getUuid(),
                                    procedure.getGrant()));
                            removed.set(true);
                        }
                    }) {
                        @Override
                        public void onClose(Player player) {
                            if (removed.get() != true) {
                                procedure.cancel();
                                event.getPlayer().sendMessage(CC.RED + "You did not confirm to remove the grant.");
                            }
                        }
                    }.openMenu(event.getPlayer());
                    break;
                }

                case GRANT: {
                    if (procedure.getGrant().getAddedReason().equals("§")) {
                        procedure.getGrant().setAddedReason(event.getMessage());
                        procedure.setStage(GrantProcedureStage.REQUIRE_TEXT);
                        event.getPlayer().sendMessage(CC.GREEN + "Type the duration of this grant in chat...");
                        break;
                    }

                    Duration duration = Duration.fromString(event.getMessage());
                    if (duration.getValue() == -1) {
                        event.getPlayer().sendMessage(CC.RED + "That duration is not valid.");
                        event.getPlayer().sendMessage(CC.RED + "Example: [perm/1y1m1w1d]");
                        break;
                    }

                    procedure.getGrant().setDuration(duration.getValue());
                    procedure.setStage(GrantProcedureStage.REQUIRE_CONFIRMATION);
                    event.getPlayer().sendMessage(CC.GREEN + "Please confirm this grant...");

                    AtomicBoolean confirmed = new AtomicBoolean(false);

                    new ConfirmMenu(CC.YELLOW + "Create this grant?", data -> {
                        if (data) {
                            confirmed.set(true);
                            procedure.getRecipient().getGrants().add(procedure.getGrant());
                            procedure.finish();
                            event.getPlayer().sendMessage(CC.GREEN + "You applied a `{rank}` grant to `{player}` for {time-remaining}."
                                    .replace("{rank}", procedure.getGrant().getRank().getDisplayName())
                                    .replace("{player}", procedure.getRecipient().getName())
                                    .replace("{time-remaining}", duration.getValue() == Integer.MAX_VALUE ? "forever"
                                            : TimeUtil.millisToRoundedTime(duration.getValue())));
                        }
                    }) {
                        @Override
                        public void onClose(Player player) {
                            if (confirmed.get() != true) {
                                procedure.cancel();
                                event.getPlayer().sendMessage(CC.RED + "You did not confirm to create the grant.");
                            }
                        }
                    }.openMenu(event.getPlayer());
                    break;
                }
            }
        }
    }

}
