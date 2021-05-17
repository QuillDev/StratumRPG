package moe.quill.stratumrpg;

import moe.quill.stratumrpg.Config.ConfigManager;
import moe.quill.stratumrpg.Database.DatabaseManager;
import moe.quill.stratumrpg.Events.HandleStratumPlayerList;
import moe.quill.stratumrpg.Events.MiningEvents.MiningDropEvent;
import moe.quill.stratumrpg.Events.MiningEvents.MiningExperienceEvent;
import moe.quill.stratumrpg.Players.StratumPlayerManager;
import moe.quill.stratumrpg.Scheduler.StratumScheduler;
import org.bukkit.plugin.java.JavaPlugin;

public final class StratumRPG extends JavaPlugin {

    private final ConfigManager configManager = new ConfigManager(this);
    private final DatabaseManager databaseManager = new DatabaseManager(configManager);
    private final StratumPlayerManager stratumPlayerManager = new StratumPlayerManager(databaseManager);
    private final StratumScheduler stratumScheduler = new StratumScheduler(stratumPlayerManager);

    @Override
    public void onEnable() {
        stratumPlayerManager.enable();
        stratumScheduler.enable();
        final var pluginManager = getServer().getPluginManager();

        //Database Events
        pluginManager.registerEvents(new HandleStratumPlayerList(stratumPlayerManager), this);

        //Mining Events
        pluginManager.registerEvents(new MiningExperienceEvent(stratumPlayerManager), this);
        pluginManager.registerEvents(new MiningDropEvent(stratumPlayerManager), this);
    }

    @Override
    public void onDisable() {
        //Disable the player manager
        stratumPlayerManager.disable();
        stratumScheduler.disable();
    }
}
