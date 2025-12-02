package net.dimmid.kafka;

import net.dimmid.config.Config;
import net.dimmid.config.KafkaConfiguration;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Properties;

public class GameKafkaConsumer {
    private final KafkaConsumer<String, String> consumer;


    public GameKafkaConsumer(List<String> topics, String consumer_group_id, String client_id) throws FileNotFoundException {
        Properties properties = KafkaConfiguration.getKafkaProperties();
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, consumer_group_id);
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, client_id);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,
                Config.getOrDefault("SESSION_TIMEOUT_MS", "45000"));

        this.consumer = new KafkaConsumer<>(properties);
        this.consumer.subscribe(topics);
    }

    public KafkaConsumer<String, String> getConsumer() {
        return consumer;
    }

    public void close() {
        consumer.close();
    }
}
