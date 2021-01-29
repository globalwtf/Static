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
public class PacketSuccessfulDiscSync implements Packet {

    @Getter
    private UUID playerUUID;
    @Getter
    private String discordID;
    @Getter
    private long authAt;


    @Override
    public int id() {
        return 701;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
                .addProperty("discord_id", discordID)
                .addProperty("auth_at", authAt)
                .addProperty("discord_id", discordID)
                .get();
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        playerUUID = UUID.fromString(jsonObject.get("playerUUID").getAsString());
        authAt = jsonObject.get("auth_at").getAsLong();
        discordID = jsonObject.get("discord_id").getAsString();
    }
}
