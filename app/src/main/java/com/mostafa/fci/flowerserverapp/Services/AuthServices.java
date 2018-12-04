package com.mostafa.fci.flowerserverapp.Services;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mostafa.fci.flowerserverapp.interfaces.AuthStateChanged;


import java.util.ArrayList;

public class AuthServices {


    final private static FirebaseAuth auth = FirebaseAuth.getInstance();

    private static ArrayList<AuthStateChanged> onAuthStateChangedList = new ArrayList<>();

    private static FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    new DBServices().setDeviceToken();
                    for (AuthStateChanged state: onAuthStateChangedList) {
                        state.onAuthStateChanged();
                    }
                }

            }
        };


    /**
     *      Sign In
     * */
    public static void signInFirebase(final Context context) {

        /**
         * authentication
         * **/
        auth.signInWithEmailAndPassword("admin@gmail.com", "admin1")
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = auth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [END_EXCLUDE]
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"ERROR:"+e.toString(),Toast.LENGTH_LONG).show();
            }
        });
        // [END create_user_with_email]
    }



    public static void setOnAuthStateChanged(AuthStateChanged authStateChanged){
        auth.addAuthStateListener(authStateListener);
        onAuthStateChangedList.add(authStateChanged);
    }

    public static boolean isAuth (){
        if ( auth.getCurrentUser() == null )
            return false;
        else
            return true;
    }

}
