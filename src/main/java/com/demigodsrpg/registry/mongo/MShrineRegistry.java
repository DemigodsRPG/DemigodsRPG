package com.demigodsrpg.registry.mongo;

import com.demigodsrpg.model.ShrineModel;
import com.demigodsrpg.registry.ShrineRegistry;
import com.demigodsrpg.util.datasection.AbstractMongoRegistry;
import com.mongodb.client.MongoDatabase;

public class MShrineRegistry extends AbstractMongoRegistry<ShrineModel> implements ShrineRegistry {
    public MShrineRegistry(MongoDatabase database) {
        super(database.getCollection(NAME), 0);
    }
}
