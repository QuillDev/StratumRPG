package moe.quill.stratumrpg.Players;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.entity.Player;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class StratumPlayer {

    private final Player player;

    //Life skill stats
    private double miningXP;
    private int miningLevel;
    private double fishingXP;
    private int fishingLevel;
    private double loggingXP;
    private int loggingLevel;

    public StratumPlayer(Player player, Document document) {
        this.player = player;

        //Jobs Experience
        //Mining Information
        this.miningXP = document.get(PlayerDataKey.MINING_XP.name(), 0.);
        this.miningLevel = document.getInteger(PlayerDataKey.MINING_LEVEL.name(), 1);

        //Fishing Information
        this.fishingXP = document.get(PlayerDataKey.FISHING_XP, 0);
        this.fishingLevel = document.get(PlayerDataKey.FISHING_LEVEL, 1);

        //Logging information
        this.loggingXP = document.get(PlayerDataKey.LOGGING_XP, 0);
        this.loggingLevel = document.get(PlayerDataKey.LOGGING_LEVEL, 1);
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
                        set(PlayerDataKey.MINING_LEVEL.name(), miningLevel),
                        //Fishing Jobs Info
                        set(PlayerDataKey.FISHING_XP.name(), fishingXP),
                        set(PlayerDataKey.FISHING_LEVEL.name(), fishingLevel),
                        //Logging Jobs Info
                        set(PlayerDataKey.LOGGING_XP.name(), loggingXP),
                        set(PlayerDataKey.LOGGING_LEVEL.name(), loggingLevel)
                );
    }

    // FIELDS GETTERS AND SETTERS

    public double getMiningXP() {
        return miningXP;
    }

    public int getMiningLevel() {
        return miningLevel;
    }

    public void setMiningLevel(int miningLevel) {
        this.miningLevel = miningLevel;
    }

    public void setMiningXP(double miningXP) {
        this.miningXP = miningXP;
    }

    public void setFishingLevel(int fishingLevel) {
        this.fishingLevel = fishingLevel;
    }

    public void setFishingXP(double fishingXP) {
        this.fishingXP = fishingXP;
    }

    public void setLoggingLevel(int loggingLevel) {
        this.loggingLevel = loggingLevel;
    }

    public void setLoggingXP(double loggingXP) {
        this.loggingXP = loggingXP;
    }

    public double getFishingXP() {
        return fishingXP;
    }

    public int getFishingLevel() {
        return fishingLevel;
    }

    public double getLoggingXP() {
        return loggingXP;
    }

    public int getLoggingLevel() {
        return loggingLevel;
    }
}
