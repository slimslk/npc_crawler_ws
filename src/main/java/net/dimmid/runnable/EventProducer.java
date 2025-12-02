package net.dimmid.runnable;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.dimmid.config.Config;
import net.dimmid.entity.PlayerEvent;
import net.dimmid.kafka.GameKafkaProducer;
import net.dimmid.util.JsonUtil;

import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class EventProducer implements Runnable {

    private final GameKafkaProducer producer;
    private final BlockingQueue<PlayerEvent> eventInputQueue;
    private final String PLAYER_EVENT_TOPIC;
    private final int DEFAULT_QUEUE_TIMEOUT;

    public EventProducer(GameKafkaProducer producer,
                         BlockingQueue<PlayerEvent> eventInputQueue) throws FileNotFoundException {
        this.producer = producer;
        this.eventInputQueue = eventInputQueue;
        this.PLAYER_EVENT_TOPIC = Config.getOrDefault(
                "PLAYER_EVENT_TOPIC", "player-event");
        this.DEFAULT_QUEUE_TIMEOUT = Integer.parseInt(
                Config.getOrDefault("GAME_ROUND_TIME","250")
        );

    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Optional<PlayerEvent> event = getEvent();
                if (event.isPresent()) {
                    sendEvent(event.get());
                }
            } catch ( JsonProcessingException | InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
        producer.close();

    }

    private Optional<PlayerEvent> getEvent() throws InterruptedException, JsonProcessingException {
        PlayerEvent event = eventInputQueue.poll(DEFAULT_QUEUE_TIMEOUT, TimeUnit.MILLISECONDS);
        if (event == null) {
            return Optional.empty();
        }
        return Optional.of(event);
    }

    private void sendEvent(PlayerEvent event) throws JsonProcessingException {
        var key = event.userId();
        var value = convertValueToJson(event);
        producer.produce(PLAYER_EVENT_TOPIC, key, value);
    }

    private String convertValueToJson(PlayerEvent event) throws JsonProcessingException {
        return JsonUtil.playerEventToJson(event);
    }
}
