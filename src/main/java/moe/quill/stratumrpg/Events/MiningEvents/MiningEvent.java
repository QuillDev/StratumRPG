package moe.quill.stratumrpg.Events.MiningEvents;

import moe.quill.stratumrpg.Events.ExperienceEvents.ExperienceEvent;
import moe.quill.stratumrpg.Players.StratumPlayerManager;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;

public class MiningEvent extends ExperienceEvent {
    public MiningEvent(StratumPlayerManager playerManager) {
        super(playerManager, new HashMap<>() {{
            put(Material.STONE, .05f);
            put(Material.BLACKSTONE, .05f);
            put(Material.BASALT, .05f);
            put(Material.REDSTONE_ORE, 1f);
            put(Material.LAPIS_ORE, 1f);
            put(Material.NETHER_QUARTZ_ORE, 1f);
            put(Material.NETHER_GOLD_ORE, 1f);
            put(Material.GILDED_BLACKSTONE, 1f);
            put(Material.COAL_ORE, 2f);
            put(Material.IRON_ORE, 4f);
            put(Material.GOLD_ORE, 4f);
            put(Material.DIAMOND_ORE, 8f);
            put(Material.EMERALD_ORE, 12f);
            put(Material.ANCIENT_DEBRIS, 25f);
        }});
    }

    public MiningEventData getMiningEventData(BlockBreakEvent event) {
        final var player = event.getPlayer();
        final var stratumPlayer = playerManager.getPlayer(player);
        if (stratumPlayer == null) return null;
        final var block = event.getBlock();
        if (block.hasMetadata("placedByPlayer")) return null;
        return new MiningEventData(stratumPlayer, block);
    }
}
