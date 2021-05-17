package moe.quill.stratumrpg.Events.MiningEvents;

import moe.quill.stratumrpg.Metadata.BlockMeta;
import moe.quill.stratumrpg.Players.StratumPlayer;
import moe.quill.stratumrpg.Players.StratumPlayerManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class MiningExperienceEvent extends MiningEvent {

    public MiningExperienceEvent(StratumPlayerManager playerManager) {
        super(playerManager);
    }

    // Put a meta tag on the given ore so that way it doesn't count towards XP gain
    @EventHandler
    public void tagPlayerPlacedOre(BlockPlaceEvent event) {
        final var block = event.getBlock();
        if (!xpMap.containsKey(block.getType())) return;
        block.setMetadata("placedByPlayer", BlockMeta.blockPlacedMeta);
    }

    // Give experience whenever an ore is mined.
    @EventHandler
    public void experienceOnMineOre(BlockBreakEvent event) {
        final var eventData = getMiningEventData(event);
        System.out.println("event data 1");
        if (eventData == null) return;
        System.out.println("event data 2");


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
