package com.minexd.pidgin.packet.listener;

import com.minexd.pidgin.packet.Packet;
import lombok.Getter;

import java.beans.ConstructorProperties;
import java.lang.reflect.Method;

@Getter
public class PacketListenerData {
    private Object instance;
    private Method method;
    private Class packetClass;

    @ConstructorProperties({"instance", "method", "packetClass"})
    public PacketListenerData(Object instance, Method method, Class packetClass) {
        this.instance = instance;
        this.method = method;
        this.packetClass = packetClass;
    }

    public boolean matches(Packet packet) {
        return this.packetClass == packet.getClass();
    }
}