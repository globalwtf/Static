package com.minexd.zoot.network.packet;

import com.google.gson.JsonObject;
import com.minexd.pidgin.packet.Packet;
import com.minexd.zoot.util.json.JsonChain;

public class PacketDiscordSync implements Packet {

    private String discordID;
    private String password;

    @Override
    public int id() {
        return 700;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
                .addProperty("userid", discordID)
                .addProperty("password", password)
                .get();
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        discordID = jsonObject.get("userid").getAsString();
        password = jsonObject.get("password").getAsString();
    }
}
