package org.multistore;

import org.multistore.config.*;
import org.multistore.model.*;
import org.multistore.tasks.*;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws Exception {
        Store store = DataFactory.defaultStore();
        List<Buyer> buyers = DataFactory.createBuyers(AppConfig.BUYERS);

        System.out.println("Initial inventory: " + store.snapshotInventory());

        ExecutorService pool = Executors.newFixedThreadPool(AppConfig.THREADS);

        List<Callable<Receipt>> tasks = new ArrayList<>();
        for (Buyer b : buyers) {
            tasks.add(new PurchaseTask(store, b));
        }

        try {
            List<Future<Receipt>> results = pool.invokeAll(tasks);

            System.out.println("\n--- Receipts ---");
            for (Future<Receipt> f : results) {
                Receipt r = f.get();
                System.out.println(r);
            }
        } finally {
            pool.shutdown();
            pool.awaitTermination(5, TimeUnit.SECONDS);
        }

        System.out.println("\nFinal inventory: " + store.snapshotInventory());
    }
}
