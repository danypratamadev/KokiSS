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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class BayarActivity extends AppCompatActivity {

    private ACProgressFlower loading;
    private RecyclerView detail_byr_view, tmbahan_recycler;
    private Bayar_Adapter adapter;
    private ArrayList<Bayar_Class> bayarArrayList, tambahArrayList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView nama_plg_dt, tot_byr_dis;
    private String id_plg, getNama_plg;
    private int getNomeja_plg, tot_bayar = 0, tot_jml = 0;
    private Button bayar_btn;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dayFormat = new SimpleDateFormat("dd-MMMM-yyyy");
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
    private String day = dayFormat.format(calendar.getTime());
    private String month = monthFormat.format(calendar.getTime());
    private Date date1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bayar);
        init();
    }

    public void init(){
        final Intent in = getIntent();
        id_plg = in.getStringExtra("id_plg");

        Locale locale = new Locale("in", "ID");
        final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

        loading = new ACProgressFlower.Builder(BayarActivity.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE).themeColor(Color.GREEN)
                .text("Please Wait").fadeColor(Color.DKGRAY).build();
        loading.show();

        nama_plg_dt = (TextView) findViewById(R.id.nama_plg_dt);
        tot_byr_dis = (TextView) findViewById(R.id.tot_byr_dis);
        bayar_btn = (Button) findViewById(R.id.bayar);

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
                Toast.makeText(BayarActivity.this, "Ada Masalah", Toast.LENGTH_SHORT).show();
                Log.w("Masalah di", e.getMessage());
            }
        });

        detail_byr_view = (RecyclerView) findViewById(R.id.detail_byr_recycler);
        detail_byr_view.setHasFixedSize(true);
        detail_byr_view.setLayoutManager(new LinearLayoutManager(this));
        bayarArrayList = new ArrayList<>();

        db.collection("pesan").document(month).collection(day).document(id_plg).collection("order").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                for(DocumentSnapshot query : snapshots){
                    Bayar_Class bayar = new Bayar_Class(query.getId(), query.getString("nm_mnu"), query.getLong("jml_psn").intValue(), query.getLong("hrg_mnu").intValue(), query.getLong("tot_hrg").intValue());
                    tot_bayar = tot_bayar + query.getLong("tot_hrg").intValue();
                    tot_jml = tot_jml + query.getLong("jml_psn").intValue();
                    bayarArrayList.add(bayar);
                }
                adapter = new Bayar_Adapter(BayarActivity.this, bayarArrayList);
                detail_byr_view.setAdapter(adapter);
                tot_byr_dis.setText(numberFormat.format((double) tot_bayar)+",-");
            }
        });

        tmbahan_recycler = (RecyclerView) findViewById(R.id.tambah_recycler);
        tmbahan_recycler.setHasFixedSize(true);
        tmbahan_recycler.setLayoutManager(new LinearLayoutManager(this));
        tambahArrayList = new ArrayList<>();

        db.collection("pesan").document(month).collection(day).document(id_plg).collection("tambahan").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                for(DocumentSnapshot query : snapshots){
                    Bayar_Class bayar = new Bayar_Class(query.getId(), query.getString("nm_mnu"), query.getLong("jml_psn").intValue(), query.getLong("hrg_mnu").intValue(), query.getLong("tot_hrg").intValue());
                    tot_bayar = tot_bayar + query.getLong("tot_hrg").intValue();
                    tot_jml = tot_jml + query.getLong("jml_psn").intValue();
                    tambahArrayList.add(bayar);
                }
                adapter = new Bayar_Adapter(BayarActivity.this, tambahArrayList);
                tmbahan_recycler.setAdapter(adapter);
                tot_byr_dis.setText(numberFormat.format((double) tot_bayar)+",-");
            }
        });

        bayar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder pBuilder = new AlertDialog.Builder(BayarActivity.this);
                final View pView = getLayoutInflater().inflate(R.layout.bayar_dialog, null);
                final TextView order_num = (TextView) pView.findViewById(R.id.order_num);
                final TextView nama_plg = (TextView) pView.findViewById(R.id.nama_plg);
                final TextView jml_item = (TextView) pView.findViewById(R.id.jml_item);
                final TextView total_byr = (TextView) pView.findViewById(R.id.total_byr);
                final TextView bayar = (TextView) pView.findViewById(R.id.bayar_txt);
                final TextView kembali = (TextView) pView.findViewById(R.id.kembali_txt);
                final EditText input_byr = (EditText) pView.findViewById(R.id.input_byr);
                final Button konfir_byr = (Button) pView.findViewById(R.id.konfir_byr);
                final ConstraintLayout input = (ConstraintLayout) pView.findViewById(R.id.input_bayar);
                final ConstraintLayout proses = (ConstraintLayout) pView.findViewById(R.id.proses_bayar);
                final ConstraintLayout done = (ConstraintLayout) pView.findViewById(R.id.done_bayar);
                final ConstraintLayout gagal = (ConstraintLayout) pView.findViewById(R.id.gagal_bayar);
                final Button selesai = (Button) pView.findViewById(R.id.selesai_bayar);
                final Button tutup = (Button) pView.findViewById(R.id.tutup_bayar);
                konfir_byr.setEnabled(false);
                proses.setVisibility(View.GONE);
                done.setVisibility(View.GONE);
                gagal.setVisibility(View.GONE);

                order_num.setText("Order : "+id_plg);
                nama_plg.setText(": "+getNama_plg);
                jml_item.setText(": "+String.valueOf(tot_jml));
                total_byr.setText(numberFormat.format((double) tot_bayar)+",-");

                input_byr.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(s.toString().equals("")){
                            bayar.setText("Rp,-");
                            kembali.setText("Rp,-");
                        } else {
                            hitungBayar(Integer.parseInt(s.toString()));
                        }
                    }

                    public void hitungBayar(int bayar_mon){
                        int kembalian = 0;

                        bayar.setText(numberFormat.format((double) bayar_mon)+",-");

                        kembalian = bayar_mon - tot_bayar;

                        kembali.setText(numberFormat.format((double) kembalian)+",-");

                        if(kembalian >= 0){
                            konfir_byr.setEnabled(true);
                            kembali.setTextColor(getResources().getColor(R.color.Gray900));
                        } else {
                            kembali.setTextColor(getResources().getColor(R.color.Red600));
                        }
                    }
                });

                pBuilder.setView(pView);
                final AlertDialog dialog = pBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                konfir_byr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        input.setVisibility(View.GONE);
                        proses.setVisibility(View.VISIBLE);

                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        String date = dateFormat.format(calendar.getTime());
                        try {
                            date1 = dateFormat.parse(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Map<String, Object> orderbayar = new HashMap<>();
                        orderbayar.put("status", "Sukses");
                        orderbayar.put("success", date1);
                        orderbayar.put("items", tot_jml);
                        orderbayar.put("price", tot_bayar);

                        db.collection("pesan").document(month).collection(day).document(id_plg).update(orderbayar).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        done.setVisibility(View.VISIBLE);
                                        proses.setVisibility(View.GONE);
                                    }
                                }, 3000);
                            }
                        });
                    }
                });

                selesai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                tutup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }
}
