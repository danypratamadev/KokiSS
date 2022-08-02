package com.pratama.dany.kokiss;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    private ImageView menu, transaksi, users, pegawai;
    private ImageView aksi;
    private FirebaseUser user_auth = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        menu = (ImageView) findViewById(R.id.menu);
        transaksi = (ImageView) findViewById(R.id.transaksi);
        users = (ImageView) findViewById(R.id.user);
        pegawai = (ImageView) findViewById(R.id.pegawai);
        aksi = (ImageView) findViewById(R.id.aksi);
        barChart = (BarChart) findViewById(R.id.chart);

        setData(12);
        barChart.setFitBars(true);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, TransaksiActivity.class);
                startActivity(intent);
            }
        });

        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });

        pegawai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, PegawaiActivity.class);
                startActivity(intent);
            }
        });

        aksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(AdminActivity.this, aksi);
                popupMenu.inflate(R.menu.option_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.akun:
                                Intent intent = new Intent(AdminActivity.this, AccountActivity.class);
                                intent.putExtra("email", "");
                                intent.putExtra("akses", "View");
                                startActivity(intent);
                                break;
                            case R.id.logout:
                                final AlertDialog.Builder oBuilder = new AlertDialog.Builder(AdminActivity.this);
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
                                                startActivity(new Intent(AdminActivity.this, LoginActivity.class));
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

    }

    public void setData(int data){
        ArrayList<BarEntry> yValue = new ArrayList<>();

        for(int i = 0; i < data; i++){
            float value = (float) (Math.random()*100);
            yValue.add(new BarEntry(i, (int) value));
        }

        BarDataSet set = new BarDataSet(yValue, "Data Set");
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        set.setDrawValues(true);
        set.setValueTextColor(getResources().getColor(R.color.Gray300));

        BarData barData = new BarData(set);

        barChart.setData(barData);
        barChart.invalidate();
        barChart.animateY(500);
    }

}
