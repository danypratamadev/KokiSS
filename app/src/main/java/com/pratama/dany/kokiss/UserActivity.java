package com.pratama.dany.kokiss;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {

    private RecyclerView user_view;
    private ArrayList<User_Class> userArrayList;
    private User_Adapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        user_view = (RecyclerView) findViewById(R.id.user_recycler);

        user_view.setHasFixedSize(true);
        user_view.setLayoutManager(new LinearLayoutManager(UserActivity.this));
        userArrayList = new ArrayList<>();

        setUser();

    }

    public void setUser(){
        db.collection("login").orderBy("akses", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot query : task.getResult()){
                    if(query.getString("akses").equals("User")){
                        User_Class user = new User_Class(query.getId(), query.getString("foto"), query.getString("nama"), query.getString("email"), query.getString("akses"));
                        userArrayList.add(user);
                    }
                }
                adapter = new User_Adapter(UserActivity.this, userArrayList);
                user_view.setAdapter(adapter);
            }
        });
    }
}
