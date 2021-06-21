package moe.quill.stratumrpg.Players.Events;

import com.google.inject.Inject;
import moe.quill.StratumCommonApi.Database.IDatabaseService;
import moe.quill.stratumrpg.Players.PlayerManager;
import moe.quill.stratumrpg.Skills.Experience.RPGListener;
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
