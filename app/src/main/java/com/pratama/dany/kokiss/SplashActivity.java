package com.pratama.dany.kokiss;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (auth.getCurrentUser() != null) {
            db.collection("login").whereEqualTo("email", user.getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                    for(DocumentSnapshot query : snapshots){
                        if(query.getString("akses").equals("Koki")){
                            startActivity(new Intent(SplashActivity.this, KokiActivity.class));
                            finish();
                        } else if(query.getString("akses").equals("Kasir")){
                            startActivity(new Intent(SplashActivity.this, KasirActivity.class));
                            finish();
                        } else {
                            startActivity(new Intent(SplashActivity.this, AdminActivity.class));
                            finish();
                        }
                    }
                }
            });

        } else {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }, 3000);
        }
    }
}
