package org.multistore.model;

import java.math.BigDecimal;
import java.util.Arrays;

public enum Product {
    BREAD("P1", "Bread", new BigDecimal("1.60")),
    MILK("P2", "Milk", new BigDecimal("2.40")),
    CHEESE("P3", "Cheese", new BigDecimal("8.90")),
    APPLE("P4", "Oranges", new BigDecimal("3.20")),
    CHOCOLATE("P5", "Candy", new BigDecimal("2.10"));

    private final String code;
    private final String displayName;
    private final BigDecimal unitPrice;

    Product(String code, String displayName, BigDecimal unitPrice) {
        this.code = code;
        this.displayName = displayName;
        this.unitPrice = unitPrice;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public static Product fromCode(String code) {
        return Arrays.stream(values())
                .filter(p -> p.code.equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown product code: " + code));
    }

    @Override
    public String toString() {
        return displayName + " (" + code + "), price: " + unitPrice;
    }
}
