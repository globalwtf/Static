package com.minexd.zoot.config.impl;

import com.minexd.zoot.config.ConfigConversion;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigConversion6 implements ConfigConversion {

    @Override
    public void convert(File file, FileConfiguration fileConfiguration) {
        fileConfiguration.set("CONFIG_VERSION", 6);

        fileConfiguration.set("PLAYER_TAG.NOT_OWNED", "&cYou can purchase this tag on our store: https://store.hcstatic.com");
        fileConfiguration.set("PLAYER_TAG.ALREADY_ENABLED", "&cYou already have this tag selected.");
        fileConfiguration.set("PLAYER_TAG.ENABLE_ERROR", "&cFailed to enable this tag.");
        fileConfiguration.set("PLAYER_TAG.ENABLED", "&aYou have activated the &b{2} &aTag!");
        fileConfiguration.set("PLAYER_TAG.NO_ACTIVE_TAG", "&cYou do not have a tag selected.");
        fileConfiguration.set("PLAYER_TAG.CLEARED", "&cYou have cleared your tag.");

        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
