package org.multistore.model;

import java.util.Objects;
import java.util.UUID;

public class Buyer {

    private final String id;
    private final String name;
    private final Cart cart;

    public Buyer(String name, Cart cart) {
        this(UUID.randomUUID().toString(), name, cart);
    }

    public Buyer(String id, String name, Cart cart) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.cart = Objects.requireNonNull(cart, "cart must not be null");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Cart getCart() {
        return cart;
    }

    @Override
    public String toString() {
        return "Buyer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", cart=" + cart +
                '}';
    }
}