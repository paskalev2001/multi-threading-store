package org.multistore.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class Receipt {

    private final String buyerId;
    private final String buyerName;

    private final EnumMap<Product, Integer> requested;
    private final EnumMap<Product, Integer> bought;
    private final EnumMap<Product, Integer> missing;

    private final BigDecimal requestedTotal;
    private final BigDecimal paidTotal;

    private final Instant createdAt;

    public Receipt(
            String buyerId,
            String buyerName,
            Map<Product, Integer> requested,
            Map<Product, Integer> bought,
            Map<Product, Integer> missing,
            BigDecimal requestedTotal,
            BigDecimal paidTotal
    ) {
        this.buyerId = Objects.requireNonNull(buyerId, "buyerId must not be null");
        this.buyerName = Objects.requireNonNull(buyerName, "buyerName must not be null");

        this.requested = new EnumMap<>(Product.class);
        this.requested.putAll(Objects.requireNonNull(requested, "requested must not be null"));

        this.bought = new EnumMap<>(Product.class);
        this.bought.putAll(Objects.requireNonNull(bought, "bought must not be null"));

        this.missing = new EnumMap<>(Product.class);
        this.missing.putAll(Objects.requireNonNull(missing, "missing must not be null"));

        this.requestedTotal = Objects.requireNonNull(requestedTotal, "requestedTotal must not be null");
        this.paidTotal = Objects.requireNonNull(paidTotal, "paidTotal must not be null");

        this.createdAt = Instant.now();
    }

    public String getBuyerId() {
        return buyerId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public Map<Product, Integer> getRequested() {
        return Collections.unmodifiableMap(requested);
    }

    public Map<Product, Integer> getBought() {
        return Collections.unmodifiableMap(bought);
    }

    public Map<Product, Integer> getMissing() {
        return Collections.unmodifiableMap(missing);
    }

    public BigDecimal getRequestedTotal() {
        return requestedTotal;
    }

    public BigDecimal getPaidTotal() {
        return paidTotal;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public boolean isFullySatisfied() {
        return missing.isEmpty();
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "buyer='" + buyerName + '\'' +
                ", requested=" + requested +
                ", bought=" + bought +
                ", missing=" + missing +
                ", requestedTotal=" + requestedTotal +
                ", paidTotal=" + paidTotal +
                ", createdAt=" + createdAt +
                '}';
    }
}
