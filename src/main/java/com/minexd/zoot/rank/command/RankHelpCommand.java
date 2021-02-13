package com.minexd.zoot.rank.command;

import com.minexd.zoot.util.CC;
import net.frozenorb.qlib.command.Command;
import org.bukkit.command.CommandSender;

public class RankHelpCommand {

    private static final String[][] HELP;

    static {
        HELP = new String[][]{
                new String[]{"ranks", "List all existing ranks"},
                new String[]{"rank create <name>", "Create a new rank"},
                new String[]{"rank delete <rank>", "Delete an existing rank"},
                new String[]{"rank info <rank>", "Show info about an existing rank"},
                new String[]{"rank setcolor <rank> <color>", "Set a rank's color"},
                new String[]{"rank setprefix <rank> <prefix>", "Set a rank's prefix"},
                new String[]{"rank setweight <rank> <weight>", "Set a rank's weight"},
                new String[]{"rank addperm <rank> <permission>", "Add a permission to a rank"},
                new String[]{"rank delperm <rank> <permission>", "Remove a permission from a rank"},
                new String[]{"rank inherit <parent> <child>", "Make a parent rank inherit a child rank"},
                new String[]{"rank uninherit <parent> <child>", "Make a parent rank uninherit a child rank"}
        };
    }

    @Command(names = {"rank", "rank help"}, permission = "static.admin.rank")
    public static void rank(CommandSender sender) {
        sender.sendMessage(CC.CHAT_BAR);
        sender.sendMessage(CC.GOLD + "Rank Help");

        for (String[] help : HELP) {
            sender.sendMessage(CC.BLUE + "/" + help[0] + CC.GRAY + " - " + CC.RESET + help[1]);
        }

        sender.sendMessage(CC.CHAT_BAR);
    }

}