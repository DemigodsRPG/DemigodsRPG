package com.demigodsrpg.demigods.classic.registry;

import com.demigodsrpg.demigods.classic.DGClassic;
import com.demigodsrpg.demigods.classic.deity.Deity;
import com.demigodsrpg.demigods.classic.deity.IDeity;
import com.demigodsrpg.demigods.classic.model.PlayerModel;
import com.demigodsrpg.demigods.classic.util.YamlFileUtil;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class PlayerRegistry extends AbstractRegistry<UUID, PlayerModel> {
    public static final String FILE_NAME = "players.dgc";

    @Deprecated
    public PlayerModel fromName(final String name) {
        return Iterables.find(getRegistered(), new Predicate<PlayerModel>() {
            @Override
            public boolean apply(PlayerModel model) {
                return model.getLastKnownName().equalsIgnoreCase(name);
            }
        }, null);

    }

    public PlayerModel fromId(UUID id) {
        if (super.REGISTERED_DATA.containsKey(id)) {
            return super.REGISTERED_DATA.get(id);
        }
        return null;
    }

    public PlayerModel fromPlayer(Player player) {
        PlayerModel found = fromId(player.getUniqueId());
        if (found == null) {
            found = new PlayerModel(player);
            register(found);
        }
        return found;
    }

    public Set<OfflinePlayer> getOfflinePlayers() {
        Set<OfflinePlayer> offlinePlayers = new HashSet<>();
        for (PlayerModel model : getRegistered()) {
            offlinePlayers.add(model.getOfflinePlayer());
        }
        return offlinePlayers;
    }

    public Set<PlayerModel> getOnlineInAlliance(IDeity.Alliance alliance) {
        Set<PlayerModel> players = new HashSet<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerModel model = fromPlayer(player);
            if (model.getAlliance().equals(alliance)) {
                players.add(model);
            }
        }
        return players;
    }

    public Collection<PlayerModel> fromDeity(final Deity deity) {
        return Collections2.filter(getRegistered(), new Predicate<PlayerModel>() {
            @Override
            public boolean apply(PlayerModel model) {
                return model.getMajorDeity().equals(deity) || model.getContractedDeities().contains(deity);
            }
        });
    }

    public List<String> getNameStartsWith(final String name) {
        List<String> names = new ArrayList<>();
        for (PlayerModel model : getRegistered()) {
            if (TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - model.getLastLoginTime()) < 3
                    && model.getLastKnownName().toLowerCase().startsWith(name.toLowerCase())) {
                names.add(model.getLastKnownName());
            }
        }
        return names;
    }

    public Map<UUID, PlayerModel> getFileData() {
        // Grab the current file.
        FileConfiguration data = YamlFileUtil.getConfiguration(DGClassic.SAVE_PATH, FILE_NAME);

        // Convert the raw file data into more usable data, in map form.
        Map<UUID, PlayerModel> map = new HashMap<>();
        for (String stringId : data.getKeys(false)) {
            try {
                map.put(UUID.fromString(stringId), new PlayerModel(UUID.fromString(stringId), data.getConfigurationSection(stringId)));
            } catch (Exception oops) {
                oops.printStackTrace();
            }
        }
        return map;
    }

    public void registerFromFile() {
        register(getFileData().values());
    }

    public boolean saveToFile() {
        // Grab the current file, and its data as a usable map.
        FileConfiguration currentFile = YamlFileUtil.getConfiguration(DGClassic.SAVE_PATH, FILE_NAME);
        final Map<UUID, PlayerModel> currentFileData = getFileData();

        // Create/overwrite a configuration section if new data exists.
        for (Map.Entry<UUID, PlayerModel> entry : Collections2.filter(REGISTERED_DATA.entrySet(), new Predicate<Map.Entry<UUID, PlayerModel>>() {
            @Override
            public boolean apply(Map.Entry<UUID, PlayerModel> value) {
                return !currentFileData.containsKey(value.getKey()) || currentFileData.get(value.getKey()).equals(value.getValue());
            }
        }))
            currentFile.createSection(entry.getKey().toString(), entry.getValue().serialize());

        // Remove old unneeded data.
        for (PlayerModel model : Collections2.filter(currentFileData.values(), new Predicate<PlayerModel>() {
            @Override
            public boolean apply(PlayerModel key) {
                return !getRegistered().contains(key);
            }
        }))
            currentFile.set(model.getPersistantId().toString(), null);

        // Save the file!
        return YamlFileUtil.saveFile(DGClassic.SAVE_PATH, FILE_NAME, currentFile);
    }
}
