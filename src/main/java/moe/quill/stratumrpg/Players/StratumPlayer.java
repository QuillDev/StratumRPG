package moe.quill.stratumrpg.Players;

import org.bukkit.entity.Player;

public class StratumPlayer {

    private final Player player;

    public StratumPlayer(Player player) {
        this.player = player;
    }

    /**
     * Get the mc player from the stratum player
     *
     * @return the mc player
     */
    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StratumPlayer that = (StratumPlayer) o;
        return getPlayer().getUniqueId().equals(that.getPlayer().getUniqueId());
    }
}
