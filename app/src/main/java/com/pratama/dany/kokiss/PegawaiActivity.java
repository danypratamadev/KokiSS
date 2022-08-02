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

public class PegawaiActivity extends AppCompatActivity {

    private RecyclerView pegawai_view;
    private ArrayList<User_Class> pegawaiArrayList;
    private Pegawai_Adapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pegawai);

        pegawai_view = (RecyclerView) findViewById(R.id.pegawai_recycler);

        pegawai_view.setHasFixedSize(true);
        pegawai_view.setLayoutManager(new LinearLayoutManager(PegawaiActivity.this));
        pegawaiArrayList = new ArrayList<>();

        setUser();
    }

    public void setUser(){
        db.collection("login").orderBy("akses", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot query : task.getResult()){
                    if(!query.getString("akses").equals("User")){
                        User_Class user = new User_Class(query.getId(), query.getString("foto"), query.getString("nama"), query.getString("email"), query.getString("akses"));
                        pegawaiArrayList.add(user);
                    }
                }
                adapter = new Pegawai_Adapter(PegawaiActivity.this, pegawaiArrayList);
                pegawai_view.setAdapter(adapter);
            }
        });
    }
}
