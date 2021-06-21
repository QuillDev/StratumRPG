package moe.quill.stratumrpg.Skills.Experience.Events;

import com.google.inject.Inject;
import moe.quill.StratumCommonApi.Database.IDatabaseService;
import moe.quill.stratumrpg.Skills.Experience.ExperienceEventData;
import moe.quill.stratumrpg.Skills.Experience.RPGListener;
import moe.quill.stratumrpg.Skills.ExperienceUi.SkillXpBarData;
import moe.quill.stratumrpg.Players.PlayerManager;
import moe.quill.stratumrpg.Skills.SkillType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;

public abstract class RpgExperienceListener extends RPGListener {

    protected final String skillName;
    private final float difficulty;
    private final Plugin plugin;

    private final HashMap<Player, SkillXpBarData> xpBarCache = new HashMap<>();

    /**
     * @param plugin          to register scheduled events with
     * @param playerManager   to manage players with
     * @param databaseService service for working with the database
     * @param skillType       to assign this xp modifiers with
     */
    @Inject
    public RpgExperienceListener(
            Plugin plugin,
            PlayerManager playerManager,
            IDatabaseService databaseService,
            SkillType skillType
    ) {
        super(playerManager, databaseService);
        this.plugin = plugin;
        this.skillName = skillType.getSkillName();
        this.difficulty = skillType.getDifficulty();
    }


    /**
     * Show the updated experience bar to the player depending on how much XP they gained
     *
     * @param player to get experience for
     * @param cur    current experience of the player in this skill
     * @param level  of the player in this skill
     */
    public void showUpdatedXpBar(Player player, float cur, int level) {

        var xpBarData = xpBarCache.getOrDefault(player, null);

        if (xpBarData == null) {

            xpBarData = new SkillXpBarData(
                    plugin,
                    player,
                    Bukkit.createBossBar(skillName, BarColor.RED, BarStyle.SOLID)
            );
            xpBarCache.put(player, xpBarData);
        }

        final var max = calculateXpNeeded(level);

        final var adjustedCur = cur - ((level == 1) ? 0 : calculateXpNeeded(level - 1));
        xpBarData.update(adjustedCur, max);
        xpBarData.show();
    }

    /**
     * Show XP gain to the player on their action bar
     *
     * @param player   to show xp gains to
     * @param xpGained amount of xp they gained
     */
    public void showActionBarUpdate(Player player, float xpGained) {
        player.sendActionBar(
                Component.text("+")
                        .append(Component.text(xpGained))
                        .append(Component.space())
                        .append(Component.text(skillName))
                        .append(Component.space())
                        .append(Component.text("XP"))
                        .color(TextColor.color(Color.green.getRGB()))
        );
    }

    /**
     * Calculated Xp needed for the next level with a custom difficulty
     *
     * @param level the player is currently at
     * @return the experience needed for the next level
     */
    public float calculateXpNeeded(int level) {
        return (float) (((500 * level) + Math.pow((level - 1), 2.4f)) * this.difficulty);
    }

    /**
     * Get experience event data for the given event
     *
     * @param event     to get experience from
     * @param whitelist to check whether the given block is in
     * @return the event data for that event
     */
    public ExperienceEventData getExperienceEventData(BlockBreakEvent event, HashSet<Material> whitelist) {
        if (!event.isDropItems()) return null;
        final var player = event.getPlayer();
        final var rpgPlayer = playerManager.getPlayer(player);
        if (rpgPlayer == null) return null; //If there is no RPGPlayer for this player, return

        //Check block eligibility for experience
        final var broken = event.getBlock();
        if (!whitelist.contains(broken.getType())) return null; //if it's not eligible, just return out
        return new ExperienceEventData(player, rpgPlayer, event, event.getBlock(), whitelist);
    }
}
