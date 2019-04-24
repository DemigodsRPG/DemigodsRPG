package com.demigodsrpg.registry.mongo;

import com.demigodsrpg.model.TributeModel;
import com.demigodsrpg.registry.TributeRegistry;
import com.demigodsrpg.util.datasection.AbstractMongoRegistry;
import com.mongodb.client.MongoDatabase;

public class MTributeRegistry extends AbstractMongoRegistry<TributeModel> implements TributeRegistry {
    public MTributeRegistry(MongoDatabase database) {
        super(database.getCollection(NAME), 0, false);
    }
}
