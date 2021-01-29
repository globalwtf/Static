package com.minexd.zoot.network.packet;

import com.google.gson.JsonObject;
import com.minexd.pidgin.packet.Packet;
import com.minexd.zoot.util.json.JsonChain;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by vape on 5/30/2020 at 12:02 PM.
 */
@Getter
public class PacketReloadTags implements Packet {

    public PacketReloadTags() {
    }

    @Override
    public int id() {
        return 10;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain().get();
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
    }

}