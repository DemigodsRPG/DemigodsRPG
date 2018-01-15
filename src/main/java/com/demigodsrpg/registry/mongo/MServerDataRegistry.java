package com.demigodsrpg.registry.mongo;

import com.demigodsrpg.model.ServerDataModel;
import com.demigodsrpg.registry.ServerDataRegistry;
import com.demigodsrpg.util.datasection.AbstractMongoRegistry;
import com.mongodb.client.MongoDatabase;

public class MServerDataRegistry extends AbstractMongoRegistry<ServerDataModel> implements ServerDataRegistry {
    public MServerDataRegistry(MongoDatabase database) {
        super(database.getCollection(NAME), 0);
    }
}
