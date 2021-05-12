package moe.quill.stratumrpg;

import moe.quill.stratumrpg.Events.HandleStratumPlayerList;
import moe.quill.stratumrpg.Players.StratumPlayerManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class StratumRPG extends JavaPlugin {

    private final static StratumPlayerManager stratumPlayerManager = new StratumPlayerManager();

    @Override
    public void onEnable() {
        //Enable the player manager
        stratumPlayerManager.enable();

        // Plugin startup logic
        final var pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new HandleStratumPlayerList(stratumPlayerManager), this);

    }

    @Override
    public void onDisable() {
        //Disable the player manager
        stratumPlayerManager.disable();
        // Plugin shutdown logic
    }
}
