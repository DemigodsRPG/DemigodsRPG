package com.demigodsrpg.registry.mongo;

import com.demigodsrpg.model.PlayerModel;
import com.demigodsrpg.registry.PlayerRegistry;
import com.demigodsrpg.util.datasection.AbstractMongoRegistry;
import com.mongodb.client.MongoDatabase;

public class MPlayerRegistry extends AbstractMongoRegistry<PlayerModel> implements PlayerRegistry {
    public MPlayerRegistry(MongoDatabase database) {
        super(database.getCollection(NAME), 0, true);
    }
}
