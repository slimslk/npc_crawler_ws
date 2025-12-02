package net.dimmid.runnable.factory_impl;

import net.dimmid.config.Config;
import net.dimmid.consumer.PlayerUpdatesConsumerImpl;
import net.dimmid.kafka.GameKafkaConsumer;
import net.dimmid.consumer.GameConsumer;
import net.dimmid.runnable.UpdatesFactory;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class PlayerUpdates extends UpdatesFactory {
    private final BlockingQueue<Map<String, String>> playerUpdates;
    private final GameKafkaConsumer kafkaConsumer;

    public PlayerUpdates(BlockingQueue<Map<String, String>> playerUpdatesQueue) throws FileNotFoundException {
        this.playerUpdates = playerUpdatesQueue;
        this.kafkaConsumer = new GameKafkaConsumer(
                List.of(Config.getOrDefault("PLAYER_UPDATES_TOPIC", "player")),
                        "player_updates",
                        "ws_player_update_consumer"
        );
    }

    @Override
    public GameConsumer createGameConsumerService() {
        return new PlayerUpdatesConsumerImpl(kafkaConsumer, playerUpdates);
    }
}
