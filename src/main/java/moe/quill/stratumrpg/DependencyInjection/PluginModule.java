package moe.quill.stratumrpg.DependencyInjection;

import com.google.inject.AbstractModule;
import moe.quill.StratumCommonApi.Database.IDatabaseService;
import moe.quill.StratumCommonApi.KeyManager.IKeyManager;
import moe.quill.StratumCommonApi.Serialization.ISerializer;
import moe.quill.stratumrpg.Players.PlayerManager;
import moe.quill.stratumrpg.StratumRPG;
import org.bukkit.plugin.Plugin;

public class PluginModule extends AbstractModule {

    private final Plugin plugin;
    private final ISerializer serializer;
    private final IDatabaseService databaseService;
    private final IKeyManager keyManager;
    private final PlayerManager playerManager;

    public PluginModule(StratumRPG plugin) {
        this.plugin = plugin;
        this.serializer = plugin.getSerializer();
        this.databaseService = plugin.getDatabaseService();
        this.keyManager = plugin.getKeyManager();
        this.playerManager = new PlayerManager(databaseService);
    }

    @Override
    protected void configure() {
        bind(Plugin.class).toInstance(plugin);
        bind(ISerializer.class).toInstance(serializer);
        bind(PlayerManager.class).toInstance(playerManager);
        bind(IDatabaseService.class).toInstance(databaseService);
        bind(IKeyManager.class).toInstance(keyManager);
    }
}
