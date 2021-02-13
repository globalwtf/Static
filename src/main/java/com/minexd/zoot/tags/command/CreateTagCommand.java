package com.minexd.zoot.tags.command;

import com.minexd.zoot.Zoot;
import com.minexd.zoot.network.packet.PacketReloadTags;
import com.minexd.zoot.tags.PlayerTag;
import com.minexd.zoot.tags.TagManager;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.command.CommandSender;

public class CreateTagCommand {

    @Command(names = "tag create", permission = "static.createtag")
    public static void create(CommandSender sender, @Param(name = "id") String id) {
        if (id.contains(" ")) {
            sender.sendMessage(CC.RED + "Invalid tag ID!");
            return;
        }

        if (TagManager.doesTagExist(id)) {
            sender.sendMessage(CC.RED + "A tag already exists with this ID.");
            return;
        }

        TagManager.saveTag(new PlayerTag(id, id, "", "Default Description", false));

        sender.sendMessage(CC.GREEN + "Tag created.");
        Zoot.get().getPidgin().sendPacket(new PacketReloadTags());
    }

}