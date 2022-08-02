package com.pratama.dany.kokiss;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class KokiActivity extends AppCompatActivity {

    private RecyclerView order_view;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Order_Adapter adapter;
    private ArrayList<Order_Class> orderArrayList;
    private ACProgressFlower loading;
    private String status;
    private ImageView aksi;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dayFormat = new SimpleDateFormat("dd-MMMM-yyyy");
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
    private String day = dayFormat.format(calendar.getTime());
    private String month = monthFormat.format(calendar.getTime());
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user_auth = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_koki);

        loading = new ACProgressFlower.Builder(KokiActivity.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE).themeColor(Color.BLUE).build();
        loading.show();

        aksi = (ImageView) findViewById(R.id.aksi);

        aksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(KokiActivity.this, aksi);
                popupMenu.inflate(R.menu.option_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.akun:
                                Intent intent = new Intent(KokiActivity.this, AccountActivity.class);
                                intent.putExtra("email", "");
                                intent.putExtra("akses", "Akses");
                                startActivity(intent);
                                break;
                            case R.id.logout:
                                final AlertDialog.Builder oBuilder = new AlertDialog.Builder(KokiActivity.this);
                                final View oView = getLayoutInflater().inflate(R.layout.logout, null);
                                final TextView email_out = (TextView) oView.findViewById(R.id.email_out);
                                final ConstraintLayout input_out = (ConstraintLayout) oView.findViewById(R.id.input_out);
                                final ConstraintLayout proses_out = (ConstraintLayout) oView.findViewById(R.id.proses_out);
                                Button logout_yes = (Button) oView.findViewById(R.id.logout_yes);
                                Button logout_no = (Button) oView.findViewById(R.id.logout_no);

                                email_out.setText(user_auth.getEmail().toString());
                                proses_out.setVisibility(View.GONE);

                                logout_yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        input_out.setVisibility(View.GONE);
                                        proses_out.setVisibility(View.VISIBLE);

                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                auth.signOut();
                                                startActivity(new Intent(KokiActivity.this, LoginActivity.class));
                                                finish();
                                            }
                                        }, 3000);
                                    }
                                });

                                oBuilder.setView(oView);
                                final AlertDialog dialog = oBuilder.create();
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.show();

                                logout_no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                break;
                                default:
                                    break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        order_view = (RecyclerView) findViewById(R.id.order_recycler);
        order_view.setHasFixedSize(true);
        order_view.setLayoutManager(new LinearLayoutManager(KokiActivity.this));
        orderArrayList = new ArrayList<>();

    }

    @Override
    protected void onStart() {
        super.onStart();
        db.collection("pesan").document(month).collection(day).orderBy("date", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                orderArrayList.clear();
                int index = 1;
                for(DocumentSnapshot query : snapshots){
                    if(query.getString("status").equals("Proses") || query.getString("status").equals("Tambah") || query.getString("status").equals("Diproses")){
                        Order_Class order = new Order_Class(index, query.getId(), query.getString("nm_plg"), query.getLong("no_mja").intValue(), query.getString("status"));
                        orderArrayList.add(order);
                        index++;
                    }
                }
                adapter = new Order_Adapter(KokiActivity.this, orderArrayList);
                order_view.setAdapter(adapter);
                loading.hide();
            }
        });
    }
}
