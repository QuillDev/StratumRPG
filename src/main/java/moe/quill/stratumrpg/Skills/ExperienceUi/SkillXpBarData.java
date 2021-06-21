package moe.quill.stratumrpg.Skills.ExperienceUi;

import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class SkillXpBarData {

    private final BossBar bossBar;
    private final Player player;
    private final Plugin plugin;
    private final BukkitScheduler scheduler;
    private BukkitTask autoCloseTaskId = null;

    public SkillXpBarData(Plugin plugin, Player player, BossBar bossBar) {
        this.bossBar = bossBar;
        this.plugin = plugin;
        this.scheduler = Bukkit.getScheduler();
        this.player = player;
    }

    public void show() {
        if (player == null) {
            return;
        }

        bossBar.addPlayer(player);
        this.autoCloseTaskId = scheduleAutoClose();
    }

    public @NotNull BukkitTask scheduleAutoClose() {
        //Cancel the auto close task currently
        if (autoCloseTaskId != null && !autoCloseTaskId.isCancelled()) {
            autoCloseTaskId.cancel();
        }

        return scheduler.runTaskLaterAsynchronously(plugin, () -> {
            bossBar.removePlayer(player);
        }, 3 * 20);
    }

    public void hide() {
        if (player == null) {
            return;
        }
        bossBar.removePlayer(player);
    }

    public void update(float cur, float max) {
        if (Float.isNaN(cur)) cur = 0; //If it's a nan set cur to 0
        bossBar.setProgress(cur / max);
    }
}
