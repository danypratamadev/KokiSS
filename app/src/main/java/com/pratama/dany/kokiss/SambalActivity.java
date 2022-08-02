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

public class SambalActivity extends AppCompatActivity {

    private RecyclerView sambal_view;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Sambal_Adapter adapter;
    private ArrayList<Sambal_Class> sambalArrayList;
    private EditText cari_sambal;
    public Uri fotoUri;
    public ImageView fotoku;
    public static final int PICK_IMAGE_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sambal);

        sambal_view = (RecyclerView) findViewById(R.id.sambal_recycler);
        cari_sambal = (EditText) findViewById(R.id.cari_sambal);
        sambal_view.setHasFixedSize(true);
        sambal_view.setLayoutManager(new LinearLayoutManager(SambalActivity.this));
        sambalArrayList = new ArrayList<>();

        setMenuSambal();

        cari_sambal.addTextChangedListener(new TextWatcher() {
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
        ArrayList<Sambal_Class> filteredList = new ArrayList<>();

        for (Sambal_Class item : sambalArrayList) {
            if (item.getNama_sb().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.filterList(filteredList);
    }

    public void setMenuSambal(){
        sambalArrayList.clear();
        db.collection("menu").document("sambal").collection("list_sambal").orderBy("nm_mnu", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot query : task.getResult()){
                    Sambal_Class sambal = new Sambal_Class(query.getId(), query.getString("img_mnu"), query.getString("nm_mnu"), query.getString("sts_mnu"), query.getLong("hrg_mnu").intValue());
                    sambalArrayList.add(sambal);
                }
                adapter = new Sambal_Adapter(SambalActivity.this, sambalArrayList);
                sambal_view.setAdapter(adapter);
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
