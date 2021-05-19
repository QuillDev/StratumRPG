package moe.quill.stratumrpg.Events.MiningEvents.MiningLogEvents;

import moe.quill.stratumrpg.Events.MiningEvents.MiningEvent;
import moe.quill.stratumrpg.Players.StratumPlayer;
import moe.quill.stratumrpg.Players.StratumPlayerManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class MiningLogEvent extends MiningEvent {
    public MiningLogEvent(StratumPlayerManager playerManager, Plugin plugin) {
        super(playerManager, new HashMap<>() {{
            put(Material.ACACIA_LOG, 25f);
            put(Material.OAK_LOG, 25f);
            put(Material.BIRCH_LOG, 25f);
            put(Material.DARK_OAK_LOG, 25f);
            put(Material.JUNGLE_LOG, 25f);
            put(Material.SPRUCE_LOG, 25f);
            put(Material.CRIMSON_STEM, 25f);
            put(Material.WARPED_STEM, 25f);
        }}, plugin);
    }


    @EventHandler
    public void experienceOnMineLog(BlockBreakEvent event) {
        final var eventData = getMiningEventData(event);
        if (eventData == null) return;

        final var stratumPlayer = eventData.stratumPlayer();

        //Get the xp for the event
        final var xp = xpMap.get(event.getBlock().getType());
        if (xp == null) return;
        //Set the mining xp of the player depending on what they mined
        stratumPlayer.setLoggingXP(stratumPlayer.getLoggingXP() + xp);
        checkLevelup(stratumPlayer);
    }

    private void checkLevelup(StratumPlayer player) {
        final var level = player.getLoggingLevel();
        final var xp = player.getLoggingXP();

        final var xpNeeded = (level * 50) + (Math.pow(level + 1, 2.3f));
        if (xp > xpNeeded) {
            player.setLoggingLevel(player.getLoggingLevel() + 1);
            player.getPlayer().sendMessage(
                    Component.text("Your logging level increased to")
                            .append(Component.space())
                            .append(Component.text(player.getLoggingLevel()))
                            .append(Component.text("!"))
                            .color(TextColor.color(0xD21B))
            );
        }
    }
}
