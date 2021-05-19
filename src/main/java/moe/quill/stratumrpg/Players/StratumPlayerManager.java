package moe.quill.stratumrpg.Players;

import moe.quill.stratumrpg.Database.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class StratumPlayerManager {

    private final ArrayList<StratumPlayer> playerCache = new ArrayList<>();
    private final DatabaseManager databaseManager;
    private final static Logger logger = LoggerFactory.getLogger(StratumPlayerManager.class.getSimpleName());

    public StratumPlayerManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Load the given player into the stratum player manager
     *
     * @param player to load into the player manager
     */
    public void loadPlayer(Player player) {
        final var stratumPlayer = databaseManager.getPlayer(player);
        if (stratumPlayer == null) return;
        playerCache.add(stratumPlayer); // add the stratum player to the player cache
        logger.info(String.format("Added player to the cache. -> Name: %s | UUID:  %s", player.getName(), player.getUniqueId()));
    }

    /**
     * Unload the given player from the stratum player manager
     *
     * @param player to unload from the cache
     */
    public void unloadPlayer(Player player) {
        final var stratumPlayer = getPlayer(player); // Get the player from the database
        if (stratumPlayer == null) return;
        playerCache.remove(stratumPlayer); // Remove the player from the cache
        savePlayer(stratumPlayer); //save the player to the DB
        logger.info(String.format("Removed player from the cache. -> Name: %s | UUID: %s ", player.getName(), player.getUniqueId()));
    }

    /**
     * Get the given player from the stratum player manager
     *
     * @param player to get from the player manager
     * @return the given stratum player
     */
    public StratumPlayer getPlayer(Player player) {
        return playerCache
                .stream()
                .filter(cachePlayer -> cachePlayer.getPlayer().getUniqueId().equals(player.getUniqueId()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Save changes for all online players to the database
     */
    public void saveAllOnlinePlayers() {
        playerCache.forEach(this::savePlayer);
        logger.info(String.format("Attempting to auto-save data for %s users!", playerCache.size()));
    }

    /**
     * Save data changes for the given user to the database
     *
     * @param player to save to the database
     */
    public void savePlayer(StratumPlayer player) {
        databaseManager.updatePlayerDocument(player);
    }

    /**
     * Add all online players to the player manager
     */
    public void enable() {
        Bukkit.getServer()
                .getOnlinePlayers()
                .forEach(this::loadPlayer);
    }

    /**
     * Remove all players from the cache whenever the plugin is disabled
     */
    public void disable() {
        saveAllOnlinePlayers();
    }
}
