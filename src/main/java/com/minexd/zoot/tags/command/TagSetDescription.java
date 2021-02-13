package com.minexd.zoot.tags.command;

import com.minexd.zoot.Zoot;
import com.minexd.zoot.network.packet.PacketReloadTags;
import com.minexd.zoot.tags.PlayerTag;
import com.minexd.zoot.tags.TagManager;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.command.CommandSender;

public class TagSetDescription {

    @Command(names = {"tag setdes", "tag setdescription"}, permission = "static.settag")
    public static void content(CommandSender sender, @Param(name = "id") String id, @Param(name = "tag", wildcard = true) String description) {
        PlayerTag playerTag = TagManager.getById(id);
        playerTag.setDescription(description);
        TagManager.saveTag(playerTag);

        sender.sendMessage(CC.GREEN + "Description updated to \"" + description + "\".");
        Zoot.get().getPidgin().sendPacket(new PacketReloadTags());
    }
}
