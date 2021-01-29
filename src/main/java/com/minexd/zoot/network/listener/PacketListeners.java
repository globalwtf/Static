package com.minexd.zoot.network.listener;

import com.minexd.zoot.Zoot;
import com.minexd.zoot.ZootAPI;
import com.minexd.zoot.cache.RedisPlayerData;
import com.minexd.zoot.network.packet.PacketStaffJoinNetwork;
import com.minexd.zoot.network.packet.PacketStaffLeaveNetwork;
import com.minexd.zoot.network.packet.PacketStaffSwitchServer;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.status.ZootServer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PacketListeners implements Listener {

    private Profile profile;
    private static Map<String, ZootServer> servers = new ConcurrentHashMap<>();


}
