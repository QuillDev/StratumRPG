package moe.quill.stratumrpg.Metadata;

import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class BlockMeta {

    public static MetadataValue blockPlacedMeta;


    public static void init(Plugin plugin) {
        blockPlacedMeta = new FixedMetadataValue(plugin, true);
    }
}
