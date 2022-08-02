package com.pratama.dany.kokiss;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class User_Adapter extends RecyclerView.Adapter<User_Adapter.UserViewholder> {

    private ArrayList<User_Class> mdataList;
    private UserActivity userActivity;
    private PegawaiActivity pegawaiActivity;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static class UserViewholder extends RecyclerView.ViewHolder{

        private TextView nama, email, akses;
        private ImageView foto;
        private Button view;

        public UserViewholder(View itemView){
            super(itemView);

            nama = (TextView) itemView.findViewById(R.id.nama_usr);
            email = (TextView) itemView.findViewById(R.id.email_usr);
            akses = (TextView) itemView.findViewById(R.id.akses_usr);
            foto = (ImageView) itemView.findViewById(R.id.img_usr);
            view = (Button) itemView.findViewById(R.id.view_usr);

        }

    }

    public User_Adapter(UserActivity userActivity, ArrayList<User_Class> dataList) {
        this.userActivity = userActivity;
        this.mdataList = dataList;
    }

    @NonNull
    @Override
    public User_Adapter.UserViewholder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_layout, parent, false);
        User_Adapter.UserViewholder svh = new User_Adapter.UserViewholder(view);
        return svh;

    }

    @Override
    public void onBindViewHolder(@NonNull final User_Adapter.UserViewholder holder, int position) {

        final User_Class currentItem = mdataList.get(position);

        holder.nama.setText(currentItem.getNm_usr());
        holder.email.setText(currentItem.getEmail());
        holder.akses.setText(currentItem.getAkses());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(currentItem.getImg_usr());
        try {
            final File localFile = File.createTempFile("images", "png,jpg");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    holder.foto.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e ) {}

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userActivity, AccountActivity.class);
                intent.putExtra("email", currentItem.getEmail());
                intent.putExtra("akses", currentItem.getAkses());
                userActivity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mdataList.size();
    }
}
