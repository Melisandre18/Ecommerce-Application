package com.ecommerce.redis;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
class ProductRepository {
    private final String REDIS_KEY = "products";

    private final RedisTemplate<String, Product> redisTemplate;

    @Autowired
    public ProductRepository(RedisTemplate<String, Product> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(Product product) {
        redisTemplate.opsForHash().put(REDIS_KEY, product.getId(), product);
    }

    public Product findById(String id) {
        return (Product) redisTemplate.opsForHash().get(REDIS_KEY, id);
    }


    public void purchase(String id, int quantity, Integer price) {
        Product product = findById(id);
        if (product != null) {
            int newBalance = product.getquantity() + quantity;
            product.setquantity(newBalance);
            product.getPurchaseHistory().add(price);
            save(product);
        }
    }

    public void order(String id, int quantity) {
        Product product = findById(id);
        if (product != null) {
            int newBalance = product.getquantity() - quantity;
            if (newBalance < 0) {
                throw new IllegalArgumentException("Not enough stock available");
            }
            product.setquantity(newBalance);
            product.getOrderHistory().add(product.getPrice()* product.getquantity());
            save(product);
        }
    }

    public int getQuantity(String id) {
        Product product = findById(id);
        if (product != null) {
            return product.getquantity();
        }
        throw new IllegalArgumentException("Product with id " + id + " doesn't exist");
    }

    public double getAveragePrice(String id) {
        Product product = findById(id);
        if (product != null) {
            List<Integer> purchaseHistory = product.getPurchaseHistory();
            if (purchaseHistory.isEmpty()) {
                return 0;
            }
            double sum = 0;
            for (int price : purchaseHistory) {
                sum += price;
            }
            return sum / purchaseHistory.size();
        }
        throw new IllegalArgumentException("Product with id " + id + " doesn't exist");
    }


    public double getProductProfit(String id) {
        Product product = findById(id);
        if (product == null) {
            throw new IllegalArgumentException("Product with id " + id + " doesn't exist");
        }
        List<Integer> purchaseHistory = product.getPurchaseHistory();
        List<Integer> orderHistory = product.getOrderHistory();
        if (purchaseHistory.isEmpty() || orderHistory.isEmpty()) {
            return 0;
        }
        double averagePurchasePrice = purchaseHistory.stream().mapToInt(Integer::intValue).average().orElse(0);
        double averageOrderPrice = orderHistory.stream().mapToInt(Integer::intValue).average().orElse(0);
        double profitPerUnit = averageOrderPrice - averagePurchasePrice;
        int totalQuantitySold = orderHistory.stream().mapToInt(Integer::intValue).sum() / product.getPrice();
        return profitPerUnit * totalQuantitySold;
    }

    public String getFewestProduct() {
        String productId = null;
        int minQuantity = Integer.MAX_VALUE;
        Set<Object> productIdsObject = redisTemplate.opsForHash().keys(REDIS_KEY);
        Set<String> productIds = productIdsObject.stream()
                .map(Object::toString)
                .collect(Collectors.toSet());
        for (String id : productIds) {
            Product product = findById(id);
            if (product.getquantity() < minQuantity) {
                productId = id;
                minQuantity = product.getquantity();
            }
        }
        if (productId == null) {
            throw new RuntimeException("No products found");
        }
        return findById(productId).getName();
    }


    public String getMostPopularProduct() {
        Set<Object> productIdsObject = redisTemplate.opsForHash().keys(REDIS_KEY);
        Set<String> productIds = productIdsObject.stream()
                .map(Object::toString)
                .collect(Collectors.toSet());
        String mostPopularProductId = null;
        int mostOrders = 0;
        for (String id : productIds) {
            Product product = findById(id);
            List<Integer> orderHistory = product.getOrderHistory();
            int numOrders = orderHistory.size();
            if (numOrders > mostOrders) {
                mostOrders = numOrders;
                mostPopularProductId = product.getId();
            }
        }
        if (mostPopularProductId != null) {
            return findById(mostPopularProductId).getName();
        }
        throw new IllegalStateException("No products found");
    }

}



