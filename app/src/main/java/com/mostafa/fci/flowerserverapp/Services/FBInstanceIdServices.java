package com.mostafa.fci.flowerserverapp.Services;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;



public class FBInstanceIdServices extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String recent_token = FirebaseInstanceId.getInstance().getToken();
       new DBServices().setDeviceToken(recent_token);
    }
}
