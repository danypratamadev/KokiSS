package com.pratama.dany.kokiss;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.annotation.Nullable;

public class Trans_Adapter extends RecyclerView.Adapter<Trans_Adapter.TransViewHolder> {

    private ArrayList<Trans_Class> mdataList;
    private TransaksiActivity transaksiActivity;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static class TransViewHolder extends RecyclerView.ViewHolder{

        private TextView nomor, nama, item, total;
        private ConstraintLayout back_trans;

        public TransViewHolder(View itemView){
            super(itemView);

            nomor = (TextView) itemView.findViewById(R.id.nomor);
            nama = (TextView) itemView.findViewById(R.id.nama);
            item = (TextView) itemView.findViewById(R.id.item);
            total = (TextView) itemView.findViewById(R.id.total);
            back_trans = (ConstraintLayout) itemView.findViewById(R.id.back_trans);

        }

    }

    public Trans_Adapter(TransaksiActivity transaksiActivity, ArrayList<Trans_Class> dataList) {
        this.transaksiActivity = transaksiActivity;
        this.mdataList = dataList;
    }

    @NonNull
    @Override
    public Trans_Adapter.TransViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trans_layout, parent, false);
        Trans_Adapter.TransViewHolder svh = new Trans_Adapter.TransViewHolder(view);
        return svh;

    }

    @Override
    public void onBindViewHolder(@NonNull final Trans_Adapter.TransViewHolder holder, int position) {

        final Trans_Class currentItem = mdataList.get(position);
        Locale locale = new Locale("in", "ID");
        final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        final int[] item = {0};
        final int[] total = {0};

        if(currentItem.getIndex() % 2 == 1){
            holder.back_trans.setBackgroundColor(transaksiActivity.getResources().getColor(R.color.WindowBg));
        }

        holder.nomor.setText(String.valueOf(currentItem.getIndex()));
        holder.nama.setText(currentItem.getNama_trans());

        db.collection("pesan").document(currentItem.getMonth()).collection(currentItem.getDay()).document(currentItem.getId_trans()).collection("order").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot query : task.getResult()) {
                    item[0] = item[0] + query.getLong("jml_psn").intValue();
                    total[0] = total[0] + query.getLong("tot_hrg").intValue();
                }
                holder.item.setText(String.valueOf(item[0])+" Porsi");
                holder.total.setText(numberFormat.format((double) total[0])+",-");
                transaksiActivity.getSumAll(item[0], total[0]);
            }
        });

        db.collection("pesan").document(currentItem.getMonth()).collection(currentItem.getDay()).document(currentItem.getId_trans()).collection("tambahan").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int items = 0, totals = 0;
                for (DocumentSnapshot query : task.getResult()) {
                    item[0] = item[0] + query.getLong("jml_psn").intValue();
                    total[0] = total[0] + query.getLong("tot_hrg").intValue();
                    items = items + query.getLong("jml_psn").intValue();
                    totals = totals + query.getLong("tot_hrg").intValue();
                }
                holder.item.setText(String.valueOf(item[0])+" Porsi");
                holder.total.setText(numberFormat.format((double) total[0])+",-");
                transaksiActivity.getSumAll(items, totals);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mdataList.size();
    }
}
