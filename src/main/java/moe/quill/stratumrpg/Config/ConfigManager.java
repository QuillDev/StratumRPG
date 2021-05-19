package moe.quill.stratumrpg.Config;

import moe.quill.stratumrpg.StratumRPG;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager {

    final FileConfiguration config;

    public ConfigManager(Plugin plugin) {
        this.config = plugin.getConfig();
    }

    public String getDatabaseName() {
        return config.getString("dbConfig.name");
    }
}
