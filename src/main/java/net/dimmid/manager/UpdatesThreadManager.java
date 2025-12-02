package net.dimmid.manager;

import net.dimmid.runnable.UpdatesFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdatesThreadManager {
    private final ExecutorService executor;
    private final List<UpdatesFactory> updatesFactories;


    public UpdatesThreadManager(List<UpdatesFactory> updatesFactories) {
        this.updatesFactories = updatesFactories;
        executor = Executors.newFixedThreadPool(updatesFactories.size());
    }

    public void start() {
        for (UpdatesFactory updatesFactory : updatesFactories) {
            executor.submit(updatesFactory);
        }
    }

    public void stop() {
        executor.shutdownNow();
    }
}
