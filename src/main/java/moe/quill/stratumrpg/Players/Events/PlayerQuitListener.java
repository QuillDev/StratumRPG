package moe.quill.stratumrpg.Players.Events;

import com.google.inject.Inject;
import moe.quill.StratumCommonApi.Database.IDatabaseService;
import moe.quill.stratumrpg.Players.PlayerManager;
import moe.quill.stratumrpg.Skills.Experience.RPGListener;
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
