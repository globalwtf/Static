package com.minexd.zoot.essentials.command;

import com.minexd.zoot.Zoot;
import com.minexd.zoot.hologram.Hologram;
import com.minexd.zoot.hologram.Holograms;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.entity.*;
import org.bukkit.*;
import mkremins.fanciful.*;

import java.util.*;
import java.util.stream.Collectors;

public class HologramCommands
{
    @Command(names = { "hologram create", "holo create" }, permission = "static.holograms")
    public static void hologram_create(Player sender, @Param(name = "text", wildcard = true) String text) {
        Hologram hologram = Holograms.newHologram().at(sender.getEyeLocation()).addLines(new String[] { text }).build();
        hologram.send();
        int id = Zoot.get().getHologramManager().register(hologram);
        sender.sendMessage(ChatColor.GREEN + "Created hologram with the id " + id);
    }

    @Command(names = { "hologram addline", "holo addline" }, permission = "static.holograms")
    public static void hologram_addline(Player sender, @Param(name = "id") int id, @Param(name = "text", wildcard = true) String text) {
        Hologram hologram = Zoot.get().getHologramManager().getHolograms().get(id);
        if (hologram == null) {
            sender.sendMessage(ChatColor.RED + "Hologram not found.");
            return;
        }
        hologram.addLines(new String[] { text });
        Zoot.get().getHologramManager().save();
    }

    @Command(names = { "hologram removeline", "holo removeline" }, permission = "static.holograms")
    public static void hologram_removeline(Player sender, @Param(name = "id") int id, @Param(name = "lineNumber") int line) {
        Hologram hologram = Zoot.get().getHologramManager().getHolograms().get(id);
        if (--line < 0) {
            sender.sendMessage(ChatColor.RED + "Invalid Position");
            return;
        }
        if (hologram == null) {
            sender.sendMessage(ChatColor.RED + "Hologram not found.");
            return;
        }
        List<String> lines = new ArrayList<>(hologram.getLines());
        try {
            lines.remove(line);
            hologram.setLines(lines);
            Zoot.get().getHologramManager().save();
            sender.sendMessage(ChatColor.GREEN + "Success");
        }
        catch (IndexOutOfBoundsException e) {
            sender.sendMessage(ChatColor.RED + "Invalid Position");
        }
    }

    @Command(names = { "hologram insertbefore", "holo insertbefore" }, permission = "static.holograms")
    public static void hologram_insertbefore(Player sender, @Param(name = "id") int id, @Param(name = "beforeLineNumber") int line, @Param(name = "text", wildcard = true) String text) {
        if (--line < 0) {
            sender.sendMessage(ChatColor.RED + "Invalid Position");
            return;
        }
        Hologram hologram = Zoot.get().getHologramManager().getHolograms().get(id);
        if (hologram == null) {
            sender.sendMessage(ChatColor.RED + "Hologram not found.");
            return;
        }
        List<String> lines = new ArrayList<String>(hologram.getLines());
        try {
            lines.add(line, text);
            hologram.setLines(lines);
            Zoot.get().getHologramManager().save();
            sender.sendMessage(ChatColor.GREEN + "Success");
        }
        catch (IndexOutOfBoundsException e) {
            sender.sendMessage(ChatColor.RED + "Invalid Position");
        }
    }

    @Command(names = { "hologram insertafter", "holo insertafter" }, permission = "static.holograms")
    public static void hologram_insertafter(Player sender, @Param(name = "id") int id, @Param(name = "afterLineNumber") int line, @Param(name = "text", wildcard = true) String text) {
        if (--line < 0) {
            sender.sendMessage(ChatColor.RED + "Invalid Position");
            return;
        }
        Hologram hologram = Zoot.get().getHologramManager().getHolograms().get(id);
        if (hologram == null) {
            sender.sendMessage(ChatColor.RED + "Hologram not found.");
            return;
        }
        List<String> lines = new ArrayList<String>(hologram.getLines());
        try {
            lines.add(line + 1, text);
            hologram.setLines(lines);
            Zoot.get().getHologramManager().save();
            sender.sendMessage(ChatColor.GREEN + "Success");
        }
        catch (IndexOutOfBoundsException e) {
            sender.sendMessage(ChatColor.RED + "Invalid Position");
        }
    }

    @Command(names = { "hologram edit", "holo edit" }, permission = "static.holograms")
    public static void hologram_edit(Player sender, @Param(name = "id") int id, @Param(name = "lineToEdit") int line, @Param(name = "newText", wildcard = true) String newText) {
        if (--line < 0) {
            sender.sendMessage(ChatColor.RED + "Invalid Position");
            return;
        }
        Hologram hologram = Zoot.get().getHologramManager().getHolograms().get(id);
        if (hologram == null) {
            sender.sendMessage(ChatColor.RED + "Hologram not found.");
            return;
        }
        hologram.setLine(line, newText);
        Zoot.get().getHologramManager().save();
        sender.sendMessage(ChatColor.GREEN + "Success");
    }

    @Command(names = { "hologram list", "holo list" }, permission = "static.holograms")
    public static void hologram_list(Player sender) {
        if (Zoot.get().getHologramManager().getHolograms().size() == 0) {
            sender.sendMessage(ChatColor.RED + "There are no holograms created.");
            return;
        }
        for (Map.Entry<Integer, Hologram> entry : Zoot.get().getHologramManager().getHolograms().entrySet()) {

            List<String> tooltip = new ArrayList<>(Arrays.asList(ChatColor.GREEN + "Location: " +
                    String.format("[%.1f, %.1f, %.1f]", entry.getValue().getLocation().getX(), entry.getValue().getLocation().getY(), entry.getValue().getLocation().getZ())
            , ChatColor.YELLOW + "Click to teleport"));

            int i = 0;
            for (String line : entry.getValue().getLines()) {
                ++i;
                line = ChatColor.translateAlternateColorCodes('&', line);
                tooltip.add(ChatColor.GRAY.toString() + i + ". " + ChatColor.RESET + line);
            }
            FancyMessage message = new FancyMessage(ChatColor.RED + String.valueOf(entry.getKey())).tooltip(tooltip).command("/tppos " + String.format("%.1f %.1f %.1f", entry.getValue().getLocation().getX(), entry.getValue().getLocation().getY(), entry.getValue().getLocation().getZ()));
            message.send(sender);
        }
    }

    @Command(names = { "hologram movehere", "holo tphere" }, permission = "static.holograms")
    public static void hologram_movehere(Player sender, @Param(name = "id") int id) {
        Hologram hologram = Zoot.get().getHologramManager().getHolograms().get(id);
        if (hologram == null) {
            sender.sendMessage(ChatColor.RED + "Hologram not found.");
            return;
        }
        Zoot.get().getHologramManager().move(id, sender.getEyeLocation());
    }

    @Command(names = { "hologram delete", "holo delete" }, permission = "static.holograms")
    public static void hologram_delete(Player sender, @Param(name = "id") int id) {
        Hologram hologram = Zoot.get().getHologramManager().getHolograms().get(id);
        if (hologram == null) {
            sender.sendMessage(ChatColor.RED + "Hologram not found.");
            return;
        }
        Zoot.get().getHologramManager().getHolograms().remove(id);
        Zoot.get().getHologramManager().save();
        hologram.destroy();
        sender.sendMessage(ChatColor.GREEN + "Deleted hologram with id " + id);
    }
}
