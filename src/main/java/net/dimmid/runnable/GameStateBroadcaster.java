package net.dimmid.runnable;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.dimmid.service.IGameStateService;
import net.dimmid.ws.service.WebClientMessageService;

import java.util.Map;

public class GameStateBroadcaster implements Runnable {
    private final IGameStateService gameStateService;
    private final WebClientMessageService webClientMessageService;

    public GameStateBroadcaster(IGameStateService gameStateService,
                                WebClientMessageService webClientMessageService) {
        this.gameStateService = gameStateService;
        this.webClientMessageService = webClientMessageService;
    }

    @Override
    public void run() {
        Map<String, String> playersData;
        try {
            playersData = gameStateService.getUserMessages();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        try {
            broadcastData(playersData);
        } catch (JsonProcessingException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void broadcastData(Map<String, String> playersData) throws JsonProcessingException, InterruptedException {
        webClientMessageService.broadcastMessagesToClient(playersData);
    }

}
