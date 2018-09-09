package com.mostafa.fci.flowerserverapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mostafa.fci.flowerserverapp.Classes.Flower;
import com.mostafa.fci.flowerserverapp.Utillities.RecyclerViewOnTouchItemHelper;
import com.mostafa.fci.flowerserverapp.adapter.FlowerRVAdapter;
import com.mostafa.fci.flowerserverapp.R;
import com.mostafa.fci.flowerserverapp.Services.AuthServices;
import com.mostafa.fci.flowerserverapp.Services.DBServices;
import com.mostafa.fci.flowerserverapp.interfaces.AuthStateChanged;
import com.mostafa.fci.flowerserverapp.interfaces.FetchFlowers;
import com.mostafa.fci.flowerserverapp.interfaces.RecyclerViewOnTouchItemHelperListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DBServices dbServices;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Flower> flowersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new FlowerRVAdapter(MainActivity.this,flowersList );
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        ItemTouchHelper.SimpleCallback itemCallback
                = new RecyclerViewOnTouchItemHelper(new RecyclerViewOnTouchItemHelperListener() {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

                Flower flower = flowersList.get(position);
                flowersList.remove(position);
                adapter.notifyDataSetChanged();
                new DBServices().removeOrder(flower);
                Toast.makeText(MainActivity.this,"Order removed"
                        ,Toast.LENGTH_SHORT).show();

            }
        });

        new ItemTouchHelper(itemCallback).attachToRecyclerView(recyclerView);

        dbServices = new DBServices();
        AuthServices.signInFirebase(MainActivity.this);
        AuthServices.setOnAuthStateChanged(new AuthStateChanged() {
            @Override
            public void onAuthStateChanged() {
                getFlowerFromServer();
            }
        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.flower_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add) {
            // start add activity
            startActivity(new Intent(MainActivity.this,AddFlowerActivity.class));
            return true;
        }else if (id == R.id.orders){
            startActivity(new Intent(MainActivity.this,OrdersActivity.class));
            return true;
        }else if (id == R.id.derviries){
            startActivity(new Intent(MainActivity.this,DeliveriesActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void getFlowerFromServer(){
        dbServices.setOnFetchFlowers(new FetchFlowers() {
            @Override
            public void onCompleted(ArrayList<Flower> flowers) {
                if (flowers != null && flowers.size() > 0) {
                    flowersList.clear();
                    flowersList.addAll(flowers);
                    adapter.notifyDataSetChanged();
                }
            }
        });


    }


    // end of class
}
