/*
 * Copyright (c) 2015 Demigods RPG
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.demigodsrpg.util.datasection;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.*;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ResultOfMethodCallIgnored")
public abstract class AbstractMongoRegistry<T extends Model> implements Registry<T> {
    protected final Cache<String, T> REGISTERED_DATA;
    protected final MongoCollection<Document> COLLECTION;
    protected final boolean UUID_KEYS;

    public AbstractMongoRegistry(MongoCollection<Document> collection, int expireMins, boolean uuidKeys) {
        COLLECTION = collection;
        if (expireMins > 0) {
            REGISTERED_DATA =
                    CacheBuilder.newBuilder().concurrencyLevel(4).expireAfterAccess(expireMins, TimeUnit.MINUTES)
                            .build();
        } else {
            REGISTERED_DATA = CacheBuilder.newBuilder().concurrencyLevel(4).build();
        }
        UUID_KEYS = uuidKeys;
    }

    public Optional<T> fromKey(String key) {
        if (!REGISTERED_DATA.asMap().containsKey(key)) {
            loadFromDb(key);
        }
        return Optional.ofNullable(REGISTERED_DATA.asMap().getOrDefault(key, null));
    }

    public T register(T value) {
        REGISTERED_DATA.put(value.getKey(), value);
        saveToDb(value.getKey());
        return value;
    }

    public T put(String key, T value) {
        REGISTERED_DATA.put(key, value);
        saveToDb(key);
        return value;
    }

    public void remove(String key) {
        REGISTERED_DATA.asMap().remove(key);
        COLLECTION.deleteOne(Filters.eq("key", key));
    }

    public void saveToDb(String key) {
        Optional<Document> loaded = documentFromDb(key);
        if (REGISTERED_DATA.asMap().containsKey(key)) {
            Model model = REGISTERED_DATA.asMap().get(key);
            if (loaded.isPresent()) {
                COLLECTION.deleteMany(Filters.eq("key", key));
            }
            Document document = mapToDocument(model.serialize());
            document.put("key", key);
            COLLECTION.insertOne(document);
        }
    }

    @SuppressWarnings("unchecked")
    public void loadFromDb(String key) {
        Document document = COLLECTION.find(Filters.eq("key", key)).first();
        if (document != null) {
            REGISTERED_DATA.put(key, fromDataSection(key, new MJsonSection(document)));
        }
    }

    public Optional<Document> documentFromDb(String key) {
        Document document = COLLECTION.find(Filters.eq("key", key)).first();
        if (document != null) {
            return Optional.of(document);
        }
        return Optional.empty();
    }

    @SuppressWarnings("ConstantConditions")
    public Map<String, T> loadAllFromDb() {
        MongoCursor<Document> cursor = COLLECTION.find().iterator();
        try {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                String key = document.getString("key");
                if (key != null) {
                    try {
                        if (UUID_KEYS) UUID.fromString(key);
                        REGISTERED_DATA.put(key, fromDataSection(key, new MJsonSection(document)));
                    } catch (Exception oops) {
                        oops.printStackTrace();
                    }
                }
            }
        } finally {
            cursor.close();
        }
        return REGISTERED_DATA.asMap();
    }

    public void purge() {
        REGISTERED_DATA.asMap().clear();
        COLLECTION.drop();
    }

    @Override
    public Map<String, T> getRegisteredData() {
        return REGISTERED_DATA.asMap();
    }

    // -- UTILITY METHODS -- //

    public static Document mapToDocument(Map<String, Object> map) {
        Document document = new Document();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof Map) {
                document.put(entry.getKey(), mapToDocument((Map) entry.getValue()));
            } else {
                document.put(entry.getKey(), entry.getValue());
            }
        }
        return document;
    }

    public static Map<String, Object> documentToMap(Document document) {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : document.entrySet()) {
            if (entry.getValue() instanceof Document) {
                map.put(entry.getKey(), documentToMap((Document) entry.getValue()));
            } else {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        return map;
    }
}
