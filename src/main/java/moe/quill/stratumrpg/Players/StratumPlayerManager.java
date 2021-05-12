package moe.quill.stratumrpg.Players;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StratumPlayerManager {

    private final ArrayList<StratumPlayer> players = new ArrayList<>();
    private final Logger logger = Logger.getLogger("Minecraft");

    public StratumPlayerManager() {
    }

    /**
     * Load the given player into the stratum player manager
     *
     * @param player to load into the player manager
     */
    public void loadPlayer(Player player) {
        final var existingPlayer = getPlayer(player);
        if (existingPlayer != null) return;
        players.add(new StratumPlayer(player));
        log("Loaded player " + player.getName() + " UUID: " + player.getUniqueId());

    }

    /**
     * Unload the given player from the stratum player manager
     *
     * @param player to unload
     */
    public void unloadPlayer(Player player) {
        final var existingPlayer = getPlayer(player);
        if (existingPlayer == null) return;
        players.remove(existingPlayer); //remove the player from the list
        log("Removed player " + player.getName() + " UUID: " + player.getUniqueId());
    }

    /**
     * Get the given player from the stratum player manager
     *
     * @param player to get from the player manager
     * @return the given stratum player
     */
    public StratumPlayer getPlayer(Player player) {
        return players.stream()
                .filter(stratumPlayer -> stratumPlayer.getPlayer()
                        .getUniqueId()
                        .equals(player.getUniqueId()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Add all online players to the player manager
     */
    public void enable() {
        Bukkit.getServer().getOnlinePlayers()
                .forEach(this::loadPlayer);
    }

    /**
     * Remove all players from the cache whenever the plugin is disabled
     */
    public void disable() {
        players.clear();
        log("Disabling plugin, removing all players from the cache.");
    }

    /**
     * Log the given message
     *
     * @param message to log
     */
    public void log(String message) {
        logger.log(Level.INFO, String.format("[%s] " + message, this.getClass().getSimpleName()));
    }
}
