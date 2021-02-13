package com.minexd.zoot.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Getter
@AllArgsConstructor
public class ZootServer {
    private String serverName;
    @Setter
    private boolean whitelisted;
    @Setter
    private long lastHeartbeat;
    @Setter
    private int players, maxPlayers;
//    @Getter
//    @Setter
//    private Collection<? extends Player> onlinePlayers;
//    @Getter
//    @Setter
//    private Collection<OfflinePlayer> whitelistedPlayers;


    public boolean isOnline() {
        return System.currentTimeMillis() - lastHeartbeat <= TimeUnit.SECONDS.toMillis(5L);
    }

    public IntStream getPlayersAsStream() {
        return IntStream.of(players);
    }
}