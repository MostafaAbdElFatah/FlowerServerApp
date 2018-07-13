package com.mostafa.fci.flowerserverapp.Services;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mostafa.fci.flowerserverapp.Classes.Flower;
import com.mostafa.fci.flowerserverapp.Classes.Order;
import com.mostafa.fci.flowerserverapp.Classes.User;
import com.mostafa.fci.flowerserverapp.interfaces.FetchFlowers;
import com.mostafa.fci.flowerserverapp.interfaces.FetchOrders;

import java.util.ArrayList;
import java.util.Random;

public class DBServices {


    private DataSnapshot flowersDataSnapshot;
    private StorageReference storageReference;
    // Listener for orders , flowers or user Name
    private FetchOrders fetchOrder;
    private FetchFlowers fetchFlower;
    // firebase root nodes and child nodes
    private DatabaseReference root = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://flowerapp-89831.firebaseio.com/");
    private DatabaseReference usersChild  = root.child("users");
    private DatabaseReference flowersChild  = root.child("flowers");

    public DBServices(){
        flowersChild.addValueEventListener(flowersDataChange);
    }
    // when flowers data change

    private   ValueEventListener usersDataChange = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ArrayList<Order> ordersList = new ArrayList<>();
            for (DataSnapshot userDatasnapshot: dataSnapshot.getChildren()) {
                User user = new User();
                user.setUid(userDatasnapshot.getKey().toString());
                user.setName(userDatasnapshot.child("name").getValue().toString());
                user.setAddress(userDatasnapshot.child("address").getValue().toString());
                user.setPhone(userDatasnapshot.child("phone").getValue().toString());

                DataSnapshot ordersDatasnapshot = userDatasnapshot.child("orders");
                for (DataSnapshot orderDatasnapshpt: ordersDatasnapshot.getChildren()) {
                    Order order = orderDatasnapshpt.getValue(Order.class);
                    order.setUser(user);
                    ordersList.add(order);
                }
            }

            if (fetchOrder != null)
                fetchOrder.onCompleted(ordersList);
            // end of if statement

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    // when flowers data change

    private   ValueEventListener flowersDataChange = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ArrayList<Flower> flowersList = new ArrayList<>();
            flowersDataSnapshot = dataSnapshot;
            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                Flower flower = snapshot.getValue(Flower.class);
                flowersList.add(flower);
            }
            if (fetchFlower != null) {
                fetchFlower.onCompleted(flowersList);

            }
            // end of if statement

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    /**
     * method for sign i , sign up ,sign out , set listener for flowers data or orders data
     * */

    public   void removeOrder(final Flower flower) {

        this.setOnFetchFlowers(new FetchFlowers() {
            @Override
            public void onCompleted(ArrayList<Flower> flowersList) {
                String key = getKey(flower.getProductId());
                if (!key.equals(""))
                    flowersChild.child(key).removeValue();
                storageReference = FirebaseStorage.getInstance().getReference();
                storageReference.child( "flowers/"+flower.getPhoto() ).delete();
            }
        });

    }

    public  String getKey(int id){
        for (DataSnapshot snapshot: flowersDataSnapshot.getChildren()) {
            Flower flower1 = snapshot.getValue(Flower.class);
            if (id == flower1.getProductId())
                return snapshot.getKey();
        }
        return "";
    }
    // set new flower in firebase database
    public void pushFlower(Flower flower){
        Random rand = new Random();
        int  id = rand.nextInt(999999999);
        flower.setProductId(id);
        flowersChild.push().setValue(flower);
    }

    public void acceptOrder(Order order){
        usersChild.child(order.getUser().getUid()).child("orders")
                .child(order.getId()).child("status").setValue(true);
    }

    // listener for flowers data
    public void setOnFetchFlowers(FetchFlowers fetchData){
        flowersChild.addValueEventListener(flowersDataChange);
        this.fetchFlower = fetchData;
    }
    // listener for users data
    public void setOnFetchOrders(FetchOrders fetchData){
        usersChild.addValueEventListener(usersDataChange);
        this.fetchOrder = fetchData;
    }

    // set device token to database
    public void setDeviceToken(){
        String recent_token = FirebaseInstanceId.getInstance().getToken();
        if (recent_token != "")
            root.child("Admin").child("device_token").setValue(recent_token);
    }

    // set device token to database
    public void setDeviceToken(String recent_token){
        if (recent_token != "")
            root.child("Admin").child("device_token").setValue(recent_token);
    }

}
