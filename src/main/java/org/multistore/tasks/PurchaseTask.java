package org.multistore.tasks;

import org.multistore.model.*;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

public class PurchaseTask implements Callable<Receipt> {

    private final Store store;
    private final Buyer buyer;

    public PurchaseTask(Store store, Buyer buyer) {
        this.store = Objects.requireNonNull(store, "store must not be null");
        this.buyer = Objects.requireNonNull(buyer, "buyer must not be null");
    }

    @Override
    public Receipt call() throws Exception {
        // Small delay to make concurrency visible
        Thread.sleep(ThreadLocalRandom.current().nextInt(50, 250));
        return store.sell(buyer);
    }
}
