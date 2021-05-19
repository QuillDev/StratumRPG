package moe.quill.stratumrpg.Events.MiningEvents;

import moe.quill.stratumrpg.Players.StratumPlayer;
import org.bukkit.block.Block;

public class MiningEventData {

    private final StratumPlayer player;

    public MiningEventData(StratumPlayer player) {
        this.player = player;
    }

    public StratumPlayer stratumPlayer() {
        return player;
    }
}
