package moe.quill.stratumrpg.Events;

import com.google.inject.Inject;
import moe.quill.StratumCommon.Database.IDatabaseService;
import moe.quill.stratumrpg.Players.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener extends RPGListener {

    @Inject
    public PlayerJoinListener(PlayerManager playerManager, IDatabaseService databaseService) {
        super(playerManager, databaseService);
    }

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
        final var player = event.getPlayer();
        playerManager.addPlayer(player);

    }
}
