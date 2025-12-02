package net.dimmid.runnable;

import net.dimmid.service.IGameStateService;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class PlayerEventProcessor implements Runnable{
    private final BlockingQueue<Map<String, String>> playerInfoQueue;
    private final BlockingQueue<Map<String, String>> locationUpdatesQueue;
    private final BlockingQueue<Map<String, String>> gameUpdatesQueue;
    private final IGameStateService gameStateService;

    public PlayerEventProcessor(BlockingQueue<Map<String, String>> playerInfoQueue,
                                BlockingQueue<Map<String, String>> locationUpdatesQueue,
                                BlockingQueue<Map<String, String>> gameUpdatesQueue,
                                IGameStateService gameStatService) {
        this.playerInfoQueue = playerInfoQueue;
        this.locationUpdatesQueue = locationUpdatesQueue;
        this.gameUpdatesQueue = gameUpdatesQueue;
        this.gameStateService = gameStatService;

    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Map<String, String> playerInfo = playerInfoQueue.poll(20, TimeUnit.MILLISECONDS);
                if (playerInfo != null) {
                    gameStateService.updatePlayersData(playerInfo.get("userId"), playerInfo.get("value"));
                }

                Map<String, String> locationUpdates = locationUpdatesQueue.poll(20, TimeUnit.MILLISECONDS);
                if (locationUpdates != null) {
                    gameStateService.updateLocation(locationUpdates.get("locationId"), locationUpdates.get("value"));
                }

                Map<String, String> gameUpdates = gameUpdatesQueue.poll(20, TimeUnit.MILLISECONDS);
                if (gameUpdates != null) {
                    gameStateService.updateGameData(gameUpdates.get("userId"), gameUpdates.get("value"));
                }
            }
            catch (InterruptedException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
}

