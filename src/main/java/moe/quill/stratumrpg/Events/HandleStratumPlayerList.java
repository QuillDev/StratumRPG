package moe.quill.stratumrpg.Events;

import moe.quill.stratumrpg.Players.StratumPlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class HandleStratumPlayerList implements Listener {

    private final StratumPlayerManager playerManager;

    public HandleStratumPlayerList(StratumPlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        playerManager.loadPlayer(event.getPlayer()); //Load the player from the manager
    }

    @EventHandler
    public void handlePlayerLeave(PlayerQuitEvent event) {
        playerManager.unloadPlayer(event.getPlayer()); //Unload the player from the manager
    }
}
