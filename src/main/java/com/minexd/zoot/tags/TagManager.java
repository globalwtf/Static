package com.minexd.zoot.tags;

import com.minexd.zoot.Zoot;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bson.Document;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by vape on 5/30/2020 at 11:48 AM.
 */
@UtilityClass
public class TagManager {
    @Getter private Map<String, PlayerTag> tagMap = new LinkedHashMap<>();
    private final MongoCollection<Document> COLLECTION = Zoot.get().getMongoDatabase().getCollection("tags");

    public void reload() {
        tagMap.clear();

        FindIterable<Document> documents = COLLECTION.find();
        for (Document document : documents) {
            register(PlayerTag.fromDocument(document));
        }
    }

    public void deleteTag(PlayerTag tag) {
        unregister(tag);
        COLLECTION.deleteOne(Filters.eq("tagId", tag.getId()));
    }

    public void saveTag(PlayerTag tag) {
        register(tag);

        Document doc = new Document()
                .append("tagId", tag.getId())
                .append("display", tag.getDisplayName())
                .append("description", tag.getDescription())
                .append("tag", tag.getTag())
                .append("suffix", tag.isSuffix());

        COLLECTION.replaceOne(Filters.eq("tagId", tag.getId()), doc, new UpdateOptions().upsert(true));
    }

    public void register(PlayerTag tag) {
        tagMap.put(tag.getId(), tag);
    }

    public void unregister(PlayerTag tag) {
        tagMap.remove(tag.getId());
    }

    public void unregister(String id) {
        tagMap.remove(id);
    }

    public PlayerTag getById(String id) {
        if (id == null) return null;
        PlayerTag tag = tagMap.get(id);
        if (tag != null) return tag;

        for (Map.Entry<String, PlayerTag> tagEntry : tagMap.entrySet()) {
            if(tagEntry.getKey().replace(" ", "_").equalsIgnoreCase(id)) return tagEntry.getValue();
        }

        return null;
    }

    public boolean doesTagExist(String id) {
        return getById(id) != null;
    }

}