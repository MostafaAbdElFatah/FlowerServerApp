package com.mostafa.fci.flowerserverapp.Classes;

/**
 * Created by FCI on 2018-07-12.
 */

public class Order {

    private String id;
    private int quantity;
    private String flowerName;
    private double totalPrice;
    private double discount;
    private boolean status;
    private String payment;
    private  User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setFlowerName(String flowerName) {
        this.flowerName = flowerName;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getFlowerName() {
        return flowerName;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public boolean isStatus() {
        return status;
    }

    public String getPayment() {
        return payment;
    }
}
