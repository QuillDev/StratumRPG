package moe.quill.stratumrpg.Skills.Experience.Events;

import com.google.inject.Inject;
import moe.quill.StratumCommonApi.Database.IDatabaseService;
import moe.quill.stratumcommonutils.MaterialGroups.MaterialGroups;
import moe.quill.stratumrpg.Players.PlayerManager;
import moe.quill.stratumrpg.Skills.SkillType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;

public class LoggingExperienceListener extends RpgExperienceListener {

    private static final float XpPerLog = 5f;

    @Inject
    public LoggingExperienceListener(Plugin plugin, PlayerManager playerManager, IDatabaseService databaseService) {
        super(plugin, playerManager, databaseService, SkillType.LOGGING);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogMine(BlockBreakEvent event) {
        final var experienceEventData = getExperienceEventData(event, MaterialGroups.LOGS);
        if (experienceEventData == null) return;
        final var rpgPlayer = experienceEventData.getRpgPlayer();
        final var player = experienceEventData.getPlayer();

        //Add Exp to the player
        rpgPlayer.setLoggingExperience(rpgPlayer.getLoggingExperience() + XpPerLog);

        //Level up the player if they exceeded the required experience for the given level
        final var currentExp = rpgPlayer.getLoggingExperience();
        final var xpNeeded = calculateXpNeeded(rpgPlayer.getLoggingLevel());
        if (currentExp >= xpNeeded) {
            rpgPlayer.setLoggingLevel(rpgPlayer.getLoggingLevel() + 1);
            player.sendMessage(String.format("Your logging skill has improved, leveled up to %s!", rpgPlayer.getLoggingLevel()));
        }

        showUpdatedXpBar(player, rpgPlayer.getLoggingExperience(), rpgPlayer.getLoggingLevel());
        showActionBarUpdate(player, XpPerLog);
    }
}
