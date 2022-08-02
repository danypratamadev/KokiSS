package com.pratama.dany.kokiss;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MenuActivity extends AppCompatActivity {

    private ConstraintLayout btn_lauk, btn_sayur, btn_sambal, btn_minum;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView jml_lk, jml_sy, jml_sb, jml_mn, tot_lk, tot_sy, tot_sb, tot_mn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btn_lauk = (ConstraintLayout) findViewById(R.id.btn_lauk);
        btn_sayur = (ConstraintLayout) findViewById(R.id.btn_sayur);
        btn_sambal = (ConstraintLayout) findViewById(R.id.btn_sambal);
        btn_minum = (ConstraintLayout) findViewById(R.id.btn_minum);
        jml_lk = (TextView) findViewById(R.id.jml_lk);
        jml_sy = (TextView) findViewById(R.id.jml_sy);
        jml_sb = (TextView) findViewById(R.id.jml_sb);
        jml_mn = (TextView) findViewById(R.id.jml_mn);
        tot_lk = (TextView) findViewById(R.id.tot_lk);
        tot_sy = (TextView) findViewById(R.id.tot_sy);
        tot_sb = (TextView) findViewById(R.id.tot_sb);
        tot_mn = (TextView) findViewById(R.id.tot_mn);

        btn_lauk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, LaukActivity.class);
                startActivity(intent);
            }
        });

        btn_sayur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, SayurActivity.class);
                startActivity(intent);
            }
        });

        btn_sambal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, SambalActivity.class);
                startActivity(intent);
            }
        });

        btn_minum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MinumActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        final int[] lauk = {0};
        final int[] sayur = {0};
        final int[] sambal = {0};
        final int[] minum = {0};
        db.collection("menu").document("lauk").collection("list_lauk").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int count = 0;
                for(DocumentSnapshot query : task.getResult()){
                    lauk[0] = lauk[0] + query.getLong("jml_psn").intValue();
                    count++;
                }
                jml_lk.setText(String.valueOf(lauk[0])+" x Dipesan");
                tot_lk.setText(String.valueOf(count)+" Menu Lauk");
            }
        });

        db.collection("menu").document("sayur").collection("list_sayur").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int count = 0;
                for(DocumentSnapshot query : task.getResult()){
                    sayur[0] = sayur[0] + query.getLong("jml_psn").intValue();
                    count++;
                }
                jml_sy.setText(String.valueOf(sayur[0])+" x Dipesan");
                tot_sy.setText(String.valueOf(count)+" Menu Sayur");
            }
        });

        db.collection("menu").document("sambal").collection("list_sambal").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int count = 0;
                for(DocumentSnapshot query : task.getResult()){
                    sambal[0] = sambal[0] + query.getLong("jml_psn").intValue();
                    count++;
                }
                jml_sb.setText(String.valueOf(sambal[0])+" x Dipesan");
                tot_sb.setText(String.valueOf(count)+" Menu Sambal");
            }
        });

        db.collection("menu").document("minum").collection("list_minum").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int count = 0;
                for(DocumentSnapshot query : task.getResult()){
                    minum[0] = minum[0] + query.getLong("jml_psn").intValue();
                    count++;
                }
                jml_mn.setText(String.valueOf(minum[0])+" x Dipesan");
                tot_mn.setText(String.valueOf(count)+" Menu Minuman");
            }
        });
    }
}
