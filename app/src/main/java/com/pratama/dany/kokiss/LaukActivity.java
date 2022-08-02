package com.pratama.dany.kokiss;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LaukActivity extends AppCompatActivity {

    private RecyclerView lauk_view;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Lauk_Adapter adapter;
    private ArrayList<Lauk_Class> laukArrayList;
    private EditText cari_lauk;
    public Uri fotoUri;
    public ImageView fotoku;
    public static final int PICK_IMAGE_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lauk);

        lauk_view = (RecyclerView) findViewById(R.id.lauk_recycler);
        cari_lauk = (EditText) findViewById(R.id.cari_lauk);
        lauk_view.setHasFixedSize(true);
        lauk_view.setLayoutManager(new LinearLayoutManager(LaukActivity.this));
        laukArrayList = new ArrayList<>();

        setMenuLauk();

        cari_lauk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

    }

    public void filter(String text) {
        ArrayList<Lauk_Class> filteredList = new ArrayList<>();

        for (Lauk_Class item : laukArrayList) {
            if (item.getNama_lk().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.filterList(filteredList);
    }

    public void setMenuLauk(){
        laukArrayList.clear();
        db.collection("menu").document("lauk").collection("list_lauk").orderBy("nm_mnu", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot query : task.getResult()){
                    Lauk_Class lauk = new Lauk_Class(query.getId(), query.getString("img_mnu"), query.getString("nm_mnu"), query.getString("sts_mnu"), query.getLong("hrg_mnu").intValue());
                    laukArrayList.add(lauk);
                }
                adapter = new Lauk_Adapter(LaukActivity.this, laukArrayList);
                lauk_view.setAdapter(adapter);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_RESULT && resultCode == RESULT_OK && data != null && data.getData() != null){
            fotoUri = data.getData();
            fotoku.setImageURI(fotoUri);
        }
    }
}
