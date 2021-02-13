package com.minexd.zoot.tags;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.ChatColor;

/**
 * Created by vape on 5/30/2020 at 11:46 AM.
 */
@Getter
@AllArgsConstructor
public class PlayerTag {

    private final String id;
    @Setter private String displayName, tag, description;
    @Setter private boolean suffix;

    public String format(String playerName) {
        suffix = true;
        return ChatColor.translateAlternateColorCodes('&', (suffix ? "" : (tag + " ")) + playerName + (suffix ? (" " + tag) : ""));
    }

    public static PlayerTag fromDocument(Document document) {
        return new PlayerTag(
                document.getString("tagId"),
                document.getString("display"),
                document.getString("tag"),
                document.getString("description"),
                document.getBoolean("suffix"));
    }

    public static final class DefaultTag extends PlayerTag {
        public DefaultTag() {
            super("", "", "", "", false);
        }

        @Override
        public String format(String playerName) {
            return playerName;
        }
    }
}