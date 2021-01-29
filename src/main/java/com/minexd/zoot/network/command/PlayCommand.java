package com.minexd.zoot.network.command;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.minexd.zoot.Zoot;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@SuppressWarnings("UnstableApiUsage")
public class PlayCommand {

    @Command(names = "play", permission = "static.staff.play")
    public static void play(Player player, @Param(name = "server") String server) {
        player.sendMessage(ChatColor.GREEN + "Attempting to connect you to '" + server + "'...");
        connect(player, server);
    }

    private static void connect(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("Connect");
        out.writeUTF(server);

        player.sendPluginMessage(Zoot.get(), "BungeeCord", out.toByteArray());
    }

}