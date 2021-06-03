package moe.quill.stratumrpg.Events;

import com.google.inject.Inject;
import moe.quill.StratumCommon.Database.IDatabaseService;
import moe.quill.stratumrpg.Players.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener extends RPGListener {

    @Inject
    public PlayerQuitListener(PlayerManager playerManager, IDatabaseService databaseService) {
        super(playerManager, databaseService);
    }

    @EventHandler
    public void playerQuitEvent(PlayerQuitEvent event) {
        playerManager.savePlayer(event.getPlayer());
        playerManager.removePlayer(event.getPlayer());
    }
}
