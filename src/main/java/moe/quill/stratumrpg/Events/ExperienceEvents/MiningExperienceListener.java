package moe.quill.stratumrpg.Events.ExperienceEvents;

import com.google.inject.Inject;
import moe.quill.StratumCommonApi.Database.IDatabaseService;
import moe.quill.stratumcommonutils.MaterialGroups.MaterialGroups;
import moe.quill.stratumrpg.Players.PlayerManager;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;

public class MiningExperienceListener extends RpgExperienceListener {

    @Inject
    public MiningExperienceListener(Plugin plugin, PlayerManager playerManager, IDatabaseService databaseService) {
        super(plugin, playerManager, databaseService, "Mining");
    }

    @EventHandler
    public void onOreMine(BlockBreakEvent event) {
        if (!event.isDropItems()) return;
        final var player = event.getPlayer();
        final var rpgPlayer = playerManager.getPlayer(player);
        if (rpgPlayer == null) return; //If there is no RPGPlayer for this player, return

        //Check block eligibility for experience
        final var broken = event.getBlock();
        if (!MaterialGroups.MINING.contains(broken.getType())) return; //if it's not eligible, just return out

        //Add Exp to the player
        rpgPlayer.setMiningExperience(rpgPlayer.getMiningExperience() + 5); //TODO: Add XP Values for each block

        final var xpGained = 5;
        //Level up the player if they exceeded the required experience for the given level
        final var currentExp = rpgPlayer.getMiningExperience();
        final var xpNeeded = calculateXpNeeded(rpgPlayer.getMiningLevel());
        if (currentExp >= xpNeeded) {
            rpgPlayer.setMiningLevel(rpgPlayer.getMiningLevel() + 1);
            player.sendMessage(String.format("Your mining skill has improved, leveled up to %s!", rpgPlayer.getMiningLevel()));
        }

        showUpdatedXpBar(player, rpgPlayer.getMiningExperience(), rpgPlayer.getMiningLevel());
        showActionBarUpdate(player, xpGained);
    }
}
