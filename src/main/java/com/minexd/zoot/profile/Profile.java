package com.minexd.zoot.profile;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.minexd.zoot.Zoot;
import com.minexd.zoot.ZootAPI;
import com.minexd.zoot.profile.conversation.ProfileConversations;
import com.minexd.zoot.profile.grant.Grant;
import com.minexd.zoot.profile.grant.event.GrantAppliedEvent;
import com.minexd.zoot.profile.grant.event.GrantExpireEvent;
import com.minexd.zoot.profile.option.ProfileOptions;
import com.minexd.zoot.profile.punishment.AuthCode;
import com.minexd.zoot.profile.punishment.Punishment;
import com.minexd.zoot.profile.punishment.PunishmentType;
import com.minexd.zoot.profile.staff.ProfileStaffOptions;
import com.minexd.zoot.rank.Rank;
import com.minexd.zoot.tags.PlayerTag;
import com.minexd.zoot.tags.TagManager;
import com.minexd.zoot.util.Cooldown;
import com.minexd.zoot.util.json.JsonChain;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.com.google.common.hash.Hashing;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;

public class Profile {

    @Getter
    private static Map<UUID, Profile> profiles = new HashMap<>();
    private static MongoCollection<Document> collection = Zoot.get().getMongoDatabase().getCollection("profiles");

    @Getter
    private static List<AuthCode> authCodes = new ArrayList<>();

    @Getter private final UUID uuid;
    @Getter @Setter private String name;

    @Getter private final List<UUID> knownAlts;
    @Getter private final ProfileOptions options;
    @Getter private final ProfileStaffOptions staffOptions;
    @Getter private final ProfileConversations conversations;
    @Getter private final List<Grant> grants;
    @Getter private final List<Punishment> punishments;

    @Getter private final List<String> ownedTags;
    @Getter @Setter private String activeTagId;

    @Getter @Setter private List<String> permissions;
    @Getter @Setter private Long firstSeen, lastSeen;
    @Getter @Setter private List<UUID> sibling;
    @Getter @Setter private String currentAddress;
    @Getter private List<String> ipAddresses;
    @Getter @Setter private List<String> authedips;
    @Getter private Grant activeGrant;
    @Getter @Setter private boolean loaded;
    @Getter @Setter private Cooldown chatCooldown, requestCooldown;

    @Getter @Setter private long lastAuthenticated;

    @Getter @Setter private String sync;

    @Getter @Setter private boolean isBotChecked = false;

    @Setter @Getter private Material botCheckMaterial;

    @Getter @Setter
    private String discordID;

    @Getter @Setter private int tokens;

    @Getter @Setter private long timeStampOfLastLoginOrTokenGive;

    public Profile(String username, UUID uuid) {
        this.uuid = uuid;
        this.name = username;
        this.ownedTags = new ArrayList<>();
        this.grants = new ArrayList<>();
        this.punishments = new ArrayList<>();
        this.ipAddresses = new ArrayList<>();
        this.knownAlts = new ArrayList<>();
        this.options = new ProfileOptions();
        this.staffOptions = new ProfileStaffOptions();
        this.conversations = new ProfileConversations(this);
        this.chatCooldown = new Cooldown(0);
        this.requestCooldown = new Cooldown(0);
        this.sibling = new ArrayList<>();
        this.permissions = new ArrayList<>();
        this.discordID = null;
        this.lastAuthenticated = 0;
        this.sync = null;
        this.authedips = new ArrayList<>();
        this.tokens = 0;
        timeStampOfLastLoginOrTokenGive = System.currentTimeMillis();
        load();

    }

    public static Profile getByUuid(UUID uuid) {
        if (profiles.containsKey(uuid)) {
            return profiles.get(uuid);
        }

        return new Profile(null, uuid);
    }


