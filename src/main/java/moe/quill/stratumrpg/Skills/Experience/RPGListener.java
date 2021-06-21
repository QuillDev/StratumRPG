package moe.quill.stratumrpg.Skills.Experience;

import com.google.inject.Inject;
import moe.quill.StratumCommonApi.Database.IDatabaseService;
import moe.quill.stratumrpg.Players.PlayerManager;
import org.bukkit.event.Listener;

public abstract class RPGListener implements Listener {

    protected final IDatabaseService databaseService;
    protected final PlayerManager playerManager;

    @Inject
    public RPGListener(PlayerManager playerManager, IDatabaseService databaseService) {
        this.databaseService = databaseService;
        this.playerManager = playerManager;
    }

    public IDatabaseService getDatabaseService() {
        return databaseService;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}
