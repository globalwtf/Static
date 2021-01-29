package com.minexd.zoot.hologram;

import com.minexd.zoot.Zoot;
import lombok.Getter;
import net.frozenorb.qlib.qLib;
import net.minecraft.util.org.apache.commons.io.*;
import java.io.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.*;
import org.bukkit.*;

import java.beans.*;

public class HologramManager
{
    @Getter private Map<Integer, Hologram> holograms;

    public HologramManager() {
        this.holograms = new HashMap<>();
        File file = new File(Zoot.get().getDataFolder(), "holograms.json");
        if (!file.exists()) {
            return;
        }
        try {
            List<SerializedHologram> loaded = qLib.GSON.fromJson(FileUtils.readFileToString(file), new TypeToken<List<SerializedHologram>>() {}.getType());
            for (SerializedHologram serialized : loaded) {
                Hologram hologram = Holograms.newHologram().at(serialized.location).addLines(serialized.lines).build();
                this.holograms.put(serialized.id, hologram);
                hologram.send();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int register(Hologram hologram) {
        if (hologram instanceof UpdatingHologram) {
            throw new IllegalArgumentException("We can only serialize static holograms.");
        }
        int nextId = this.createId();
        this.holograms.put(nextId, hologram);
        this.save();
        return nextId;
    }

    public void save() {
        List<SerializedHologram> toSerialize = new ArrayList<>();
        for (Map.Entry<Integer, Hologram> entry : this.holograms.entrySet()) {
            toSerialize.add(new SerializedHologram(entry.getKey(), entry.getValue().getLocation(), entry.getValue().getLines()));
        }
        File file = new File(Zoot.get().getDataFolder(), "holograms.json");
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileUtils.write(file, qLib.GSON.toJson(toSerialize));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int createId() {
        int id;
        for (id = this.holograms.size() + 1; this.holograms.get(id) != null; ++id) {}
        return id;
    }

    public void move(int id, Location location) {
        Hologram hologram = this.getHolograms().get(id);
        List<String> lines = hologram.getLines();
        this.holograms.remove(id);
        hologram.destroy();
        this.save();
        Hologram newHologram = Holograms.newHologram().at(location).addLines(lines).build();
        newHologram.send();
        this.holograms.put(id, newHologram);
        this.save();
    }


    private class SerializedHologram
    {
        int id;
        Location location;
        List<String> lines;

        @ConstructorProperties({ "id", "location", "lines" })
        public SerializedHologram(int id, Location location, List<String> lines) {
            this.id = id;
            this.location = location;
            this.lines = lines;
        }
    }
}
