package net.dimmid.runnable.factory_impl;

import net.dimmid.config.Config;
import net.dimmid.consumer.GameConsumer;
import net.dimmid.consumer.GameUpdatesConsumerImpl;
import net.dimmid.kafka.GameKafkaConsumer;
import net.dimmid.runnable.UpdatesFactory;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class GameUpdates extends UpdatesFactory {
    private final BlockingQueue<Map<String, String>> gameUpdates;
    private final GameKafkaConsumer kafkaConsumer;

    public GameUpdates(BlockingQueue<Map<String, String>> gameUpdates) throws FileNotFoundException {
        this.gameUpdates = gameUpdates;
        this.kafkaConsumer = new GameKafkaConsumer(
                List.of(Config.getOrDefault("GAME_UPDATES_TOPIC", "game")),
                "game_updates",
                "ws_game_update_consumer"
        );
    }

    @Override
    public GameConsumer createGameConsumerService() {
        return new GameUpdatesConsumerImpl(kafkaConsumer, gameUpdates);
    }
}
