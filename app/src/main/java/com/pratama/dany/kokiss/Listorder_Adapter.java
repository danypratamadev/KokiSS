package com.pratama.dany.kokiss;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class Listorder_Adapter extends RecyclerView.Adapter<Listorder_Adapter.OrderViewHolder> {

    private KasirActivity kasirActivity;
    private ArrayList<Order_Class> mdataList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dayFormat = new SimpleDateFormat("dd-MMMM-yyyy");
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
    private String day = dayFormat.format(calendar.getTime());
    private String month = monthFormat.format(calendar.getTime());

    public static class OrderViewHolder extends RecyclerView.ViewHolder{

        private TextView nama_plg, nomeja_plg, item_plg, status_plg, no_index, textView7;
        private ConstraintLayout order_view;

        public OrderViewHolder(View itemView){
            super(itemView);

            textView7 = (TextView) itemView.findViewById(R.id.textView7);
            nama_plg = (TextView) itemView.findViewById(R.id.nama_plg);
            nomeja_plg = (TextView) itemView.findViewById(R.id.nomeja_plg);
            item_plg = (TextView) itemView.findViewById(R.id.item_plg);
            //status_plg = (TextView) itemView.findViewById(R.id.status_plg);
            //no_index = (TextView) itemView.findViewById(R.id.no_index);
            order_view = (ConstraintLayout) itemView.findViewById(R.id.order_view);

        }

    }

    public Listorder_Adapter(KasirActivity kasirActivity, ArrayList<Order_Class> dataList) {
        this.kasirActivity = kasirActivity;
        this.mdataList = dataList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.listorder_layout, parent, false);
        Listorder_Adapter.OrderViewHolder svh = new Listorder_Adapter.OrderViewHolder(view);
        return svh;

    }

    @Override
    public void onBindViewHolder(@NonNull final OrderViewHolder holder, int position) {

        final int[] count = {0};
        final Order_Class currentItem = mdataList.get(position);
        Locale locale = new Locale("in", "ID");
        final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

        //holder.no_index.setText(String.valueOf(currentItem.getIndex()));

        holder.nama_plg.setText(currentItem.getNama_plg());
        holder.nomeja_plg.setText(String.valueOf(currentItem.getNomeja_plg()));

        db.collection("pesan").document(month).collection(day).document(currentItem.getId_plg()).collection("order").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentSnapshot document : snapshots) {
                    count[0] = count[0] + document.getLong("tot_hrg").intValue();
                }
                holder.item_plg.setText(numberFormat.format((double) count[0])+",-");
            }
        });

        db.collection("pesan").document(month).collection(day).document(currentItem.getId_plg()).collection("tambahan").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentSnapshot document : snapshots) {
                    count[0] = count[0] + document.getLong("tot_hrg").intValue();
                }
                holder.item_plg.setText(numberFormat.format((double) count[0])+",-");
            }
        });

        //holder.status_plg.setText(currentItem.getStatus_plg());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(kasirActivity, BayarActivity.class);
                intent.putExtra("id_plg", currentItem.getId_plg());
                kasirActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mdataList.size();
    }

    public void filterList(ArrayList<Order_Class> filteredList) {
        mdataList = filteredList;
        notifyDataSetChanged();
    }
}
