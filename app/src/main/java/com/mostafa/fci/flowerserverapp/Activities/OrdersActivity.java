package com.mostafa.fci.flowerserverapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import com.mostafa.fci.flowerserverapp.Classes.Flower;
import com.mostafa.fci.flowerserverapp.Classes.Order;
import com.mostafa.fci.flowerserverapp.R;
import com.mostafa.fci.flowerserverapp.Services.DBServices;
import com.mostafa.fci.flowerserverapp.Utillities.RecyclerViewOnTouchItemHelper;
import com.mostafa.fci.flowerserverapp.adapter.FlowerRVAdapter;
import com.mostafa.fci.flowerserverapp.adapter.OrderRVAdapter;
import com.mostafa.fci.flowerserverapp.interfaces.FetchOrders;
import com.mostafa.fci.flowerserverapp.interfaces.RecyclerViewOnTouchItemHelperListener;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity {


    DBServices dbServices;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Order> ordersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);


        recyclerView = findViewById(R.id.recyclerView);
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new OrderRVAdapter(OrdersActivity.this, ordersList );
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        ItemTouchHelper.SimpleCallback itemCallback
                = new RecyclerViewOnTouchItemHelper(new RecyclerViewOnTouchItemHelperListener() {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

                Order order = ordersList.get(position);
                ordersList.remove(position);
                adapter.notifyDataSetChanged();
                new DBServices().acceptOrder(order);
                Toast.makeText(OrdersActivity.this,"Order removed"
                        ,Toast.LENGTH_SHORT).show();

            }
        });

        new ItemTouchHelper(itemCallback).attachToRecyclerView(recyclerView);

        dbServices = new DBServices();
        dbServices.setOnFetchOrders(new FetchOrders() {
            @Override
            public void onCompleted(ArrayList<Order> orders) {
                if (orders != null || orders.size() > 0 ){
                    ordersList.clear();
                    for (Order order : orders)
                        if (!order.isStatus())
                            ordersList.add(order);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

}
