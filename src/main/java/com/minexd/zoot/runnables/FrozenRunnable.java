package com.minexd.zoot.runnables;

import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class FrozenRunnable extends BukkitRunnable {

    private final List<String> frozenText = Arrays.asList(
            CC.GRAY + CC.SB_BAR,
            CC.translate("&7&l(&c&l⚠&7&l) &c&lYOU HAVE BEEN FROZEN &7&l(&c&l⚠&7&l)"),
            "",
            CC.translate("&cYou have been frozen by a staff member."),
            CC.translate("&cplease join ts.hcstatic.com"),
            "",
            CC.translate("&7&l(&c&l⚠&7&l) &c&lYOU HAVE BEEN FROZEN &7&l(&c&l⚠&7&l)"),
            CC.GRAY + CC.SB_BAR
        );

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            if(profile == null || !profile.isLoaded() || !profile.getOptions().isFrozen()) return;
            for (String text : frozenText) {
                player.sendMessage(text);
            }
        });
    }
}
