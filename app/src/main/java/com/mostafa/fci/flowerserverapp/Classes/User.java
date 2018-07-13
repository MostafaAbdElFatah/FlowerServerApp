package com.mostafa.fci.flowerserverapp.Classes;

import java.util.ArrayList;


public class User {

    private String uid;
    private String name;
    private String address;
    private String phone;
    private ArrayList<Order> orders = new ArrayList<>();

    public void setOrder(Order order) {
        this.orders.add(order);
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}
