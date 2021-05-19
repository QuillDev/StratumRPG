package moe.quill.stratumrpg.Events.MiningEvents;

import moe.quill.stratumrpg.Events.ExperienceEvents.ExperienceEvent;
import moe.quill.stratumrpg.Metadata.BlockMeta;
import moe.quill.stratumrpg.Players.StratumPlayerManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class MiningEvent extends ExperienceEvent {

    protected HashMap<Material, Float> xpMap;
    private final Plugin plugin;

    public MiningEvent(StratumPlayerManager playerManager, HashMap<Material, Float> xpMap, Plugin plugin) {
        super(playerManager, xpMap);
        this.xpMap = xpMap;
        this.plugin = plugin;
    }

    @EventHandler
    public void tagPlacedBlock(BlockPlaceEvent event) {
        final var block = event.getBlock();
        if (!xpMap.containsKey(block.getType())) return;
        block.setMetadata("placedByPlayer", BlockMeta.blockPlacedMeta);
    }

    @EventHandler
    public void untagBrokenBlock(BlockPlaceEvent event) {
        final var block = event.getBlock();
        if (!xpMap.containsKey(block.getType())) return;
        block.removeMetadata("placedByPlayer", plugin);
    }

    public MiningEventData getMiningEventData(BlockBreakEvent event) {
        final var player = event.getPlayer();
        final var stratumPlayer = playerManager.getPlayer(player);
        if (stratumPlayer == null) return null;
        final var block = event.getBlock();
        if (block.hasMetadata("placedByPlayer")) return null;
        return new MiningEventData(stratumPlayer);
    }
}
