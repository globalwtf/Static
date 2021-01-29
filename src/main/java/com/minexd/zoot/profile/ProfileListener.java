package com.minexd.zoot.profile;

import com.minexd.zoot.Locale;
import com.minexd.zoot.Zoot;
import com.minexd.zoot.ZootAPI;
import com.minexd.zoot.cache.RedisPlayerData;
import com.minexd.zoot.network.packet.*;
import com.minexd.zoot.profile.punishment.Punishment;
import com.minexd.zoot.profile.punishment.PunishmentType;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Param;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class ProfileListener implements Listener {

    private static Zoot plugin = Zoot.get();

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        Player player = Bukkit.getPlayer(event.getUniqueId());
        // Need to check if player is still logged in when receiving another login attempt
        // This happens when a player using a custom client can access the server list while in-game (and reconnecting)
        if (player != null && player.isOnline()) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(CC.RED + "You tried to login too quickly after disconnecting.\nTry again in a few seconds.");
            plugin.getServer().getScheduler().runTask(plugin, () -> player.kickPlayer(CC.RED + "Duplicate login kick"));
            return;
        }
        RedisPlayerData playerData = new RedisPlayerData(event.getUniqueId(), event.getName());
        Profile profile = null;
        try {
            profile = new Profile(event.getName(), event.getUniqueId());

            if (!profile.isLoaded()) {
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                event.setKickMessage(Locale.FAILED_TO_LOAD_PROFILE.format());
                return;
            }

            if (profile.getActivePunishmentByType(PunishmentType.BAN) != null) {
                handleBan(event, profile.getActivePunishmentByType(PunishmentType.BAN));
                return;
            }
            if (profile.getActivePunishmentByType(PunishmentType.BLACKLIST) != null) {
                handleBan(event, profile.getActivePunishmentByType(PunishmentType.BLACKLIST));
                return;
            }
            if (profile.getActivePunishmentByType(PunishmentType.IPBAN) != null) {
                handleBan(event, profile.getActivePunishmentByType(PunishmentType.IPBAN));
                return;
            }

            profile.setName(event.getName());


            if (profile.getFirstSeen() == null) {
                profile.setFirstSeen(System.currentTimeMillis());
            }

            profile.setLastSeen(System.currentTimeMillis());
            if (profile.getCurrentAddress() == null) {
                profile.setCurrentAddress(event.getAddress().getHostAddress());
            }

            if (!profile.getIpAddresses().contains(event.getAddress().getHostAddress())) {
                profile.getIpAddresses().add(event.getAddress().getHostAddress());
            }
            if (profile.getAuthedips().isEmpty())
                profile.getAuthedips().add(event.getAddress().getHostAddress());

            if (!profile.getAuthedips().contains(profile.getCurrentAddress()) && profile.getAuthedips() != null) {
                Zoot.get().getLogger().info("User is not on an authed IP address.");
                if (profile.getActiveRank().getAllPermissions().contains("zoot.twofa")) {
                    profile.getOptions().setTwoFA(true);
                } else {
                    Zoot.get().getLogger().info("User does not have to be 2fa.");
                }
            }

            //if (!profile.getCurrentAddress().equals(event.getAddress().getHostAddress())) {
                List<Profile> alts = Profile.getByIpAddress(event.getAddress().getHostAddress());
                alts.add(profile);
            for (Profile alt: alts)
                profile.getKnownAlts().add(alt.getUuid());

                for (Profile alt: alts) {
                    if (alt.getActivePunishmentByType(PunishmentType.BAN) != null) {
                        handleBan(event, alt.getActivePunishmentByType(PunishmentType.BAN));
                        return;
                    }
                    if (alt.getActivePunishmentByType(PunishmentType.IPBAN) != null) {
                        handleBan(event, alt.getActivePunishmentByType(PunishmentType.IPBAN));
                        return;
                    }
                    if (alt.getActivePunishmentByType(PunishmentType.BLACKLIST) != null) {
                        handleBan(event, alt.getActivePunishmentByType(PunishmentType.BLACKLIST));
                        return;
                    }
                }
            //}

            profile.save();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (profile == null || !profile.isLoaded()) {
            event.setKickMessage(Locale.FAILED_TO_LOAD_PROFILE.format());
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            return;
        }
        Profile.getProfiles().put(profile.getUuid(), profile);
        plugin.getRedisCache().updateNameAndUUID(event.getName(), event.getUniqueId());
        plugin.getRedisCache().updateNameAndUUID(event.getName(), event.getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getProfiles().get(player.getUniqueId());

        profile.setupBukkitPlayer(player);

        new Thread(() -> {
            RedisPlayerData playerData = Zoot.get().getRedisCache().getPlayerData(profile.getUuid());

            if(playerData.getLastSeenServer() != null && !playerData.getLastSeenServer().equals(Bukkit.getServerName()) && System.currentTimeMillis() - profile.getLastSeen() < 100) {
                playerData.setLastAction(RedisPlayerData.LastAction.SWITCHING_SERVER);
            }else {
                playerData.setLastAction(RedisPlayerData.LastAction.JOINING_SERVER);
                if (player.hasPermission("zoot.staff")) {
                    Zoot.get().getPidgin().sendPacket(new PacketStaffJoinNetwork(player.getDisplayName(), Bukkit.getServerName()));
                }
                playerData.setLastAction(RedisPlayerData.LastAction.ONLINE);
            }
            if(playerData.getLastAction() == RedisPlayerData.LastAction.SWITCHING_SERVER){
                if (player.hasPermission("zoot.staff")) {
                    Zoot.get().getPidgin().sendPacket(new PacketStaffSwitchServer(player.getDisplayName(), playerData.getLastSeenServer() == null ? "N/A" : playerData.getLastSeenServer(), Bukkit.getServerName()));
                }
                playerData.setLastAction(RedisPlayerData.LastAction.ONLINE);
            } else {
                Zoot.get().getPidgin().sendPacket(new PacketStaffJoinNetwork(player.getDisplayName(), Bukkit.getServerName()));
                playerData.setLastAction(RedisPlayerData.LastAction.ONLINE);
                if (player.hasPermission("zoot.staff"))
                    player.sendMessage(profile.getStaffOptions().staffModeEnabled() ? (CC.GREEN + "You are currently viewing staff related messages.") : (CC.RED + "You are not viewing staff related messages."));
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    playerData.setLastSeenServer(Bukkit.getServerId());
                    playerData.setLastSeenAt(System.currentTimeMillis());
                    plugin.getRedisCache().updatePlayerData(playerData);
                }
            }.runTaskLater(Zoot.get(), 50);
        }).start();




}

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        RedisPlayerData playerData = new RedisPlayerData(event.getPlayer().getUniqueId(), event.getPlayer().getName());
        Profile profile = Profile.getProfiles().remove(event.getPlayer().getUniqueId());
        Player player = event.getPlayer();
        profile.setLastSeen(System.currentTimeMillis());

        if (profile.isLoaded()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    profile.save();
                }
            }.runTaskAsynchronously(Zoot.get());
        }

        playerData.setLastSeenAt(System.currentTimeMillis());
        playerData.setLastSeenServer(Bukkit.getServerName());

        if(!playerData.getLastAction().equals(RedisPlayerData.LastAction.SWITCHING_SERVER)) {
            Zoot.get().getPidgin().sendPacket(new PacketStaffLeaveNetwork(player.getDisplayName(), Bukkit.getServerName()));
            playerData.setLastAction(RedisPlayerData.LastAction.LEAVING_SERVER);
        }

        plugin.getRedisCache().updatePlayerData(playerData);

    }

    @EventHandler(priority = EventPriority.LOW)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Profile profile = Profile.getByUuid(event.getPlayer().getUniqueId());

        if (profile.getStaffOptions().staffChatModeEnabled()) {
            if (profile.getStaffOptions().staffModeEnabled()) {
                Zoot.get().getPidgin().sendPacket(new PacketStaffChat(
                        ZootAPI.getColoredName(event.getPlayer()), Bukkit.getServerId(), event.getMessage()));
            } else {
                event.getPlayer().sendMessage(CC.RED + "You must enable staff mode or disable staff chat mode.");
            }

            event.setCancelled(true);
        }
        if (profile.getStaffOptions().adminChatModeEnabled()) {
            Zoot.get().getPidgin().sendPacket(new PacketAdminChat(
                    ZootAPI.getColoredName(event.getPlayer()), Bukkit.getServerId(), event.getMessage()));
            event.setCancelled(true);
        }

    }



    private void handleBan(AsyncPlayerPreLoginEvent event, Punishment punishment) {
        event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        event.setKickMessage(punishment.getKickMessage());
    }

}
