package com.pratama.dany.kokiss;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.annotation.Nullable;

public class TransaksiActivity extends AppCompatActivity {

    private ImageButton calendar;
    private DatePickerDialog.OnDateSetListener mOnDateSetListener;
    private TextView bulan, tanggal, transaksi, porsi, bayar;
    private Calendar cal = Calendar.getInstance();
    private int year = cal.get(Calendar.YEAR);
    private int month = cal.get(Calendar.MONTH);
    private int day = cal.get(Calendar.DAY_OF_MONTH);
    private RecyclerView trans_view;
    private ArrayList<Trans_Class> transArrayList;
    private Trans_Adapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SimpleDateFormat dayFormat = new SimpleDateFormat("dd-MMMM-yyyy");
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
    private String day_day = dayFormat.format(cal.getTime());
    private String month_month = monthFormat.format(cal.getTime());
    private int items = 0, totals = 0;
    private String TAG = "TransaksiActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

        calendar = (ImageButton) findViewById(R.id.calendar);
        bulan = (TextView) findViewById(R.id.bulan);
        tanggal = (TextView) findViewById(R.id.tanggal);
        transaksi = (TextView) findViewById(R.id.transaksi);
        porsi = (TextView) findViewById(R.id.porsi);
        bayar = (TextView) findViewById(R.id.bayar);
        trans_view = (RecyclerView) findViewById(R.id.transaksi_recycler);
        trans_view.setHasFixedSize(true);
        trans_view.setLayoutManager(new LinearLayoutManager(TransaksiActivity.this));
        transArrayList = new ArrayList<>();

        bulan.setText(": "+month_month);
        tanggal.setText(": "+day_day);

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(TransaksiActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, mOnDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                final SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
                final SimpleDateFormat dayFormat = new SimpleDateFormat("MM");
                final String month_txt;
                try {
                    month_txt = monthFormat.format(dayFormat.parse(String.valueOf(month)));
                    final String date = dayOfMonth + "-" + month_txt + "-" + year;
                    tanggal.setText(date);

                    bulan.setText(": "+month_txt);
                    tanggal.setText(": "+date);
                    transArrayList.clear();
                    items = 0;
                    totals = 0;
                    porsi.setText("Total : Porsi");
                    bayar.setText("Rp0,-");

                    db.collection("pesan").document(month_txt).collection(date).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            int index = 1;
                            for(DocumentSnapshot query : task.getResult()){
                                Trans_Class trans = new Trans_Class(index, query.getId(), query.getString("nm_plg"), month_txt, date);
                                transArrayList.add(trans);
                                index++;
                            }
                            adapter = new Trans_Adapter(TransaksiActivity.this, transArrayList);
                            trans_view.setAdapter(adapter);
                            transaksi.setText(String.valueOf(index-1)+" Transaksi");
                        }
                    });
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();

        db.collection("pesan").document(month_month).collection(day_day).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                int index = 1;
                for(DocumentSnapshot query : snapshots){
                    Trans_Class trans = new Trans_Class(index, query.getId(), query.getString("nm_plg"), month_month, day_day);
                    transArrayList.add(trans);
                    index++;
                }
                adapter = new Trans_Adapter(TransaksiActivity.this, transArrayList);
                trans_view.setAdapter(adapter);
                transaksi.setText(String.valueOf(index-1)+" Transaksi");
            }
        });
    }

    public void getSumAll(int item, int total){
        Locale locale = new Locale("in", "ID");
        final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        items = items + item;
        totals = totals + total;

        porsi.setText("Total : "+String.valueOf(items)+" Porsi");
        bayar.setText(numberFormat.format((double) totals)+",-");
    }
}
