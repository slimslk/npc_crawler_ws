package net.dimmid.manager;

import net.dimmid.runnable.EventProducer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventProducerThreadManager {
    private final ExecutorService executor;
    private final EventProducer producer;

    public EventProducerThreadManager(EventProducer producer) {
        this.executor = Executors.newSingleThreadExecutor();
        this.producer = producer;
    }

    public void start() {
        executor.submit(producer);
    }

    public void stop() {
        executor.shutdownNow();
    }
}
