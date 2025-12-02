package net.dimmid.manager;

import net.dimmid.runnable.PlayerEventProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PlayerEventProcessorThreadManager {
    private final int AMOUNT_OF_THREADS = 4;

    private final ExecutorService executor;
    private final PlayerEventProcessor playerEventProcessor;

    public PlayerEventProcessorThreadManager(PlayerEventProcessor playerEventProcessor) {
        this.playerEventProcessor = playerEventProcessor;
        this.executor = Executors.newFixedThreadPool(AMOUNT_OF_THREADS);
    }

    public void start() {
        for (int i = 0; i < AMOUNT_OF_THREADS; i++) {
           executor.submit(playerEventProcessor);
        }
    }

    public void stop() {
        executor.shutdownNow();
    }
}