    public void addTokens(int amount) {
        this.tokens += amount;
    }
    public static Profile getByUsername(String username) {
        Player player = Bukkit.getPlayer(username);

        if (player != null) {
            return profiles.get(player.getUniqueId());
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(username);

        if (offlinePlayer.hasPlayedBefore()) {
            if (profiles.containsKey(offlinePlayer.getUniqueId())) {
                return profiles.get(offlinePlayer.getUniqueId());
            }

            return new Profile(offlinePlayer.getName(), offlinePlayer.getUniqueId());
        }

        UUID uuid = Zoot.get().getRedisCache().getUuid(username);

        if (uuid != null) {
            if (profiles.containsKey(uuid)) {
                return profiles.get(uuid);
            }

            return new Profile(username, uuid);
        }

        return null;
    }

    public static List<Profile> getByIpAddress(String ipAddress) {
        List<Profile> profiles = new ArrayList<>();
        try {
            for (Document document : collection.find()) {
                List<String> ipAddresses = Zoot.GSON.fromJson(document.getString("ipAddresses"), Zoot.LIST_STRING_TYPE);
                for (String ipadd: ipAddresses)
                    if (ipadd.equals(ipAddress)) {
                        Profile profile = new Profile(document.getString("name"),
                                UUID.fromString(document.getString("uuid")));
                        profiles.add(profile);
                    }
                }
        } catch (Exception e) {
           for (StackTraceElement ele : e.getStackTrace())
               System.out.println(ele.toString());
        }
        //Zoot.get().getLogger().info("Found: " + profiles.size() + " alts for ip:" + ipAddress);
        return profiles;
    }


    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    public String getColoredUsername() {
        return activeGrant.getRank().getColor() + name;
    }

    public Punishment getActivePunishmentByType(PunishmentType type) {
        for (Punishment punishment : punishments) {
            if (punishment.getType() == type && !punishment.isRemoved() && !punishment.hasExpired()) {
                return punishment;
            }
        }
        return null;
    }

    public int getPunishmentCountByType(PunishmentType type) {
        int i = 0;

        for (Punishment punishment : punishments) {
            if (punishment.getType() == type) i++;
        }

        return i;
    }

    public Rank getActiveRank() {
        return activeGrant.getRank();
    }

    /**
     * Finds and applies the next best grant.
     * The next chosen grant is determined by comparing descending rank weights.
     */
    public void activateNextGrant() {
        List<Grant> grants = new ArrayList<>(this.grants);

        grants.sort(Comparator.comparingInt(grant -> grant.getRank().getWeight()));
        Collections.reverse(grants);

        for (Grant grant : grants) {
            if (!grant.isRemoved() && !grant.hasExpired()) {
                if (!grant.equals(activeGrant)) {
                    activeGrant = grant;
                    setupBukkitPlayer(getPlayer());
                    return;
                }
            }
        }
    }

    public void giveTag(PlayerTag playerTag) {
        if (doesOwnTag(playerTag)) return;
        getPermissions().add("zoot.tag." + playerTag.getId().toLowerCase());
    }

    public void giveAllTags() {
        getPermissions().add("zoot.tag.all");
    }

    public boolean doesOwnTag(PlayerTag tag) {
        return getPermissions().contains("zoot.tag." + tag.getId().toLowerCase());
    }

    public boolean isTagActive(PlayerTag tag) {
        return activeTagId != null && activeTagId.equals(tag.getId());
    }

    public PlayerTag getActiveTag(boolean defaultIfNone) {
        PlayerTag active = TagManager.getById(activeTagId);

        if (active == null && defaultIfNone)
            active = new PlayerTag.DefaultTag();

        return active;
    }

    public void reloadTags() {
        ownedTags.removeIf(id -> TagManager.getById(id) == null);
        if (activeTagId != null && TagManager.getById(activeTagId) == null)
            activeTagId = null;
    }

    /**
     * Checks for and updates any grants that have pending changes.
     */
    public void checkGrants() {
        Player player = getPlayer();

        // Update grants that are expired and not removed yet
        for (Grant grant : grants) {
            if (!grant.isRemoved() && grant.hasExpired()) {
                grant.setRemovedAt(System.currentTimeMillis());
                grant.setRemovedReason("Grant expired");
                grant.setRemoved(true);

                if (player != null) {
                    new GrantExpireEvent(player, grant).call();
                }
            }
        }

        // Active next available grant if active grant is now removed
        if (activeGrant != null && activeGrant.isRemoved()) {
            activateNextGrant();
        }

        // Generate a default grant if there is no active grant
        if (activeGrant == null) {
            Grant defaultGrant = new Grant(UUID.randomUUID(), Rank.getDefaultRank(), null,
                    System.currentTimeMillis(), "Default", Integer.MAX_VALUE);

            grants.add(defaultGrant);
            activeGrant = defaultGrant;

            if (player != null) {
                setupBukkitPlayer(getPlayer());
                new GrantAppliedEvent(player, defaultGrant).call();
            }
        }
    }
    //hash the IP with the format ipUUID
    public static String hashIP(String ip, UUID playerUUID) {
        return Hashing.sha256().hashString(ip + playerUUID, StandardCharsets.UTF_8).toString();
    }

    public void setupBukkitPlayer(Player player) {
        if (player == null) {
            return;
        }

        // Clear any permissions set for this player by this plugin
        for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
            if (attachmentInfo.getAttachment() == null || attachmentInfo.getAttachment().getPlugin() == null ||
                    !attachmentInfo.getAttachment().getPlugin().equals(Zoot.get())) {
                continue;
            }

            attachmentInfo.getAttachment().getPermissions().forEach((permission, value) -> {
                attachmentInfo.getAttachment().unsetPermission(permission);
            });
        }

        PermissionAttachment attachment = player.addAttachment(Zoot.get());

        for (String perm : permissions) {
            attachment.setPermission(perm, true);
        }

        for (String permission : activeGrant.getRank().getAllPermissions()) {
            attachment.setPermission(permission, true);
        }

        player.recalculatePermissions();

        String displayName = activeGrant.getRank().getPrefix() + player.getName();
        String coloredName = ZootAPI.getColoredName(player);

        if (coloredName.length() > 16) {
            coloredName = coloredName.substring(0, 15);
        }

        player.setDisplayName(displayName + ChatColor.RESET);
        player.setMetadata("ColorName", new FixedMetadataValue(Zoot.get(), coloredName));
        if (Zoot.get().getMainConfig().getBoolean("SETTINGS.UPDATE_PLAYER_LIST_NAME")) {
            player.setPlayerListName(coloredName);
        }
    }

