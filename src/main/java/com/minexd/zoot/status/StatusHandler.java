package com.minexd.zoot.status;

import com.minexd.zoot.Zoot;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class StatusHandler {

    @Getter
    private static Map<String, ZootServer> servers = new ConcurrentHashMap<>();

    public StatusHandler() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(Zoot.get(), () -> update(Bukkit.getServerName(), Bukkit.hasWhitelist(), Bukkit.getOnlinePlayers().size(), Bukkit.getMaxPlayers() /* Bukkit.getWhitelistedPlayers(), Bukkit.getOnlinePlayers()*/), 20L, 20L);
        Bukkit.getScheduler().runTaskTimerAsynchronously(Zoot.get(), this::fetch, 20L, 20L);
    }

    public static ZootServer getServer(String id) {
        return servers.get(id.toLowerCase());
    }

    public static int getTotalPlayers() {
        return servers.values().stream().filter(ZootServer::isOnline).flatMapToInt(ZootServer::getPlayersAsStream).sum();
    }

    private void fetch() {
        try (Jedis jedis = Zoot.get().getJedisPool().getResource()) {
            for (String keyName : jedis.keys("status:*")) {
                Map<String, String> data = jedis.hgetAll(keyName);

                setStatus(
                        keyName.split(":")[1],

                        Boolean.parseBoolean(data.get("whitelist")),

                        Long.parseLong(data.get("heartbeat")),
                        Integer.parseInt(data.get("players")),
                        Integer.parseInt(data.get("maxPlayers"))
                        //TODO Whitelisted Players and Online Players

                );
            }
        }
    }

    private void setStatus(String serverId, boolean whitelisted, long heartbeat, int players, int maxPlayers /*Collection<OfflinePlayer>whitelistedPlayers, Collection<? extends Player>onlinePlayers*/) {
        ZootServer server = servers.computeIfAbsent(serverId.toLowerCase(), id -> new ZootServer(id, false,0L, 0, 0/*new HashSet<>(), new HashSet<>()*/));

        server.setWhitelisted(whitelisted);
        server.setLastHeartbeat(heartbeat);
        server.setPlayers(players);
        server.setMaxPlayers(maxPlayers);
//        server.setOnlinePlayers((Collection<? extends Player>) whitelistedPlayers);
//        server.setWhitelistedPlayers((Collection<OfflinePlayer>) onlinePlayers);

        servers.put(serverId.toLowerCase(), server);
    }

    private void update(String serverId, boolean whitelist, int players, int maxPlayers /*Collection<OfflinePlayer>whitelistedPlayers, Collection<? extends Player>onlinePlayers*/ ) {
        try (Jedis jedis = Zoot.get().getJedisPool().getResource()) {
            jedis.hset("status:" + serverId, "whitelist", String.valueOf(whitelist));
            jedis.hset("status:" + serverId, "heartbeat", String.valueOf(System.currentTimeMillis()));
            jedis.hset("status:" + serverId, "players", String.valueOf(players));
            jedis.hset("status:" + serverId, "maxPlayers", String.valueOf(maxPlayers));
//            jedis.hset("status:" + serverId, "whitelistedPlayers", String.valueOf(whitelistedPlayers));
//            jedis.hset("status:" + serverId, "onlinePlayers", String.valueOf(onlinePlayers));
        }
    }

}