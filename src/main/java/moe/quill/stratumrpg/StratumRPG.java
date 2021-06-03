package moe.quill.stratumrpg;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import moe.quill.StratumCommon.Plugin.StratumConfigBuilder;
import moe.quill.StratumCommon.Plugin.StratumPlugin;
import moe.quill.stratumrpg.DependencyInjection.PluginModule;
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

        registerEvent(playerJoinListener, playerQuitEvent);
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
