package net.dimmid.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

public interface GameConsumer {
    ConsumerRecords<String, String> poll_records();
    void processRecord(ConsumerRecord<String, String> record);
    void stop();
}
