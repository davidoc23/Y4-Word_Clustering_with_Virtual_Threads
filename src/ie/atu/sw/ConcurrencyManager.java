package ie.atu.sw;

import java.util.concurrent.*;

public class ConcurrencyManager {
    public void processWithVirtualThreads(Runnable task) {
        Thread.ofVirtual().start(task);
    }
}

