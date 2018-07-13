package com.mostafa.fci.flowerserverapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.mostafa.fci.flowerserverapp.Classes.Order;
import com.mostafa.fci.flowerserverapp.R;
import com.mostafa.fci.flowerserverapp.Services.DBServices;
import com.mostafa.fci.flowerserverapp.adapter.DerviryRVAdapter;
import com.mostafa.fci.flowerserverapp.adapter.OrderRVAdapter;
import com.mostafa.fci.flowerserverapp.interfaces.FetchOrders;

import java.util.ArrayList;

public class DerviriesActivity extends AppCompatActivity {


    DBServices dbServices;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Order> ordersList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_derviries);

        recyclerView = findViewById(R.id.recyclerView);
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new DerviryRVAdapter(DerviriesActivity.this, ordersList );
        recyclerView.setAdapter(adapter);

        dbServices = new DBServices();
        dbServices.setOnFetchOrders(new FetchOrders() {
            @Override
            public void onCompleted(ArrayList<Order> orders) {
                if (orders != null || orders.size() > 0 ){
                    ordersList.clear();
                    for (Order order : orders)
                        if (order.isStatus())
                            ordersList.add(order);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }


}
