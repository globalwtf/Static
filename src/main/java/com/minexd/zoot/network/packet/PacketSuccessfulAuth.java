package com.minexd.zoot.network.packet;

import com.google.gson.JsonObject;
import com.minexd.pidgin.packet.Packet;
import com.minexd.zoot.util.json.JsonChain;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class PacketSuccessfulAuth implements Packet {

    @Getter
    private UUID playerUUID;
    @Getter
    private String discordID;


    @Override
    public int id() {
        return 702;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
                .addProperty("playerUuid", playerUUID.toString())
                .addProperty("discord_id", discordID)
                .get();
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        playerUUID = UUID.fromString(jsonObject.get("playerUuid").getAsString());
        discordID = jsonObject.get("discord_id").getAsString();
    }
}
