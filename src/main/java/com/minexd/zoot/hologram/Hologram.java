package com.minexd.zoot.hologram;

import java.util.Collection;
import java.util.List;
import org.bukkit.Location;

public interface Hologram {

    void send();

    void destroy();

    void addLines(String... var1);

    void setLine(int var1, String var2);

    void setLines(Collection<String> var1);

    List<String> getLines();

    Location getLocation();
}

