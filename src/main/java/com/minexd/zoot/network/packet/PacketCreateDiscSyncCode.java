package com.minexd.zoot.network.packet;

import com.google.gson.JsonObject;
import com.minexd.pidgin.packet.Packet;
import com.minexd.zoot.util.json.JsonChain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class PacketCreateDiscSyncCode implements Packet {

    @Getter
    private UUID playerUUID;
    @Getter
    private String password;

    @Override
    public int id() {
        return 700;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
                .addProperty("playerUuid", playerUUID.toString())
                .addProperty("password", password)
                .get();
    }

    @Override
    public void deserialize(JsonObject object) {
        playerUUID = UUID.fromString(object.get("playerUuid").getAsString());
        password = object.get("password").getAsString();
    }
}
