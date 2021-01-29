package com.minexd.zoot.profile;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class TokenRunnable extends BukkitRunnable {
    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            Profile profile = Profile.getByUuid(p.getUniqueId());
            if (System.currentTimeMillis() - profile.getTimeStampOfLastLoginOrTokenGive() > 600000) { //10 minutes
                profile.addTokens(3);
                profile.setTimeStampOfLastLoginOrTokenGive(System.currentTimeMillis());
            }
        });
    }
}
