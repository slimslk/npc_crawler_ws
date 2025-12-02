package net.dimmid.runnable.factory_impl;

import net.dimmid.config.Config;
import net.dimmid.kafka.GameKafkaConsumer;
import net.dimmid.consumer.GameConsumer;
import net.dimmid.consumer.LocationUpdatesConsumerImpl;
import net.dimmid.runnable.UpdatesFactory;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class LocationUpdates extends UpdatesFactory {
    private final BlockingQueue<Map<String, String>> locationUpdates;
    private final GameKafkaConsumer kafkaConsumer;

    public LocationUpdates(BlockingQueue<Map<String, String>> locationUpdatesQueue) throws FileNotFoundException {
        this.locationUpdates = locationUpdatesQueue;
        this.kafkaConsumer = new GameKafkaConsumer(
                List.of(Config.getOrDefault("LOCATION_UPDATES_TOPIC", "location")),
                "location_updates",
                "ws_location_update_consumer"
        );
    }

    @Override
    public GameConsumer createGameConsumerService() {
        return new LocationUpdatesConsumerImpl(kafkaConsumer, locationUpdates);
    }
}
