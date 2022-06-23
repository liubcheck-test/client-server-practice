package stepanenko.homework2.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import stepanenko.practice1.exception.StoreException;

public class Store {
    private static final Map<Integer, String> groups = new HashMap<>();
    private static final Map <Integer, String> products = new HashMap<>();
    private static final Map<Integer, List<String>> groupsProducts = new HashMap<>();
    private static final Map<Integer, Integer> productsAmount = new HashMap<>();
    private static final Map<Integer, Integer> productsPrice = new HashMap<>();
    private static int groupId = 0, productId = 0;

    public static synchronized int getProductAmount(int productId) {
        if (!productsAmount.containsKey(productId)) {
            throw new StoreException("Can't find product with id " + productId);
        }
        return productsAmount.get(productId);
    }

    public static synchronized void updateProductAmount(int productId, int amount) {
        if (!productsAmount.containsKey(productId)) {
            throw new StoreException("Can't find product with id " + productId);
        }
        if (amount < 0) {
            throw new StoreException("The amount can't be negative number " + amount);
        }
        productsAmount.put(productId, amount);
    }

    public static synchronized void addProductGroup(String groupName) {
        if (groups.containsValue(groupName)) {
            throw new StoreException("There is already the group with such name");
        }
        groups.put(++groupId, groupName);
        groupsProducts.put(groupId, new ArrayList<>());
    }

    public static synchronized void addProductToGroup(int groupId, String productName) {
        if (!groups.containsKey(groupId)) {
            throw new StoreException("Can't find group with id " + groupId);
        }
        if (products.containsValue(productName)) {
            throw new StoreException("There is already the product with name " + productName);
        }
        products.put(++productId, productName);
        groupsProducts.get(groupId).add(productName);
        productsAmount.put(productId, 0);
        productsPrice.put(productId, 0);
    }

    public static synchronized void setProductPrice(int productId, int price) {
        if (!products.containsKey(productId)) {
            throw new StoreException("Can't find product with id " + productId);
        }
        if (price <= 0) {
            throw new StoreException("Price can't be less than 1, your price is " + price);
        }
        productsPrice.put(productId, price);
    }

    public static synchronized Map<Integer, List<String>> getGroupsProducts() {
        return groupsProducts;
    }
}
