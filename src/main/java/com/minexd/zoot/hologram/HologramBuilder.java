package com.minexd.zoot.hologram;

import org.bukkit.Location;

import java.util.*;

public class HologramBuilder {

    private final Collection<UUID> viewers;
    private Location location;
    protected List<String> lines = new ArrayList<>();

    protected HologramBuilder(Collection<UUID> viewers) {
        this.viewers = viewers;
    }

    public HologramBuilder addLines(Iterable<String> lines) {
        for (String line : lines) {
            this.lines.add(line);
        }
        return this;
    }

    public HologramBuilder addLines(String ... lines) {
        this.lines.addAll(Arrays.asList(lines));
        return this;
    }

    public HologramBuilder at(Location location) {
        this.location = location;
        return this;
    }

    public UpdatingHologramBuilder updates() {
        return new UpdatingHologramBuilder(this);
    }

    public Hologram build() {
        return new BaseHologram(this);
    }

    protected Collection<UUID> getViewers() {
        return this.viewers;
    }

    protected Location getLocation() {
        return this.location;
    }

    protected List<String> getLines() {
        return this.lines;
    }
}

