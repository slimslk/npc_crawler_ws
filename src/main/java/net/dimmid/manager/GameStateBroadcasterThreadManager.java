package net.dimmid.manager;

import net.dimmid.config.Config;
import net.dimmid.runnable.GameStateBroadcaster;

import java.io.FileNotFoundException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameStateBroadcasterThreadManager {
    private final int update_period;
    private final ScheduledExecutorService executor;
    private final GameStateBroadcaster broadcaster;


    public GameStateBroadcasterThreadManager(GameStateBroadcaster broadcaster) throws FileNotFoundException {
        this.executor = Executors.newScheduledThreadPool(1);
        this.broadcaster = broadcaster;
        this.update_period = Integer.parseInt(
                Config.getOrDefault("GAME_ROUND_TIME", "250"));
    }

    public void start() {
        executor.scheduleAtFixedRate(broadcaster, 0, update_period, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        executor.shutdown();
    }
}
