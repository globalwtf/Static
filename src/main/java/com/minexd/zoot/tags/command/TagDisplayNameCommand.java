package com.minexd.zoot.tags.command;

import com.minexd.zoot.Zoot;
import com.minexd.zoot.network.packet.PacketReloadTags;
import com.minexd.zoot.tags.PlayerTag;
import com.minexd.zoot.tags.TagManager;
import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.command.CommandSender;

public class TagDisplayNameCommand {

    @Command(names = {"tag setdisplayname", "tag setdisplay"}, permission = "static.settag")
    public static void displayname(CommandSender sender, @Param(name = "id") String id, @Param(name = "displayName", wildcard = true) String displayName) {
        if (!TagManager.doesTagExist(id)) {
            sender.sendMessage(CC.RED + "A tag does not exist with this ID.");
            return;
        }

        PlayerTag playerTag = TagManager.getById(id);
        playerTag.setDisplayName(displayName);
        TagManager.saveTag(playerTag);

        sender.sendMessage(CC.GREEN + "Tag display name updated to \"" + displayName + "\".");
        Zoot.get().getPidgin().sendPacket(new PacketReloadTags());
    }

}