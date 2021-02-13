package com.minexd.zoot.runnables;

import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class TwoFARunnable extends BukkitRunnable {

    private final List<String> twoFAText = Arrays.asList(
            CC.GRAY + CC.SB_BAR,
            CC.translate("&cYou must login to verify your access to Coral."),
            CC.translate("&cPlease message \"authme\" to the 2FA bot in the staff discord, and type the code it gives you into chat."),
            CC.translate("&bExample: &a/login zMB0iHXcwP8qlYQRlOEYHrqoTQjnCI1crG50atZrGCXtNPIjPeQCn7yN0xKN"),
            CC.GRAY + CC.SB_BAR
    );

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            if (profile == null || !profile.isLoaded() || !profile.getOptions().isTwoFA())
                return;
            for (String text : twoFAText) {
                player.sendMessage(text);
            }
        });
    }
}
