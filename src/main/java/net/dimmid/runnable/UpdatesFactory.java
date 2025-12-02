package net.dimmid.runnable;

import net.dimmid.consumer.GameConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

public abstract class UpdatesFactory implements Runnable{
    @Override
    public void run() {
        GameConsumer service = createGameConsumerService();
        while (!Thread.currentThread().isInterrupted()) {
            ConsumerRecords<String, String> records = service.poll_records();
            for (ConsumerRecord<String, String> record : records) {
                service.processRecord(record);
            }
        }
        service.stop();
    }

    public abstract GameConsumer createGameConsumerService();
}
