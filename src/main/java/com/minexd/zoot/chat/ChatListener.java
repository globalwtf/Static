package com.minexd.zoot.chat;

import com.minexd.zoot.Locale;
import com.minexd.zoot.Zoot;
import com.minexd.zoot.chat.event.ChatAttemptEvent;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.util.CC;
import com.minexd.zoot.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.function.Predicate;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Profile profile = Profile.getProfiles().get(event.getPlayer().getUniqueId());
        if (profile == null) {
            event.setCancelled(true);
            return;
        }

        ChatAttempt chatAttempt = Zoot.get().getChat().attemptChatMessage(event.getPlayer(), event.getMessage());
        ChatAttemptEvent chatAttemptEvent = new ChatAttemptEvent(event.getPlayer(), chatAttempt, event.getMessage());

        Bukkit.getServer().getPluginManager().callEvent(chatAttemptEvent);

        if (!chatAttemptEvent.isCancelled()) {
            switch (chatAttempt.getResponse()) {
                case ALLOWED: {
                    event.setFormat(profile.getActiveTag(true).format(event.getPlayer().getDisplayName()) + CC.RESET + ": %2$s");
                }
                break;
                case MESSAGE_FILTERED: {
                    event.setCancelled(true);
                    chatAttempt.getFilterFlagged().punish(event.getPlayer());
                }
                break;
                case PLAYER_MUTED: {
                    event.setCancelled(true);

                    if (chatAttempt.getPunishment().isPermanent()) {
                        event.getPlayer().sendMessage(CC.RED + "You are muted for forever.");
                    } else {
                        event.getPlayer().sendMessage(CC.RED + "You are muted for " +
                                chatAttempt.getPunishment().getTimeRemaining() + ".");
                    }
                }
                break;
                case CHAT_MUTED: {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(CC.RED + "Public chat is currently muted.");
                }
                break;
                case CHAT_DELAYED: {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(Locale.CHAT_DELAYED.format(
                            TimeUtil.millisToSeconds((long) chatAttempt.getValue())) + " seconds");
                }
                break;
            }
        }

        if (chatAttempt.getResponse() == ChatAttempt.Response.ALLOWED) {
            event.getRecipients().removeIf(new Predicate<Player>() {
                @Override
                public boolean test(Player player) {
                    Profile profile = Profile.getProfiles().get(player.getUniqueId());
                    return profile != null && !profile.getOptions().isPublicChatEnabled();
                }
            });
        }
    }

}
