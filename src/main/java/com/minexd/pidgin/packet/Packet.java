package com.minexd.pidgin.packet;

import com.google.gson.JsonObject;

public interface Packet {
    int id();

    JsonObject serialize();

    void deserialize(final JsonObject object);
}
