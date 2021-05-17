package moe.quill.stratumrpg.Players;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.entity.Player;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class StratumPlayer {

    private final Player player;

    private float miningXP;
    private int miningLevel;

    public StratumPlayer(Player player, Document document) {
        this.player = player;

        //Jobs Experience
        //Mining Information
        this.miningXP = document.get(PlayerDataKey.MINING_XP.name(), 0f);
        this.miningLevel = document.getInteger(PlayerDataKey.MINING_LEVEL.name(), 1);

        //Hunting Information
    }

    /**
     * Get the mc player from the stratum player
     *
     * @return the mc player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the BSON object for updating the fields in the database
     *
     * @return the resulting update bson
     */
    public Bson getUpdateBSON() {
        return
                combine(
                        //Mining Jobs Info
                        set(PlayerDataKey.MINING_XP.name(), miningXP),
                        set(PlayerDataKey.MINING_LEVEL.name(), miningLevel)
                );
    }

    // FIELDS GETTERS AND SETTERS

    public float getMiningXP() {
        return miningXP;
    }

    public int getMiningLevel() {
        return miningLevel;
    }

    public void setMiningLevel(int miningLevel) {
        this.miningLevel = miningLevel;
    }

    public void setMiningXP(float miningXP) {
        this.miningXP = miningXP;
    }
}