    public void load() {
        Document document = collection.find(Filters.eq("uuid", uuid.toString())).first();

        if (document != null) {
            if (name == null) {
                name = document.getString("name");
            }

            if (document.containsKey("tags")) {
                for (Object tagEntry : document.get("tags", ArrayList.class)) {
                    try {
                        JsonElement element = new JsonParser().parse((String) tagEntry);
                        JsonObject obj = element.getAsJsonObject();

                        String id = obj.get("id").getAsString();
                        boolean active = obj.get("active").getAsBoolean();

                        ownedTags.add(id);
                        if (active)
                            activeTagId = id;
                    } catch (Exception ex) {}
                }
            }
            if (document.containsKey("discord_id"))
                discordID = document.getString("discord_id");

            if (document.containsKey("sync"))
                sync = document.getString("sync");

            if (document.containsKey("lastAuthenticated"))
                lastAuthenticated = document.getLong("lastAuthenticated");
            else
                discordID = null;

            if (document.containsKey("permissions")) {
                permissions = Zoot.GSON.fromJson(document.getString("permissions"), Zoot.LIST_STRING_TYPE);
            }

            firstSeen = document.getLong("firstSeen");
            lastSeen = document.getLong("lastSeen");
            currentAddress = document.getString("currentAddress");
            ipAddresses = Zoot.GSON.fromJson(document.getString("ipAddresses"), Zoot.LIST_STRING_TYPE);

            if (document.containsKey("tokens"))
                tokens = document.getInteger("tokens");

            //knownAlts = Zoot.GSON.fromJson(document.getString("alts"), Zoot.LIST_STRING_TYPE);
            if (Zoot.GSON.fromJson(document.getString("authediplist"), Zoot.LIST_STRING_TYPE) != null)
                authedips = Zoot.GSON.fromJson(document.getString("authediplist"), Zoot.LIST_STRING_TYPE);
            if (document.containsKey("botChecked"))
                isBotChecked = document.getBoolean("botChecked");
            Document optionsDocument = (Document) document.get("options");
            options.setPublicChatEnabled(optionsDocument.getBoolean("publicChatEnabled"));
            options.setReceivingNewConversations(optionsDocument.getBoolean("receivingNewConversations"));
            options.setPlayingMessageSounds(optionsDocument.getBoolean("playingMessageSounds"));

            JsonArray grantList = new JsonParser().parse(document.getString("grants")).getAsJsonArray();

            for (JsonElement grantData : grantList) {
                // Transform into a Grant object
                Grant grant = Grant.DESERIALIZER.deserialize(grantData.getAsJsonObject());

                if (grant != null) {
                    this.grants.add(grant);
                }
            }

            JsonArray punishmentList = new JsonParser().parse(document.getString("punishments")).getAsJsonArray();

            for (JsonElement punishmentData : punishmentList) {
                // Transform into a Grant object
                Punishment punishment = Punishment.DESERIALIZER.deserialize(punishmentData.getAsJsonObject());

                if (punishment != null) {
                    this.punishments.add(punishment);
                }
            }
        }

        reloadTags();

        // Update active grants
        activateNextGrant();
        checkGrants();

        // Set loaded to true
        loaded = true;
    }

