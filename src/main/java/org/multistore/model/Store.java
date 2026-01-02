package org.multistore.model;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class Store {

    private final EnumMap<Product, Integer> inventory = new EnumMap<>(Product.class);

    public Store(Map<Product, Integer> initialStock) {
        Objects.requireNonNull(initialStock, "initialStock must not be null");
        for (Product p : Product.values()) {
            inventory.put(p, Math.max(0, initialStock.getOrDefault(p, 0)));
        }
    }

    public synchronized EnumMap<Product, Integer> snapshotInventory() {
        return new EnumMap<>(inventory);
    }

    public Receipt sell(Buyer buyer) {
        Objects.requireNonNull(buyer, "buyer must not be null");
        return sell(buyer.getId(), buyer.getName(), buyer.getCart());
    }

    public Receipt sell(String buyerId, String buyerName, Cart cart) {
        Objects.requireNonNull(cart, "cart must not be null");

        // Make "requested" stable for the receipt (copy)
        EnumMap<Product, Integer> requested = new EnumMap<>(Product.class);
        requested.putAll(cart.getItemsView());

        BigDecimal requestedTotal = calculateTotal(requested);

        EnumMap<Product, Integer> bought = new EnumMap<>(Product.class);
        EnumMap<Product, Integer> missing = new EnumMap<>(Product.class);

        BigDecimal paidTotal;

        synchronized (this) {
            for (Map.Entry<Product, Integer> e : requested.entrySet()) {
                Product product = e.getKey();
                int reqQty = e.getValue();

                int available = inventory.getOrDefault(product, 0);
                int soldQty = Math.min(available, reqQty);

                // decrement inventory
                inventory.put(product, available - soldQty);

                if (soldQty > 0) bought.put(product, soldQty);
                if (soldQty < reqQty) missing.put(product, reqQty - soldQty);
            }
            paidTotal = calculateTotal(bought);
        }

        return new Receipt(
                buyerId,
                buyerName,
                requested,
                bought,
                missing,
                requestedTotal,
                paidTotal
        );
    }

    private static BigDecimal calculateTotal(Map<Product, Integer> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<Product, Integer> e : items.entrySet()) {
            BigDecimal line = e.getKey().getUnitPrice().multiply(BigDecimal.valueOf(e.getValue()));
            total = total.add(line);
        }
        return total;
    }
}

