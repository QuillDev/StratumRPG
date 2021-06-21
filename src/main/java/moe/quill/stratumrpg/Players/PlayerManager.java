package moe.quill.stratumrpg.Players;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import moe.quill.StratumCommonApi.Database.DataTypes.RPGPlayer;
import moe.quill.StratumCommonApi.Database.IDatabaseService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.UUID;

@Singleton
public class PlayerManager {

    private final HashMap<UUID, RPGPlayer> playerMap = new HashMap<>();
    private final IDatabaseService databaseService;
    private static final Logger logger = LoggerFactory.getLogger(PlayerManager.class.getSimpleName());

    @Inject
    public PlayerManager(IDatabaseService databaseService) {
        this.databaseService = databaseService;
        //Add all online players to the cache
        Bukkit.getServer().getOnlinePlayers().forEach(this::addPlayer);
    }

    public void addPlayer(Player player) {
        var rpgPlayer = databaseService.getPlayer(player.getUniqueId());
        if (rpgPlayer == null) {
            databaseService.createPlayer(player.getUniqueId());
            rpgPlayer = databaseService.getPlayer(player.getUniqueId());
        }

        if (rpgPlayer == null) {
            logger.error(String.format("Failed to add player %s to the cache!", player.getName()));
            return;
        }

        //Log we added them if they're not already in the cache
        if (!playerMap.containsKey(player.getUniqueId())) {
            logger.info(String.format("Added player %s | UUID: %s | to the player cache", player.getName(), player.getUniqueId()));
        }

        playerMap.put(player.getUniqueId(), rpgPlayer);
    }

    /**
     * Get a player from the cached map
     *
     * @param player to get from the cached map
     * @return the cached player
     */
    public RPGPlayer getPlayer(Player player) {
        return playerMap.getOrDefault(player.getUniqueId(), null);
    }

    public void savePlayer(Player player) {
        final var cachedPlayer = getPlayer(player);
        if (cachedPlayer == null) return;

        //If the user has not been previously saved to the database, save a new entry
        final var existingPlayer = databaseService.getPlayer(player.getUniqueId());
        if (existingPlayer == null) {
            logger.info(String.format("Saving data for player %s | UUID: %s", player.getName(), player.getUniqueId()));
        }

        databaseService.savePlayer(getPlayer(player));
    }

    public void removePlayer(Player player) {
        logger.info(String.format("Removed player %s from the cache.", player.getUniqueId()));
        playerMap.remove(player.getUniqueId());
    }

    public void savePlayers() {
        logger.info(String.format("Saving data for %s players!", playerMap.size()));
        databaseService.savePlayer(playerMap.values());
    }

    public void clearPlayerCache() {
        playerMap.clear();
        logger.info("Cleared player cache!");
    }
}
