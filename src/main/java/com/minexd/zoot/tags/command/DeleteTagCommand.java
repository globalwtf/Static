package com.minexd.zoot.tags.command;

import com.minexd.zoot.Zoot;
import com.minexd.zoot.network.packet.PacketReloadTags;
import com.minexd.zoot.tags.TagManager;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.command.CommandSender;

public class DeleteTagCommand {

    @Command(names = "tag delete", permission = "static.deletetag")
    public static void delete(CommandSender sender, @Param(name = "id") String id) {
        if (!TagManager.doesTagExist(id)) {
            sender.sendMessage(CC.RED + "A tag does not exist with this ID.");
            return;
        }

        TagManager.deleteTag(TagManager.getById(id));

        sender.sendMessage(CC.GREEN + "Tag deleted.");
        Zoot.get().getPidgin().sendPacket(new PacketReloadTags());
    }

}