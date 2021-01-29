package com.minexd.zoot.network.packet;

import com.google.gson.JsonObject;
import com.minexd.pidgin.packet.Packet;
import com.minexd.zoot.util.json.JsonChain;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PacketAuthPassword implements Packet {

    private String discordID;
    private long authAt;
    private String password;

    public PacketAuthPassword() {

    }

    public PacketAuthPassword(String discordID, long authAt, String password) {
        this.discordID = discordID;
        this.authAt = authAt;
        this.password = password;
    }

    public int id() {
        return 699;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
                .addProperty("userid", discordID)
                .addProperty("authAt", authAt)
                .addProperty("password", password)
                .get();
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        discordID = jsonObject.get("userid").getAsString();
        authAt = jsonObject.get("time_sent").getAsLong();
        password = jsonObject.get("password").getAsString();
    }

}
