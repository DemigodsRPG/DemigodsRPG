package com.demigodsrpg.registry.mongo;

import com.demigodsrpg.model.SpawnModel;
import com.demigodsrpg.registry.SpawnRegistry;
import com.demigodsrpg.util.datasection.AbstractMongoRegistry;
import com.mongodb.client.MongoDatabase;

public class MSpawnRegistry extends AbstractMongoRegistry<SpawnModel> implements SpawnRegistry {
    public MSpawnRegistry(MongoDatabase database) {
        super(database.getCollection(NAME), 0);
    }
}
