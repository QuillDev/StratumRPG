package moe.quill.stratumrpg.Skills.Experience.Events;

import com.google.inject.Inject;
import moe.quill.StratumCommonApi.Database.IDatabaseService;
import moe.quill.stratumcommonutils.MaterialGroups.MaterialGroups;
import moe.quill.stratumrpg.Players.PlayerManager;
import moe.quill.stratumrpg.Skills.SkillType;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class MiningExperienceListener extends RpgExperienceListener {

    //Map Experience Values
    private static final HashMap<Material, Float> expMap = new HashMap<>() {
        {
            //Normal Ores
            put(Material.COAL_ORE, 2f);
            put(Material.COPPER_ORE, 3f);
            put(Material.IRON_ORE, 4f);
            put(Material.GOLD_ORE, 5f);
            put(Material.DIAMOND_ORE, 10f);
            put(Material.EMERALD_ORE, 13f);
            put(Material.LAPIS_ORE, 2f);
            put(Material.REDSTONE_ORE, 2f);

            //Deep slate ores
            put(Material.DEEPSLATE_COAL_ORE, 2f);
            put(Material.DEEPSLATE_COPPER_ORE, 3f);
            put(Material.DEEPSLATE_IRON_ORE, 4f);
            put(Material.DEEPSLATE_GOLD_ORE, 5f);
            put(Material.DEEPSLATE_DIAMOND_ORE, 10f);
            put(Material.DEEPSLATE_EMERALD_ORE, 13f);
            put(Material.DEEPSLATE_LAPIS_ORE, 2f);
            put(Material.DEEPSLATE_REDSTONE_ORE, 2f);
            put(Material.NETHER_QUARTZ_ORE, 2f);
            put(Material.NETHER_GOLD_ORE, 2f);
            put(Material.ANCIENT_DEBRIS, 50f);

            //Stone
            put(Material.STONE, .05f);
            put(Material.COBBLESTONE, .05f);
            put(Material.DIORITE, .05f);
            put(Material.GRANITE, .05f);
            put(Material.ANDESITE, .05f);
            put(Material.DEEPSLATE, .05f);

            //Extra
            put(Material.AMETHYST_CLUSTER, 1.5f);
        }
    };


    @Inject
    public MiningExperienceListener(Plugin plugin, PlayerManager playerManager, IDatabaseService databaseService) {
        super(plugin, playerManager, databaseService, SkillType.MINING);
    }

    @EventHandler
    public void onOreMine(BlockBreakEvent event) {
        final var experienceData = getExperienceEventData(event, MaterialGroups.MINING);
        if (experienceData == null) return;
        final var rpgPlayer = experienceData.getRpgPlayer();
        final var player = experienceData.getPlayer();
        final var block = experienceData.getBlock();

        //Check the xp value
        final var xpGained = expMap.getOrDefault(block.getType(), 0f);
        if (xpGained == 0f) return;

        //Add Exp to the player
        rpgPlayer.setMiningExperience(rpgPlayer.getMiningExperience() + xpGained);

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