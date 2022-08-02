package com.pratama.dany.kokiss;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;

public class Detail_Adapter extends RecyclerView.Adapter<Detail_Adapter.DetailViewHolder> {

    private DetailActivity detailActivity;
    private ArrayList<Detail_Class> mdataList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    int tot, op=1;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dayFormat = new SimpleDateFormat("dd-MMMM-yyyy");
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
    private String day = dayFormat.format(calendar.getTime());
    private String month = monthFormat.format(calendar.getTime());

    public static class DetailViewHolder extends RecyclerView.ViewHolder{

        ImageView img_dt, img_done;
        TextView nama_dt, jml_dt;
        Button done;

        public DetailViewHolder(View itemView){
            super(itemView);

            img_dt = (ImageView) itemView.findViewById(R.id.img_dt);
            nama_dt = (TextView) itemView.findViewById(R.id.nama_dt);
            jml_dt = (TextView) itemView.findViewById(R.id.jml_dt);
            done = (Button) itemView.findViewById(R.id.done);
            done.setVisibility(View.GONE);
            img_done = (ImageView) itemView.findViewById(R.id.img_done);
            img_done.setVisibility(View.GONE);

        }

    }

    public Detail_Adapter(DetailActivity detailActivity, ArrayList<Detail_Class> dataList){

        this.detailActivity = detailActivity;
        this.mdataList = dataList;

    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.detail_layout, parent, false);
        DetailViewHolder svh = new DetailViewHolder(view);
        return svh;

    }

    @Override
    public void onBindViewHolder(@NonNull final DetailViewHolder holder, final int position) {
        final Detail_Class currentItem = mdataList.get(position);

        Locale locale = new Locale("in", "ID");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

        holder.nama_dt.setText(currentItem.getDetail_nm_mnu());
        holder.jml_dt.setText(String.valueOf(currentItem.getDetail_jml_mnu())+" Porsi");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(currentItem.getDetail_img());

        try {
            final File localFile = File.createTempFile("images", "png,jpg");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    holder.img_dt.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e ) {}

        if(detailActivity.status.equals("Tambah")){
            db.collection("pesan").document(month).collection(day).document(detailActivity.id_plg).collection("tambahan").whereEqualTo("sts_mnu", "Tambah").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        int count = 0;
                        for (DocumentSnapshot document : task.getResult()) {
                            count = count + document.getLong("jml_psn").intValue();
                        }
                        if(count == 0){
                            detailActivity.persen_dis.setBackground(detailActivity.getDrawable(R.drawable.btn_add_style));
                            detailActivity.persen_dis.setTextColor(Color.WHITE);
                            detailActivity.persen_dis.setText("Selesai");
                        } else {
                            detailActivity.item = count;
                            detailActivity.persen_dis.setText(String.valueOf(count) + " Item");
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });

            db.collection("pesan").document(month).collection(day).document(detailActivity.id_plg).collection("tambahan").document(currentItem.getDetail_id()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    String sts_mnu;
                    if(task.isSuccessful()){
                        DocumentSnapshot doc = task.getResult();
                        sts_mnu = doc.getString("sts_mnu");

                        if(sts_mnu.equals("Selesai")){
                            holder.img_done.setVisibility(View.VISIBLE);
                        } else {
                            holder.done.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
        } else {
            db.collection("pesan").document(month).collection(day).document(detailActivity.id_plg).collection("order").whereEqualTo("sts_mnu", "Pesan").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        int count = 0;
                        for (DocumentSnapshot document : task.getResult()) {
                            count = count + document.getLong("jml_psn").intValue();
                        }
                        if(count == 0){
                            detailActivity.persen_dis.setBackground(detailActivity.getDrawable(R.drawable.btn_add_style));
                            detailActivity.persen_dis.setTextColor(Color.WHITE);
                            detailActivity.persen_dis.setText("Selesai");
                        } else {
                            detailActivity.item = count;
                            detailActivity.persen_dis.setText(String.valueOf(count) + " Item");
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });

            db.collection("pesan").document(month).collection(day).document(detailActivity.id_plg).collection("order").document(currentItem.getDetail_id()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    String sts_mnu;
                    if(task.isSuccessful()){
                        DocumentSnapshot doc = task.getResult();
                        sts_mnu = doc.getString("sts_mnu");

                        if(sts_mnu.equals("Selesai")){
                            holder.img_done.setVisibility(View.VISIBLE);
                        } else {
                            holder.done.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
        }

        holder.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> orderupdate = new HashMap<>();
                orderupdate.put("sts_mnu", "Selesai");

                if(detailActivity.status.equals("Tambah")){
                    db.collection("pesan").document(month).collection(day).document(detailActivity.id_plg).collection("tambahan").document(currentItem.getDetail_id()).update(orderupdate);
                } else {
                    db.collection("pesan").document(month).collection(day).document(detailActivity.id_plg).collection("order").document(currentItem.getDetail_id()).update(orderupdate);
                }

                int i = currentItem.getDetail_jml_mnu();
                detailActivity.setPersentase(i);
                holder.done.setVisibility(View.GONE);
                holder.img_done.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public int getItemCount() {

        return mdataList.size();

    }

}
