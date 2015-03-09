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

package com.demigodsrpg.game.registry;

import com.demigodsrpg.game.DGGame;
import com.demigodsrpg.game.battle.Participant;
import com.demigodsrpg.game.util.ZoneUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class BattleRegistry {
    public boolean isInBattle(Participant participant) {
        return true;
    } // TODO

    public boolean canParticipate(Entity entity) {
        return !(entity instanceof Player) || defineParticipant(entity).getCanPvp();
    }

    Participant defineParticipant(Entity entity) {
        if (entity instanceof Player) {
            return DGGame.PLAYER_R.fromPlayer((Player) entity);
        }
        return null;
    }

    public boolean canTarget(Entity entity) {
        if (ZoneUtil.isNoDGWorld(entity.getWorld())) return false;
        Participant participant = defineParticipant(entity);
        return participant == null || participant.getCanPvp();
    }
}
