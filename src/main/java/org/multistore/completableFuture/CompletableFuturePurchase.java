package org.multistore.completableFuture;

import org.multistore.config.AppConfig;
import org.multistore.config.DataFactory;
import org.multistore.model.Buyer;
import org.multistore.model.Receipt;
import org.multistore.model.Store;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class CompletableFuturePurchase {

    public static void main(String[] args) {
        Store store = DataFactory.defaultStore();
        List<Buyer> buyers = DataFactory.createBuyers(AppConfig.BUYERS);

        System.out.println("Initial inventory: " + store.snapshotInventory());

        ExecutorService executor = Executors.newFixedThreadPool(AppConfig.THREADS);

        try {
            List<CompletableFuture<Receipt>> futures = new ArrayList<>();

            for (Buyer buyer : buyers) {
                CompletableFuture<Receipt> cf = CompletableFuture.supplyAsync(() -> store.sell(buyer), executor)
                        .exceptionally(ex -> {
                            System.err.println("Error for " + buyer.getName() + ": " + ex.getMessage());
                            return new Receipt(
                                    buyer.getId(),
                                    buyer.getName(),
                                    buyer.getCart().getItemsView(),
                                    java.util.Map.of(),
                                    buyer.getCart().getItemsView(),
                                    buyer.getCart().totalPrice(),
                                    java.math.BigDecimal.ZERO
                            );
                        });

                futures.add(cf);
            }

            CompletableFuture<Void> allDone = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
            allDone.join();

            List<Receipt> receipts = futures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());

            System.out.println("\n RECEIPTS \n");
            receipts.forEach(System.out::println);

        } finally {
            executor.shutdown();
            try {
                executor.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("\n FINAL INVENTORY: \n" + store.snapshotInventory());
    }
}