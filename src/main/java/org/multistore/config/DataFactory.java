package org.multistore.config;

import org.multistore.model.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public final class DataFactory {

    private DataFactory() {}

    public static Store defaultStore() {
        Map<Product, Integer> initial = Map.of(
                Product.BREAD, 30,
                Product.MILK, 20,
                Product.CHEESE, 15,
                Product.APPLE, 50,
                Product.CHOCOLATE, 25
        );
        return new Store(initial);
    }

    public static List<Buyer> createBuyers(int count) {
        List<Buyer> buyers = new ArrayList<>(count);
        for (int i = 1; i <= count; i++) {
            buyers.add(new Buyer("Buyer-" + i, randomCart(4, 10)));
        }
        return buyers;
    }

    /**
     * Random cart with up to maxDifferentProducts different products,
     * each with quantity 1 - maxQtyPerProduct
     */
    public static Cart randomCart(int maxDifferentProducts, int maxQtyPerProduct) {
        Cart cart = new Cart();

        List<Product> products = new ArrayList<>(Arrays.asList(Product.values()));
        Collections.shuffle(products);

        int different = ThreadLocalRandom.current().nextInt(1, maxDifferentProducts + 1);
        for (int i = 0; i < different; i++) {
            Product p = products.get(i);
            int qty = ThreadLocalRandom.current().nextInt(1, maxQtyPerProduct + 1);
            cart.add(p, qty);
        }
        return cart;
    }
}

