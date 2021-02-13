package com.minexd.pidgin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.minexd.pidgin.packet.Packet;
import com.minexd.pidgin.packet.handler.IncomingPacketHandler;
import com.minexd.pidgin.packet.handler.PacketExceptionHandler;
import com.minexd.pidgin.packet.listener.PacketListener;
import com.minexd.pidgin.packet.listener.PacketListenerData;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

public class Pidgin {
    private static JsonParser PARSER = new JsonParser();
    private final String channel;
    private JedisPool jedisPool;
    private JedisPubSub jedisPubSub;
    private List<PacketListenerData> packetListeners = new ArrayList<>();
    private Map<Integer, Class> idToType = new HashMap<>();
    private Map<Class, Integer> typeToId = new HashMap<>();

    public Pidgin(final String channel, final String host, final int port, final String password) {
        this.channel = channel;
        this.jedisPool = new JedisPool(host, port);
        if (password != null && !password.equals("")) {
            try (final Jedis jedis = this.jedisPool.getResource()) {
                jedis.auth(password);
            }
        }
        this.setupPubSub();
    }

    public void sendPacket(final Packet packet) {
        this.sendPacket(packet, null);
    }

    public void sendPacket(final Packet packet, final PacketExceptionHandler exceptionHandler) {
        try {
            final JsonObject object = packet.serialize();
            if (object == null) {
                throw new IllegalStateException("Packet cannot generate null serialized data");
            }
            try (final Jedis jedis = this.jedisPool.getResource()) {
                jedis.publish(this.channel, packet.id() + ";" + object.toString());
            }
        } catch (Exception e) {
            if (exceptionHandler != null) {
                exceptionHandler.onException(e);
            }
        }
    }

    public Packet buildPacket(final int id) {
        if (!this.idToType.containsKey(id)) {
            throw new IllegalStateException("A packet with that ID does not exist");
        }
        try {
            return ((Class<Packet>) this.idToType.get(Integer.valueOf(id))).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Could not create new instance of packet type");
        }
    }

    public void registerPacket(final Class clazz) {
        try {
            final int id = (int) clazz.getDeclaredMethod("id", new Class[0]).invoke(clazz.newInstance(), (Object[]) null);
            if (this.idToType.containsKey(id) || this.typeToId.containsKey(clazz)) {
                throw new IllegalStateException("A packet with that ID has already been registered");
            }
            this.idToType.put(id, clazz);
            this.typeToId.put(clazz, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerListener(final PacketListener packetListener) {
        for (final Method method : packetListener.getClass().getDeclaredMethods()) {
            if (method.getDeclaredAnnotation(IncomingPacketHandler.class) != null) {
                Class packetClass = null;
                if (method.getParameters().length > 0 && Packet.class.isAssignableFrom(method.getParameters()[0].getType())) {
                    packetClass = method.getParameters()[0].getType();
                }
                if (packetClass != null) {
                    this.packetListeners.add(new PacketListenerData(packetListener, method, packetClass));
                }
            }
        }
    }

    private void setupPubSub() {
        this.jedisPubSub = new JedisPubSub() {
            @Override
            public void onMessage(final String channel, final String message) {
                if (channel.equalsIgnoreCase(Pidgin.this.channel)) {
                    try {
                        final String[] args = message.split(";");
                        final Integer id = Integer.valueOf(args[0]);
                        final Packet packet = Pidgin.this.buildPacket(id);
                        if (packet != null) {
                            packet.deserialize(Pidgin.PARSER.parse(args[1]).getAsJsonObject());
                            for (final PacketListenerData data : Pidgin.this.packetListeners) {
                                if (data.matches(packet)) {
                                    data.getMethod().invoke(data.getInstance(), packet);
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("[Pidgin] Failed to handle message");
                        e.printStackTrace();
                    }
                }
            }
        };
        ForkJoinPool.commonPool().execute(() -> {
            try (Jedis jedis = this.jedisPool.getResource()) {
                jedis.subscribe(this.jedisPubSub, this.channel);
            }
        });
    }
}