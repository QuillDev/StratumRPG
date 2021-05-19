package moe.quill.stratumrpg.Scheduler;

import moe.quill.stratumrpg.Players.StratumPlayerManager;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class StratumScheduler {

    private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
    private final StratumPlayerManager playerManager;

    public StratumScheduler(StratumPlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public void enable() {
        executorService.scheduleAtFixedRate(playerManager::saveAllOnlinePlayers, 5, 5, TimeUnit.MINUTES);
    }

    public void disable() {
        executorService.shutdownNow();

        try {
            final var terminated = executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
