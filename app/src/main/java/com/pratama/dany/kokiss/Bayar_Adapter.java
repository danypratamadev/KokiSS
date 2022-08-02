package com.pratama.dany.kokiss;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Bayar_Adapter extends RecyclerView.Adapter<Bayar_Adapter.BayarViewHolder> {

    private BayarActivity bayarActivity;
    private ArrayList<Bayar_Class> mdataList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    int total = 0;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dayFormat = new SimpleDateFormat("dd-MMMM-yyyy");
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
    private String day = dayFormat.format(calendar.getTime());
    private String month = monthFormat.format(calendar.getTime());

    public static class BayarViewHolder extends RecyclerView.ViewHolder{

        TextView nama_byr, jml_byr, tot_byr;

        public BayarViewHolder(View itemView){
            super(itemView);

            nama_byr = (TextView) itemView.findViewById(R.id.nama_byr);
            jml_byr = (TextView) itemView.findViewById(R.id.jml_byr);
            tot_byr = (TextView) itemView.findViewById(R.id.tot_byr);

        }

    }

    public Bayar_Adapter(BayarActivity bayarActivity, ArrayList<Bayar_Class> dataList){
        this.bayarActivity = bayarActivity;
        this.mdataList = dataList;
    }

    @NonNull
    @Override
    public BayarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.bayar_layout, parent, false);
        Bayar_Adapter.BayarViewHolder svh = new Bayar_Adapter.BayarViewHolder(view);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull BayarViewHolder holder, int position) {

        final Bayar_Class currentItem = mdataList.get(position);
        Locale locale = new Locale("in", "ID");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

        holder.nama_byr.setText(currentItem.getBayar_nm_mnu());
        holder.jml_byr.setText(String.valueOf(currentItem.getBayar_jml_mnu())+" Porsi x "+numberFormat.format((double) currentItem.getBayar_hrg_mnu()));
        holder.tot_byr.setText(numberFormat.format((double) currentItem.getBayar_tot_all()));

    }

    @Override
    public int getItemCount() {
        return mdataList.size();
    }
}
