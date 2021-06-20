package moe.quill.stratumrpg;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import moe.quill.stratumcommonutils.Plugin.Configuration.StratumConfigBuilder;
import moe.quill.stratumcommonutils.Plugin.StratumPlugin;
import moe.quill.stratumrpg.DependencyInjection.PluginModule;
import moe.quill.stratumrpg.Events.ExperienceEvents.MiningExperienceListener;
import moe.quill.stratumrpg.Events.PlayerJoinListener;
import moe.quill.stratumrpg.Events.PlayerQuitListener;
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

        registerEvent(playerJoinListener, playerQuitEvent, miningExperienceListener);
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
