package net.dimmid;

import net.dimmid.config.Config;
import net.dimmid.entity.PlayerEvent;
import net.dimmid.kafka.GameKafkaProducer;
import net.dimmid.manager.GameStateBroadcasterThreadManager;
import net.dimmid.runnable.EventProducer;
import net.dimmid.manager.EventProducerThreadManager;
import net.dimmid.runnable.GameStateBroadcaster;
import net.dimmid.runnable.PlayerEventProcessor;
import net.dimmid.runnable.factory_impl.GameUpdates;
import net.dimmid.runnable.factory_impl.LocationUpdates;
import net.dimmid.manager.PlayerEventProcessorThreadManager;
import net.dimmid.runnable.factory_impl.PlayerUpdates;
import net.dimmid.manager.UpdatesThreadManager;
import net.dimmid.service.GameStateServiceImpl;
import net.dimmid.service.IGameStateService;
import net.dimmid.ws.WebSocketServer;
import net.dimmid.ws.service.WebClientMessageService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    private static PlayerEventProcessorThreadManager playerEventProcessorThreadManager;
    private static EventProducerThreadManager eventProducerThreadManager;
    private static UpdatesThreadManager updatesThreadManager;
    private static GameStateBroadcasterThreadManager gameStateBroadcasterThreadManager;
    private static WebSocketServer server;


    public static void main(String[] args) {
        try {
            initialize();

            server = new WebSocketServer(Integer.parseInt(Config.getOrDefault("WS_PORT", "8080")));
            server.start();

            playerEventProcessorThreadManager.start();
            gameStateBroadcasterThreadManager.start();
            eventProducerThreadManager.start();
            updatesThreadManager.start();

            Thread.currentThread().join();

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            shutdown();
        }

    }

    private static void initialize() throws FileNotFoundException {

        BlockingQueue<PlayerEvent> eventInputQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Map<String, String>> playerOutputQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Map<String, String>> locationOutputQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Map<String, String>> gameUpdatesQueue = new LinkedBlockingQueue<>();

        IGameStateService gameStateService = new GameStateServiceImpl();
        WebClientMessageService webClientMessageService = new WebClientMessageService(eventInputQueue, gameStateService);
        PlayerEventProcessor processor = new PlayerEventProcessor(
                playerOutputQueue,
                locationOutputQueue,
                gameUpdatesQueue,
                gameStateService
        );
        playerEventProcessorThreadManager = new PlayerEventProcessorThreadManager(processor);

        GameKafkaProducer producer = new GameKafkaProducer();
        EventProducer eventProducer = new EventProducer(producer, eventInputQueue);
        eventProducerThreadManager = new EventProducerThreadManager(eventProducer);

        LocationUpdates locationUpdates = new LocationUpdates(locationOutputQueue);
        PlayerUpdates playerUpdatesServices = new PlayerUpdates(playerOutputQueue);
        GameUpdates gameUpdatesService = new GameUpdates(gameUpdatesQueue);
        updatesThreadManager = new UpdatesThreadManager(List.of(locationUpdates,
                playerUpdatesServices,
                gameUpdatesService));

        GameStateBroadcaster gameStateBroadcaster = new GameStateBroadcaster(gameStateService, webClientMessageService);
        gameStateBroadcasterThreadManager = new GameStateBroadcasterThreadManager(gameStateBroadcaster);

        MainAppContext.addObject("eventInputQueue", eventInputQueue);
        MainAppContext.addObject("playerOutputQueue", playerOutputQueue);
        MainAppContext.addObject("locationOutputQueue", locationOutputQueue);
        MainAppContext.addObject("webClientMessageService", webClientMessageService);


        MainAppContext.addObject("gameStateService", gameStateService);
        MainAppContext.addObject("gameStateBroadcaster", gameStateBroadcaster);

    }

    private static void shutdown() {
        if (playerEventProcessorThreadManager != null) {
            playerEventProcessorThreadManager.stop();
        }
        if (eventProducerThreadManager != null) {
            eventProducerThreadManager.stop();
        }
        if (updatesThreadManager != null) {
            updatesThreadManager.stop();
        }
        if (server != null) {
            server.stop();
        }
    }
}
