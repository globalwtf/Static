package com.minexd.zoot.tags.command;

import com.minexd.zoot.Zoot;
import com.minexd.zoot.network.packet.PacketReloadTags;
import net.frozenorb.qlib.command.Command;
import org.bukkit.command.CommandSender;

public class ReloadTagsCommand {

    @Command(names = "reloadtags", permission = "static.reloadtags")
    public static void reload(CommandSender sender) {
        Zoot.get().getPidgin().sendPacket(new PacketReloadTags());
    }

}