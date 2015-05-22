/*
 * Copyright 2015 Demigods RPG
 * Copyright 2015 Alexander Chauncey
 * Copyright 2015 Alex Bennett
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.demigodsrpg.data.registry;

import com.demigodsrpg.data.DGData;
import com.demigodsrpg.data.Setting;
import com.demigodsrpg.util.datasection.AbstractDataRegistry;
import com.demigodsrpg.util.datasection.AbstractPersistentModel;

public abstract class AbstractDemigodsDataRegistry<T extends AbstractPersistentModel<String>> extends AbstractDataRegistry<T> {
    public AbstractDemigodsDataRegistry() {
        super(DGData.SAVE_PATH, Setting.SAVE_PRETTY, Setting.PSQL_PERSISTENCE, Setting.PSQL_CONNECTION);
    }
}
