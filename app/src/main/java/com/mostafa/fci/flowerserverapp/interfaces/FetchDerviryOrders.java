package com.mostafa.fci.flowerserverapp.interfaces;
import com.mostafa.fci.flowerserverapp.Classes.Order;

import java.util.ArrayList;

public interface FetchDerviryOrders {
    void onCompleted(ArrayList<Order> orders);

}
