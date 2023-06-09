package com.ecommerce.redis;

import java.util.ArrayList;
import java.util.List;

public class Product {

    private String id;
    private String name;
    private Integer price;
    private Integer quantity;
    private List<Integer> purchaseHistory = new ArrayList<>();
    private List<Integer> orderHistory = new ArrayList<>();

    public Product(String id, String name, Integer price, Integer quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getquantity() {
        return quantity;
    }

    public void setquantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<Integer> getPurchaseHistory() {
        return purchaseHistory;
    }

    public void setPurchaseHistory(List<Integer> purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }

    public List<Integer> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(List<Integer> orderHistory) {
        this.orderHistory = orderHistory;
    }
}
