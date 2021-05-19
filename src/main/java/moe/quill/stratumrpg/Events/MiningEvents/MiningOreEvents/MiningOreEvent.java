package moe.quill.stratumrpg.Events.MiningEvents.MiningOreEvents;

import moe.quill.stratumrpg.Events.MiningEvents.MiningEvent;
import moe.quill.stratumrpg.Metadata.BlockMeta;
import moe.quill.stratumrpg.Players.StratumPlayer;
import moe.quill.stratumrpg.Players.StratumPlayerManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class MiningOreEvent extends MiningEvent {

    public MiningOreEvent(StratumPlayerManager playerManager, Plugin plugin) {
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
        }}, plugin);
    }

    // Give experience whenever an ore is mined.
    @EventHandler
    public void experienceOnMineOre(BlockBreakEvent event) {
        final var eventData = getMiningEventData(event);
        if (eventData == null) return;

        final var stratumPlayer = eventData.stratumPlayer();

        //Get the xp for the event
        final var xp = xpMap.get(event.getBlock().getType());
        if (xp == null) return;
        //Set the mining xp of the player depending on what they mined
        stratumPlayer.setMiningXP(stratumPlayer.getMiningXP() + xp);
        checkLevelup(stratumPlayer);
    }

    /**
     * Check if the given XP was enough to level up
     *
     * @param player to check levelup for
     */
    private void checkLevelup(StratumPlayer player) {
        final var level = player.getMiningLevel();
        final var xp = player.getMiningXP();

        final var xpNeeded = (level * 50) + (Math.pow(level + 1, 2.3f));
        if (xp > xpNeeded) {
            player.setMiningLevel(player.getMiningLevel() + 1);
            player.getPlayer().sendMessage(
                    Component.text("Your mining level increased to")
                            .append(Component.space())
                            .append(Component.text(player.getMiningLevel()))
                            .append(Component.text("!"))
                            .color(TextColor.color(0xD21B))
            );
        }
    }
}
