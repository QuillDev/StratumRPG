package moe.quill.stratumrpg.Events.ExperienceEvents;

import com.google.inject.Inject;
import moe.quill.StratumCommonApi.Database.DataTypes.RPGPlayer;
import moe.quill.StratumCommonApi.Database.IDatabaseService;
import moe.quill.stratumrpg.Events.RPGListener;
import moe.quill.stratumrpg.Players.PlayerManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.awt.*;
import java.util.HashMap;

public abstract class RpgExperienceListener extends RPGListener {

    protected final String skillName;
    private final float difficulty;
    private final Plugin plugin;

    private final HashMap<Player, SkillXpBarData> xpBarCache = new HashMap<>();

    /**
     * @param playerManager   to manage rpg players
     * @param databaseService to use for database storage
     * @param skillName       name of the skill for presentation purposes
     * @param difficulty      difficulty modifier that is multiplied at the end of the
     *                        base experience formula that adds or removes
     *                        difficulty from this experience curve
     *                        0-.99 = Easier
     *                        1+ = Harder
     */
    @Inject
    public RpgExperienceListener(
            Plugin plugin,
            PlayerManager playerManager,
            IDatabaseService databaseService,
            String skillName,
            float difficulty
    ) {
        super(playerManager, databaseService);
        this.plugin = plugin;
        this.skillName = skillName;
        this.difficulty = difficulty;
    }

    /**
     * Create an RPG experience listener with a normal difficulty
     *
     * @param playerManager   to access rpg players
     * @param databaseService to mess with the database
     * @param skillName       name of the skill for presentation purposes
     */
    public RpgExperienceListener(Plugin plugin, PlayerManager playerManager, IDatabaseService databaseService, String skillName) {
        this(plugin, playerManager, databaseService, skillName, 1);
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
        final var adjustedCur = cur - calculateXpNeeded(level - 1);
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
}
