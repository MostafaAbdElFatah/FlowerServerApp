package com.mostafa.fci.flowerserverapp.interfaces;


import com.mostafa.fci.flowerserverapp.Classes.Order;
import com.mostafa.fci.flowerserverapp.Classes.User;

import java.util.ArrayList;

public interface FetchOrders {
    void onCompleted(ArrayList<Order> orders);
}
