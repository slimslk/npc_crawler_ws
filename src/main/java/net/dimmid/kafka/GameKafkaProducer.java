package net.dimmid.kafka;

import net.dimmid.config.Config;
import net.dimmid.config.KafkaConfiguration;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.FileNotFoundException;
import java.util.Properties;

public class GameKafkaProducer {
    private final Producer<String, String> producer;

    public GameKafkaProducer() throws FileNotFoundException {
        Properties properties = KafkaConfiguration.getKafkaProperties();
        properties.put(ProducerConfig.ACKS_CONFIG, Config.getOrDefault("ACKS", "all"));
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "ws_kafka_producer");
        this.producer = new KafkaProducer<>(properties);

    }

    public void produce(String topic, String key, String message) {
        ProducerRecord<String, String> record = new ProducerRecord<> (topic, key, message);
        producer.send(record);
    }

    public void close() {
        producer.close();
    }
}