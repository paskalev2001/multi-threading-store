package org.multistore.model;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class Cart {

    private final EnumMap<Product, Integer> items = new EnumMap<>(Product.class);

    public Cart add(Product product, int quantity) {
        Objects.requireNonNull(product, "product must not be null");
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be > 0");
        }
        items.merge(product, quantity, Integer::sum);
        return this;
    }

    public int getQuantity(Product product) {
        return items.getOrDefault(product, 0);
    }

    public Map<Product, Integer> getItemsView() {
        return Collections.unmodifiableMap(items);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public BigDecimal totalPrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<Product, Integer> e : items.entrySet()) {
            BigDecimal line = e.getKey().getUnitPrice().multiply(BigDecimal.valueOf(e.getValue()));
            total = total.add(line);
        }
        return total;
    }

    @Override
    public String toString() {
        return "Cart{" + "items=" + items + ", total=" + totalPrice() + '}';
    }
}
