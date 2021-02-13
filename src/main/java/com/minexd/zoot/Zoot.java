package com.minexd.zoot;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.minexd.pidgin.Pidgin;
import com.minexd.zoot.cache.RedisCache;
import com.minexd.zoot.chat.Chat;
import com.minexd.zoot.chat.ChatListener;
import com.minexd.zoot.config.ConfigValidation;
import com.minexd.zoot.essentials.Essentials;
import com.minexd.zoot.hologram.HologramListener;
import com.minexd.zoot.hologram.HologramManager;
import com.minexd.zoot.network.NetworkPacketListener;
import com.minexd.zoot.network.listener.PacketListeners;
import com.minexd.zoot.network.packet.*;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.profile.ProfileListener;
import com.minexd.zoot.profile.ProfileTypeAdapter;
import com.minexd.zoot.profile.TokenRunnable;
import com.minexd.zoot.profile.grant.GrantListener;
import com.minexd.zoot.profile.punishment.listener.FrozenPlayerListener;
import com.minexd.zoot.profile.punishment.listener.PunishmentListener;
import com.minexd.zoot.profile.punishment.listener.TwoFactorListener;
import com.minexd.zoot.rank.Rank;
import com.minexd.zoot.rank.RankTypeAdapter;
import com.minexd.zoot.runnables.FrozenRunnable;
import com.minexd.zoot.runnables.TwoFARunnable;
import com.minexd.zoot.status.StatusHandler;
import com.minexd.zoot.tags.TagManager;
import com.minexd.zoot.util.CC;
import com.minexd.zoot.util.adapter.ChatColorTypeAdapter;
import com.minexd.zoot.util.adapter.GameModeParameter;
import com.minexd.zoot.util.duration.Duration;
import com.minexd.zoot.util.duration.DurationTypeAdapter;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.qrakn.phoenix.lang.file.type.BasicConfigurationFile;
import lombok.Getter;
import net.frozenorb.qlib.command.FrozenCommandHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class Zoot extends JavaPlugin {

    public static final Gson GSON = new Gson();
    public static final Type LIST_STRING_TYPE = new TypeToken<List<String>>() {}.getType();

    private static Zoot zoot;

    @Getter
    private BasicConfigurationFile mainConfig;

    @Getter
    private Pidgin pidgin;

    @Getter
    private MongoDatabase mongoDatabase;
    @Getter
    private JedisPool jedisPool;
    @Getter
    private RedisCache redisCache;

    @Getter private HologramManager hologramManager;

    @Getter
    private Essentials essentials;
    @Getter
    private Chat chat;

    /**
     * Broadcasts a message to all server operators.
     *
     * @param message The message.
     */
    public static void broadcastOps(String message) {
        Bukkit.getOnlinePlayers().stream().filter(Player::isOp).forEach(op -> op.sendMessage(CC.translate(message)));
    }

    public static Zoot get() {
        return zoot;
    }

    @Override
    public void onEnable() {
        zoot = this;

        mainConfig = new BasicConfigurationFile(this, "config");

        new ConfigValidation(mainConfig.getFile(), mainConfig.getConfiguration(), 4).check();

        loadMongo();
        loadRedis();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        redisCache = new RedisCache(this);
        essentials = new Essentials(this);
        chat = new Chat(this);



        pidgin = new Pidgin("zoot",
                mainConfig.getString("REDIS.HOST"),
                mainConfig.getInteger("REDIS.PORT"),
                mainConfig.getBoolean("REDIS.AUTHENTICATION.ENABLED") ?
                        mainConfig.getString("REDIS.AUTHENTICATION.PASSWORD") : null
        );

        Arrays.asList(
                PacketAddGrant.class,
                PacketBroadcastPunishment.class,
                PacketDeleteGrant.class,
                PacketDeleteRank.class,
                PacketRefreshRank.class,
                PacketStaffChat.class,
                PacketStaffJoinNetwork.class,
                PacketStaffLeaveNetwork.class,
                PacketStaffSwitchServer.class,
                PacketStaffReport.class,
                PacketStaffRequest.class,
                PacketClearGrants.class,
                PacketClearPunishments.class,
                PacketReloadTags.class,
                PacketLogWhileFrozen.class,
                PacketAuthPassword.class,
                PacketSuccessfulAuth.class,
                PacketSuccessfulDiscSync.class,
                PacketCreateDiscSyncCode.class,
                PacketAdminChat.class
        ).forEach(pidgin::registerPacket);

        pidgin.registerListener(new NetworkPacketListener(this));

        Arrays.asList(
                new ProfileListener(),
                new ChatListener(),
                new FrozenPlayerListener(),
                new TwoFactorListener(),
                new PacketListeners(),
                new GrantListener(),
                new PunishmentListener()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        if (Bukkit.getPluginManager().getPlugin("CommonLibs") != null || Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            Bukkit.getPluginManager().registerEvents(new HologramListener(), this);
        }

        this.hologramManager = new HologramManager();

        Rank.init();

        new StatusHandler();
        TagManager.reload();
        new FrozenRunnable().runTaskTimer(this, 0, 120);
        new TwoFARunnable().runTaskTimer(this, 0 , 120);

        FrozenCommandHandler.registerAll(this);
        FrozenCommandHandler.registerParameterType(Rank.class, new RankTypeAdapter());
        FrozenCommandHandler.registerParameterType(Profile.class, new ProfileTypeAdapter());
        FrozenCommandHandler.registerParameterType(Duration.class, new DurationTypeAdapter());
        FrozenCommandHandler.registerParameterType(GameMode.class, new GameModeParameter());
        FrozenCommandHandler.registerParameterType(ChatColor.class, new ChatColorTypeAdapter());

        new TokenRunnable().runTaskLaterAsynchronously(this, 20*60);
    }

    @Override
    public void onDisable() {
        try {
            jedisPool.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMongo() {
        if (mainConfig.getBoolean("MONGO.AUTHENTICATION.ENABLED")) {
            ServerAddress serverAddress = new ServerAddress(mainConfig.getString("MONGO.HOST"),
                    mainConfig.getInteger("MONGO.PORT"));

            MongoCredential credential = MongoCredential.createCredential(
                    mainConfig.getString("MONGO.AUTHENTICATION.USERNAME"), "admin",
                    mainConfig.getString("MONGO.AUTHENTICATION.PASSWORD").toCharArray());

            mongoDatabase = new MongoClient(serverAddress, credential, MongoClientOptions.builder().build())
                    .getDatabase("core");
        } else {
            mongoDatabase = new MongoClient(mainConfig.getString("MONGO.HOST"),
                    mainConfig.getInteger("MONGO.PORT")).getDatabase("core");
        }
    }

    private void loadRedis() {
        jedisPool = new JedisPool(mainConfig.getString("REDIS.HOST"), mainConfig.getInteger("REDIS.PORT"));

        if (mainConfig.getBoolean("REDIS.AUTHENTICATION.ENABLED")) {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.auth(mainConfig.getString("REDIS.AUTHENTICATION.PASSWORD"));
            }
        }
    }

}
