package com.minexd.zoot.hologram.packets;

import com.minexd.zoot.hologram.HologramLine;
import org.bukkit.Location;

public interface HologramPacketProvider {

    HologramPacket getPacketsFor(Location var1, HologramLine var2);

}

