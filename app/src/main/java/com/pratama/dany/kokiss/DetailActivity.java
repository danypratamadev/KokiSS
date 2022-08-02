package com.pratama.dany.kokiss;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class DetailActivity extends AppCompatActivity {

    private ACProgressFlower loading;
    private RecyclerView detail_view;
    private Detail_Adapter adapter;
    private ArrayList<Detail_Class> detailArrayList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView nama_plg_dt;
    Button persen_dis;
    String id_plg, getNama_plg, status;
    int getNomeja_plg;
    int item, sisa;
    int selesai = 0;
    Date date1;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dayFormat = new SimpleDateFormat("dd-MMMM-yyyy");
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
    private String day = dayFormat.format(calendar.getTime());
    private String month = monthFormat.format(calendar.getTime());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
    }

    public void init(){
        final Intent in = getIntent();
        id_plg = in.getStringExtra("id_plg");
        status = in.getStringExtra("status");

        loading = new ACProgressFlower.Builder(DetailActivity.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE).themeColor(Color.GREEN)
                .text("Please Wait").fadeColor(Color.DKGRAY).build();
        loading.show();

        nama_plg_dt = (TextView) findViewById(R.id.nama_plg_dt);
        persen_dis = (Button) findViewById(R.id.persen);

        db.collection("pesan").document(month).collection(day).document(id_plg).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot query = task.getResult();
                getNama_plg = query.getString("nm_plg");
                getNomeja_plg = query.getLong("no_mja").intValue();
                nama_plg_dt.setText(getNama_plg +" | "+String.valueOf(getNomeja_plg));
                loading.hide();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DetailActivity.this, "Ada Masalah", Toast.LENGTH_SHORT).show();
                Log.w("Masalah di", e.getMessage());
            }
        });

        detail_view = (RecyclerView) findViewById(R.id.detail_recycler);
        detail_view.setHasFixedSize(true);
        detail_view.setLayoutManager(new LinearLayoutManager(this));
        detailArrayList = new ArrayList<>();

        if(status.equals("Tambah")){
            db.collection("pesan").document(month).collection(day).document(id_plg).collection("tambahan").orderBy("ktgr_mnu", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot query : task.getResult()){
                        Detail_Class detail = new Detail_Class(query.getId(), query.getString("img_mnu"), query.getString("nm_mnu"), query.getString("ktgr_mnu"), query.getLong("jml_psn").intValue());
                        detailArrayList.add(detail);
                    }
                    adapter = new Detail_Adapter(DetailActivity.this, detailArrayList);
                    detail_view.setAdapter(adapter);
                    loading.hide();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(DetailActivity.this, "Ada Masalah", Toast.LENGTH_SHORT).show();
                    Log.w("Masalah di", e.getMessage());
                }
            });
        } else {
            db.collection("pesan").document(month).collection(day).document(id_plg).collection("order").orderBy("ktgr_mnu", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot query : task.getResult()){
                        Detail_Class detail = new Detail_Class(query.getId(), query.getString("img_mnu"), query.getString("nm_mnu"), query.getString("ktgr_mnu"), query.getLong("jml_psn").intValue());
                        detailArrayList.add(detail);
                    }
                    adapter = new Detail_Adapter(DetailActivity.this, detailArrayList);
                    detail_view.setAdapter(adapter);
                    loading.hide();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(DetailActivity.this, "Ada Masalah", Toast.LENGTH_SHORT).show();
                    Log.w("Masalah di", e.getMessage());
                }
            });
        }

        persen_dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selesai == item){
                    final AlertDialog.Builder Builder = new AlertDialog.Builder(DetailActivity.this);
                    final View view = getLayoutInflater().inflate(R.layout.dialog_order, null);
                    final TextView nama_dial = (TextView) view.findViewById(R.id.nama_dial);
                    final ConstraintLayout proses_dial = (ConstraintLayout) view.findViewById(R.id.proses_dial);
                    final ConstraintLayout done_dial = (ConstraintLayout) view.findViewById(R.id.done_dial);
                    final ConstraintLayout gagal_dial = (ConstraintLayout) view.findViewById(R.id.gagal_dial);
                    Button selesai_dial = (Button) view.findViewById(R.id.selesai_dial);
                    Button tutup_dial = (Button) view.findViewById(R.id.tutup_dial);

                    nama_dial.setText(getNama_plg);
                    done_dial.setVisibility(View.GONE);
                    gagal_dial.setVisibility(View.GONE);

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    String date = dateFormat.format(calendar.getTime());
                    try {
                        date1 = dateFormat.parse(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Map<String, Object> orderacc = new HashMap<>();
                    orderacc.put("status", "Selesai");
                    orderacc.put("done", date1);

                    db.collection("pesan").document(month).collection(day).document(id_plg).update(orderacc).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    done_dial.setVisibility(View.VISIBLE);
                                    proses_dial.setVisibility(View.GONE);
                                }
                            }, 3000);
                        }
                    });

                    Builder.setView(view);
                    final AlertDialog dialog = Builder.create();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                    selesai_dial.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            DetailActivity.super.onBackPressed();
                        }
                    });
                } else {

                }
            }
        });
    }

    public void setPersentase(int jml) {

        selesai = selesai + jml;

        sisa = item - selesai;

        if (sisa == 0 || item == 0) {
            persen_dis.setBackground(getDrawable(R.drawable.btn_add_style));
            persen_dis.setTextColor(Color.WHITE);
            persen_dis.setText("Selesai");
        } else {
            persen_dis.setText(String.valueOf(sisa) + " Item");
        }
    }

    @Override
    public void onBackPressed() {
        if(selesai == item){
            final AlertDialog.Builder Builder = new AlertDialog.Builder(DetailActivity.this);
            final View view = getLayoutInflater().inflate(R.layout.dialog_order, null);
            final TextView nama_dial = (TextView) view.findViewById(R.id.nama_dial);
            final ConstraintLayout proses_dial = (ConstraintLayout) view.findViewById(R.id.proses_dial);
            final ConstraintLayout done_dial = (ConstraintLayout) view.findViewById(R.id.done_dial);
            final ConstraintLayout gagal_dial = (ConstraintLayout) view.findViewById(R.id.gagal_dial);
            Button selesai_dial = (Button) view.findViewById(R.id.selesai_dial);
            Button tutup_dial = (Button) view.findViewById(R.id.tutup_dial);

            nama_dial.setText(getNama_plg);
            done_dial.setVisibility(View.GONE);
            gagal_dial.setVisibility(View.GONE);

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            String date = dateFormat.format(calendar.getTime());
            try {
                date1 = dateFormat.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Map<String, Object> orderacc = new HashMap<>();
            orderacc.put("status", "Selesai");
            orderacc.put("done", date1);

            db.collection("pesan").document(month).collection(day).document(id_plg).update(orderacc).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            done_dial.setVisibility(View.VISIBLE);
                            proses_dial.setVisibility(View.GONE);
                        }
                    }, 3000);
                }
            });

            Builder.setView(view);
            final AlertDialog dialog = Builder.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            selesai_dial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    DetailActivity.super.onBackPressed();
                }
            });
        } else {
            if(status.equals("Tambah")){
                Map<String, Object> status = new HashMap<>();
                status.put("status", "Tambah");
                db.collection("pesan").document(month).collection(day).document(id_plg).update(status);
                DetailActivity.super.onBackPressed();
            } else {
                Map<String, Object> status = new HashMap<>();
                status.put("status", "Proses");
                db.collection("pesan").document(month).collection(day).document(id_plg).update(status);
                DetailActivity.super.onBackPressed();
            }
        }
    }
}
