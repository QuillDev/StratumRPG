package moe.quill.stratumrpg;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import moe.quill.stratumcommonutils.Plugin.Configuration.StratumConfigBuilder;
import moe.quill.stratumcommonutils.Plugin.StratumPlugin;
import moe.quill.stratumrpg.DependencyInjection.PluginModule;
import moe.quill.stratumrpg.Skills.Experience.Events.LoggingExperienceListener;
import moe.quill.stratumrpg.Skills.Experience.Events.MiningExperienceListener;
import moe.quill.stratumrpg.Players.Events.PlayerJoinListener;
import moe.quill.stratumrpg.Players.Events.PlayerQuitListener;
import moe.quill.stratumrpg.Players.PlayerManager;

@Singleton
public final class StratumRPG extends StratumPlugin {

    //Managers
    @Inject
    PlayerManager playerManager;

    //Events
    @Inject
    PlayerJoinListener playerJoinListener;
    @Inject
    PlayerQuitListener playerQuitEvent;
    @Inject
    MiningExperienceListener miningExperienceListener;
    @Inject
    LoggingExperienceListener loggingExperienceListener;


    public StratumRPG() {
        super(
                new StratumConfigBuilder()
                        .setUseDatabase(true)
                        .setUseSerialization(true)
                        .setUseKeyManager(true)
                        .build()
        );
    }

    @Override
    public void onEnable() {
        super.onEnable();
        final var injector = Guice.createInjector(new PluginModule(this));
        injector.injectMembers(this);

        registerEvent(playerJoinListener, playerQuitEvent, miningExperienceListener, loggingExperienceListener);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (playerManager != null) {
            playerManager.savePlayers();
            playerManager.clearPlayerCache();
        }

    }
}