    public void save() {
        Document document = new Document();
        document.put("name", name);
        document.put("uuid", uuid.toString());
        document.put("firstSeen", firstSeen);
        document.put("lastSeen", lastSeen);
        document.put("currentAddress", currentAddress);
        document.put("ipAddresses", Zoot.GSON.toJson(ipAddresses, Zoot.LIST_STRING_TYPE));
        if (authedips != null)
            document.put("authediplist", Zoot.GSON.toJson(authedips, Zoot.LIST_STRING_TYPE));

        document.put("permissions", Zoot.GSON.toJson(permissions, Zoot.LIST_STRING_TYPE));
        //document.put("alts", Zoot.GSON.toJson(knownAlts, Zoot.LIST_STRING_TYPE));
        document.put("botChecked", isBotChecked);

        if (tokens> 0)
            document.put("tokens", tokens);
        List<String> tagStrings = new ArrayList<>();
        for (String tagId : ownedTags) {
            boolean active = tagId.equals(activeTagId);

            tagStrings.add(new JsonChain()
                    .addProperty("active", active)
                    .addProperty("id", tagId)
                    .get().toString());
        }
        document.put("tags", tagStrings);

        if (discordID != null)
            document.put("discord_id", discordID);

        document.put("sync", sync);

        if (lastAuthenticated != 0)
            document.put("lastAuthenticated", lastAuthenticated);

        Document optionsDocument = new Document();
        optionsDocument.put("publicChatEnabled", options.isPublicChatEnabled());
        optionsDocument.put("receivingNewConversations", options.isReceivingNewConversations());
        optionsDocument.put("playingMessageSounds", options.isPlayingMessageSounds());
        document.put("options", optionsDocument);

        JsonArray grantList = new JsonArray();

        for (Grant grant : this.grants) {
            grantList.add(Grant.SERIALIZER.serialize(grant));
        }

        document.put("grants", grantList.toString());

        JsonArray punishmentList = new JsonArray();

        for (Punishment punishment : this.punishments) {
            punishmentList.add(Punishment.SERIALIZER.serialize(punishment));
        }

        document.put("punishments", punishmentList.toString());
        document.put("permissions", getPermissions().toString());

        collection.replaceOne(Filters.eq("uuid", uuid.toString()), document, new ReplaceOptions().upsert(true));
    }

}
