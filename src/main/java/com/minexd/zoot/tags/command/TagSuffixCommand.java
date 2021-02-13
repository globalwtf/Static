/* package com.minexd.zoot.tags.command;

import com.minexd.zoot.Zoot;
import com.minexd.zoot.network.packet.PacketReloadTags;
import com.minexd.zoot.tags.PlayerTag;
import com.minexd.zoot.tags.TagManager;
import com.minexd.zoot.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

Created by vape on 5/30/2020 at 12:30 PM.

@CommandMeta(label = "tag setsuffix", permission = "zoot.settagsuffix")
public class TagSuffixCommand {

    public void execute(CommandSender sender, String id, boolean suffix) {
        if (!TagManager.doesTagExist(id)) {
            sender.sendMessage(CC.RED + "A tag does not exist with this ID.");
            return;
        }

        PlayerTag playerTag = TagManager.getById(id);
        playerTag.setSuffix(suffix);
        TagManager.saveTag(playerTag);

        sender.sendMessage(CC.GREEN + "Tag is " + (suffix ? "now" : "no longer") + " a suffix.");
        Zoot.get().getPidgin().sendPacket(new PacketReloadTags());
    }

}
*/