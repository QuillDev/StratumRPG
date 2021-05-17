package moe.quill.stratumrpg.Events.MiningEvents;

import moe.quill.stratumrpg.Players.StratumPlayer;
import org.bukkit.block.Block;

public class MiningEventData {

    private final StratumPlayer player;
    private final Block block;

    public MiningEventData(StratumPlayer player, Block block) {
        this.player = player;
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }

    public StratumPlayer stratumPlayer() {
        return player;
    }
}
