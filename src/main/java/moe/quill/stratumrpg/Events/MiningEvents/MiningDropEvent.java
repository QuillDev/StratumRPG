package moe.quill.stratumrpg.Events.MiningEvents;

import moe.quill.stratumrpg.Players.StratumPlayerManager;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Random;

public class MiningDropEvent extends MiningEvent {


    private final Random rand = new Random();

    public MiningDropEvent(StratumPlayerManager playerManager) {
        super(playerManager);
    }

    @EventHandler
    public void miningDropEvent(BlockBreakEvent event) {
        final var eventData = getMiningEventData(event);
        final var block = event.getBlock();
        if (eventData == null) return;
        if (!xpMap.containsKey(block.getType())) return;

        final var stratumPlayer = eventData.stratumPlayer();
        final var mcPlayer = stratumPlayer.getPlayer();
        final var heldItem = mcPlayer.getInventory().getItemInMainHand();
        final var level = stratumPlayer.getMiningLevel();
        final var multiplier = getMultiplier(level);
        if (multiplier == 1) return; //If we aren't adding any drops, just return
        event.setDropItems(false);

        //Set the drops at that given location
        final var drops = block.getDrops(heldItem);
        drops.forEach(drop -> {
            drop.setAmount(drop.getAmount() + 1);
            mcPlayer.getWorld().dropItemNaturally(block.getLocation(), drop);
        });
        mcPlayer.sendMessage(
                Component.text("Triggered")
                .append(Component.space())
                .append(Component.text(multiplier))
                .append(Component.space())
                .append(Component.text("x"))
                .append(Component.space())
                .append(Component.text("drops!"))
        );


    }

    public int getMultiplier(int level) {

        //If the level is over 100 and we got a good roll, return 3
        if (level > 100) {
            if ((level / 300f) > rand.nextFloat()) {
                return 3;
            }
        }

        return ((level / 100f) > rand.nextFloat()) ? 2 : 1;
    }
}
