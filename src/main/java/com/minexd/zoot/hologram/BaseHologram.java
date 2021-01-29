package com.minexd.zoot.hologram;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.minexd.zoot.Zoot;
import com.minexd.zoot.hologram.packets.HologramPacket;
import com.minexd.zoot.hologram.packets.HologramPacketProvider;
import com.minexd.zoot.hologram.packets.v1_7.Minecraft17HologramPacketProvider;
import com.minexd.zoot.hologram.packets.v1_8.Minecraft18HologramPacketProvider;

import mkremins.fanciful.shaded.gson.internal.Pair;
import net.frozenorb.qlib.tab.TabUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class BaseHologram implements Hologram {

    private final Collection<UUID> viewers;
    protected Location location;
    protected List<HologramLine> lastLines = new ArrayList<>();
    protected List<HologramLine> lines = new ArrayList<>();
    protected final Set<UUID> currentWatchers = new HashSet<>();
    protected static final double distance = 0.23;

    protected BaseHologram(HologramBuilder builder) {
        if (builder.getLocation() == null) {
            throw new IllegalArgumentException("Please provide a location for the hologram using HologramBuilder#at(Location)");
        }
        this.viewers = builder.getViewers();
        this.location = builder.getLocation();
        for (String line : builder.getLines()) {
            this.lines.add(new HologramLine(line));
        }
    }

    @Override
    public void send() {
        Collection<UUID> viewers = this.viewers;
        if (viewers == null) {
            viewers = ImmutableList.copyOf(Zoot.get().getServer().getOnlinePlayers()).stream().map(Entity::getUniqueId).collect(Collectors.toSet());
        }
        for (UUID uuid : viewers) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline()) continue;
            this.show(player);
        }
        HologramRegistry.getHolograms().add(this);
    }

    @Override
    public void destroy() {
        Collection<UUID> viewers = this.viewers;
        if (viewers == null) {
            viewers = ImmutableList.copyOf(Zoot.get().getServer().getOnlinePlayers()).stream().map(Entity::getUniqueId).collect(Collectors.toSet());
        }
        for (UUID uuid : viewers) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline()) continue;
            this.destroy0(player);
        }
        if (this.viewers != null) {
            this.viewers.clear();
        }
        HologramRegistry.getHolograms().remove(this);
    }

    @Override
    public void addLines(String ... lines) {
        for (String line : lines) {
            this.lines.add(new HologramLine(line));
        }
        this.update();
    }

    @Override
    public void setLine(int index, String line) {
        if (index > this.lines.size() - 1) {
            this.lines.add(new HologramLine(line));
        } else if (this.lines.get(index) != null) {
            this.lines.get(index).setText(line);
        } else {
            this.lines.set(index, new HologramLine(line));
        }
        this.update();
    }

    @Override
    public void setLines(Collection<String> lines) {
        Collection<UUID> viewers = this.viewers;
        if (viewers == null) {
            viewers = ImmutableList.copyOf(Zoot.get().getServer().getOnlinePlayers()).stream().map(Entity::getUniqueId).collect(Collectors.toSet());
        }
        for (UUID uuid : viewers) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline()) continue;
            this.destroy0(player);
        }
        this.lines.clear();
        for (String line : lines) {
            this.lines.add(new HologramLine(line));
        }
        this.update();
    }

    @Override
    public List<String> getLines() {
        ArrayList<String> lines = new ArrayList<>();
        for (HologramLine line : this.lines) {
            lines.add(line.getText());
        }
        return lines;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    protected List<HologramLine> rawLines() {
        return this.lines;
    }

    protected void show(Player player) {
        if (!player.getLocation().getWorld().equals(this.location.getWorld())) {
            return;
        }
        Location first = this.location.clone().add(0.0, (double)this.lines.size() * 0.23, 0.0);
        for (HologramLine line : this.lines) {
            this.showLine(player, first.clone(), line);
            first.subtract(0.0, 0.23, 0.0);
        }
        this.currentWatchers.add(player.getUniqueId());
    }

    protected Pair<Integer, Integer> showLine(Player player, Location loc, HologramLine line) {
        HologramPacketProvider packetProvider = this.getPacketProviderForPlayer(player);
        HologramPacket hologramPacket = packetProvider.getPacketsFor(loc, line);
        if (hologramPacket != null) {
            hologramPacket.sendToPlayer(player);
            return new Pair<>(hologramPacket.getEntityIds().get(0), hologramPacket.getEntityIds().get(1));
        }
        return null;
    }

    protected void destroy0(Player player) {
        ArrayList<Integer> ints = new ArrayList<>();
        for (HologramLine line : this.lines) {
            if (line.getHorseId() == -1337) {
                ints.add(line.getSkullId());
                continue;
            }
            ints.add(line.getSkullId());
            ints.add(line.getHorseId());
        }
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(this.convertIntegers(ints));
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
        this.currentWatchers.remove(player.getUniqueId());
    }

    protected int[] convertIntegers(List<Integer> integers) {
        int[] ret = new int[integers.size()];
        for (int i = 0; i < ret.length; ++i) {
            ret[i] = integers.get(i);
        }
        return ret;
    }

    public void update() {
        Collection<UUID> viewers = this.getViewers();
        if (viewers == null) {
            viewers = ImmutableList.copyOf(Zoot.get().getServer().getOnlinePlayers()).stream().map(Entity::getUniqueId).collect(Collectors.toSet());
        }
        for (UUID uuid : viewers) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline()) continue;
            this.update(player);
        }
        this.lastLines.addAll(this.lines);
    }

    public void update(Player player) {
        if (!player.getLocation().getWorld().equals(this.location.getWorld())) {
            return;
        }
        if (this.lastLines.size() != this.lines.size()) {
            this.destroy0(player);
            this.show(player);
            return;
        }
        for (int index = 0; index < this.rawLines().size(); ++index) {
            HologramLine line = this.rawLines().get(index);
            String text = ChatColor.translateAlternateColorCodes('&', line.getText());
            boolean is18 = TabUtils.is18(player);
            try {
                PacketContainer container = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
                container.getIntegers().write(0, (is18 ? line.getSkullId() : line.getHorseId()));
                WrappedDataWatcher wrappedDataWatcher = new WrappedDataWatcher();
                if (is18) {
                    wrappedDataWatcher.setObject(2, text);
                } else {
                    wrappedDataWatcher.setObject(10, text);
                }
                List<WrappedWatchableObject> watchableObjects = Arrays.asList(Iterators.toArray(wrappedDataWatcher.iterator(), WrappedWatchableObject.class));
                container.getWatchableCollectionModifier().write(0, watchableObjects);
                try {
                    ProtocolLibrary.getProtocolManager().sendServerPacket(player, container);
                }
                catch (Exception ignored) {}
            }
            catch (IndexOutOfBoundsException e) {
                this.destroy0(player);
                this.show(player);
            }
        }
    }

    private HologramPacketProvider getPacketProviderForPlayer(Player player) {
        return ((CraftPlayer)player).getHandle().playerConnection.networkManager.getVersion() > 5 ? new Minecraft18HologramPacketProvider() : new Minecraft17HologramPacketProvider();
    }

    protected Collection<UUID> getViewers() {
        return this.viewers;
    }
}

