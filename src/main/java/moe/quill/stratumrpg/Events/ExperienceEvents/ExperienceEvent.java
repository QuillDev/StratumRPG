package moe.quill.stratumrpg.Events.ExperienceEvents;

import moe.quill.stratumrpg.Players.StratumPlayerManager;
import org.bukkit.Material;
import org.bukkit.event.Listener;

import java.util.HashMap;

public abstract class ExperienceEvent implements Listener {

    protected StratumPlayerManager playerManager;
    protected final HashMap<Material, Float> xpMap;

    public ExperienceEvent(StratumPlayerManager playerManager, HashMap<Material, Float> xpMap) {
        this.playerManager = playerManager;
        this.xpMap = xpMap;
    }

    protected float getXpValue(Material material) {
        return xpMap.getOrDefault(material, 0f);
    }
}
