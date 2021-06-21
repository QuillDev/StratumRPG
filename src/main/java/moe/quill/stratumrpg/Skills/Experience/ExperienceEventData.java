package moe.quill.stratumrpg.Skills.Experience;

import moe.quill.StratumCommonApi.Database.DataTypes.RPGPlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashSet;

public record ExperienceEventData(Player player,
                                  RPGPlayer rpgPlayer,
                                  BlockBreakEvent event,
                                  Block block,
                                  HashSet<Material> whiteList) {

    public RPGPlayer getRpgPlayer() {
        return rpgPlayer;
    }
    public BlockBreakEvent getEvent() {
        return event;
    }
    public Player getPlayer() {
        return player;
    }
    public Block getBlock() {return block;}
    public HashSet<Material> getWhiteList() {
        return whiteList;
    }
}
