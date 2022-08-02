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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MinumActivity extends AppCompatActivity {

    private RecyclerView minum_view;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Minum_Adapter adapter;
    private ArrayList<Minum_Class> minumArrayList;
    private EditText cari_minum;
    public Uri fotoUri;
    public ImageView fotoku;
    public static final int PICK_IMAGE_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minum);

        minum_view = (RecyclerView) findViewById(R.id.minum_recycler);
        cari_minum = (EditText) findViewById(R.id.cari_minum);
        minum_view.setHasFixedSize(true);
        minum_view.setLayoutManager(new LinearLayoutManager(MinumActivity.this));
        minumArrayList = new ArrayList<>();

        setMenuMinum();

        cari_minum.addTextChangedListener(new TextWatcher() {
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
        ArrayList<Minum_Class> filteredList = new ArrayList<>();

        for (Minum_Class item : minumArrayList) {
            if (item.getNama_mn().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.filterList(filteredList);
    }

    public void setMenuMinum(){
        minumArrayList.clear();
        db.collection("menu").document("minum").collection("list_minum").orderBy("nm_mnu", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot query : task.getResult()){
                    Minum_Class minum = new Minum_Class(query.getId(), query.getString("img_mnu"), query.getString("nm_mnu"), query.getString("sts_mnu"), query.getLong("hrg_mnu").intValue());
                    minumArrayList.add(minum);
                }
                adapter = new Minum_Adapter(MinumActivity.this, minumArrayList);
                minum_view.setAdapter(adapter);
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
