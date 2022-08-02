package com.pratama.dany.kokiss;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Minum_Adapter extends RecyclerView.Adapter<Minum_Adapter.MinumViewHolder> {

    private MinumActivity minumActivity;
    private ArrayList<Minum_Class> mdataList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String[] status = new String[] {"Tersedia", "Habis"};
    private List<String> liststatus = new ArrayList<>(Arrays.asList(status));
    private StorageReference storageReference1;
    private StorageTask storageTask;

    public static class MinumViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNama, txtStatus, txtHarga;
        private Button edit_lk;
        private ImageView img_lk;

        public MinumViewHolder(View itemView) {
            super(itemView);

            txtNama = (TextView) itemView.findViewById(R.id.nama_lk);
            txtStatus = (TextView) itemView.findViewById(R.id.status_lk);
            txtHarga = (TextView) itemView.findViewById(R.id.harga_lk);
            img_lk = (ImageView) itemView.findViewById(R.id.img_lk);
            edit_lk = (Button) itemView.findViewById(R.id.edit_lk);
        }
    }

    public Minum_Adapter(MinumActivity minumActivity, ArrayList<Minum_Class> dataList) {
        this.minumActivity = minumActivity;
        this.mdataList = dataList;
    }

    @NonNull
    @Override
    public Minum_Adapter.MinumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lauk_layout, parent, false);
        Minum_Adapter.MinumViewHolder svh = new Minum_Adapter.MinumViewHolder(view);
        return svh;

    }

    @Override
    public void onBindViewHolder(@NonNull final Minum_Adapter.MinumViewHolder holder, int position) {

        Locale locale = new Locale("in", "ID");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        final Minum_Class currentItem = mdataList.get(position);

        if(currentItem.getStatus_mn().equals("Habis")){
            holder.txtNama.setTextColor(minumActivity.getResources().getColor(R.color.RedA400));
            holder.txtStatus.setTextColor(minumActivity.getResources().getColor(R.color.RedA400));
            holder.txtStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_bookmark_border_red_a400_18dp, 0,0,0);
            holder.txtHarga.setTextColor(minumActivity.getResources().getColor(R.color.RedA400));
        }

        holder.txtNama.setText(currentItem.getNama_mn());
        holder.txtStatus.setText(currentItem.getStatus_mn());
        holder.txtHarga.setText(numberFormat.format((double) currentItem.getHarga_mn()));
        holder.txtHarga.setTag(String.valueOf(currentItem.getHarga_mn()));

        final FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(currentItem.getImg_mn());
        try {
            final File localFile = File.createTempFile("images", "png,jpg");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    holder.img_lk.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e ) {}

        holder.edit_lk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] nama_txt = new String[1];
                final String[] status_txt = new String[1];
                final int[] harga_txt = new int[1];
                int index = 0;

                final AlertDialog.Builder pBuilder = new AlertDialog.Builder(minumActivity);
                final View pView = minumActivity.getLayoutInflater().inflate(R.layout.edit_layout, null);
                final EditText nama_lk = (EditText) pView.findViewById(R.id.nama_lk);
                final Spinner status_lk = (Spinner) pView.findViewById(R.id.status_lk);
                final EditText harga_lk = (EditText) pView.findViewById(R.id.harga_lk);
                minumActivity.fotoku = (ImageView) pView.findViewById(R.id.fotoku);
                final ProgressBar load_foto = (ProgressBar) pView.findViewById(R.id.load_foto);
                final ConstraintLayout input = (ConstraintLayout) pView.findViewById(R.id.input_menu);
                final ConstraintLayout proses = (ConstraintLayout) pView.findViewById(R.id.proses_menu);
                final ConstraintLayout done = (ConstraintLayout) pView.findViewById(R.id.done_menu);
                final ConstraintLayout gagal = (ConstraintLayout) pView.findViewById(R.id.gagal_menu);
                Button save = (Button) pView.findViewById(R.id.save_profil);
                Button selesai = (Button) pView.findViewById(R.id.selesai_menu);
                Button tutup = (Button) pView.findViewById(R.id.tutup_menu);

                minumActivity.fotoku.setScaleType(ImageView.ScaleType.FIT_XY);

                final StorageReference storageReference = storage.getReferenceFromUrl(currentItem.getImg_mn());

                try {
                    final File localFile = File.createTempFile("images", "png,jpg");
                    storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            minumActivity.fotoku.setImageBitmap(bitmap);
                            load_foto.setVisibility(View.GONE);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    });
                } catch (IOException err ) {}

                if(currentItem.getStatus_mn().equals("Tersedia")){
                    index = 0;
                } else {
                    index = 1;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(minumActivity, android.R.layout.simple_spinner_dropdown_item, liststatus){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view =super.getView(position, convertView, parent);
                        TextView textView = (TextView) view.findViewById(android.R.id.text1);
                        textView.setTextSize(14);
                        return view;
                    }
                };
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                status_lk.setAdapter(adapter);

                nama_lk.setText(currentItem.getNama_mn());
                status_lk.setSelection(index);
                harga_lk.setText(String.valueOf(currentItem.getHarga_mn()));

                nama_lk.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        nama_lk.setBackground(minumActivity.getDrawable(R.drawable.edittext_active_style));
                        harga_lk.setBackground(minumActivity.getDrawable(R.drawable.edittext_style));
                    }
                });
                harga_lk.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        nama_lk.setBackground(minumActivity.getDrawable(R.drawable.edittext_style));
                        harga_lk.setBackground(minumActivity.getDrawable(R.drawable.edittext_active_style));
                    }
                });
                save.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        nama_lk.setBackground(minumActivity.getDrawable(R.drawable.edittext_style));
                        harga_lk.setBackground(minumActivity.getDrawable(R.drawable.edittext_style));
                    }
                });

                proses.setVisibility(View.GONE);
                done.setVisibility(View.GONE);
                gagal.setVisibility(View.GONE);

                storageReference1 = storage.getReference("minum");

                minumActivity.fotoku.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openFileChooser();
                    }
                });

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(minumActivity.fotoUri != null) {
                            input.setVisibility(View.GONE);
                            proses.setVisibility(View.VISIBLE);
                            if (storageTask != null && storageTask.isInProgress()) {
                                Toast.makeText(minumActivity, "Setup in progress", Toast.LENGTH_SHORT).show();
                            } else {
                                uploadFoto(currentItem.getId_mnu_mn());
                                nama_txt[0] = nama_lk.getText().toString();
                                status_txt[0] = status_lk.getSelectedItem().toString();
                                if (harga_lk.getText().equals("")) {
                                    harga_txt[0] = 0;
                                } else {
                                    harga_txt[0] = Integer.parseInt(harga_lk.getText().toString());
                                }

                                if (!TextUtils.isEmpty(nama_txt[0]) && !TextUtils.isEmpty(status_txt[0]) && harga_txt[0] != 0) {

                                    proses.setVisibility(View.VISIBLE);
                                    input.setVisibility(View.GONE);

                                    Map<String, Object> profilupdate = new HashMap<>();
                                    profilupdate.put("nm_mnu", nama_txt[0]);
                                    profilupdate.put("sts_mnu", status_txt[0]);
                                    profilupdate.put("hrg_mnu", harga_txt[0]);

                                    db.collection("menu").document("minum").collection("list_minum").document(currentItem.getId_mnu_mn()).update(profilupdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                minumActivity.setMenuMinum();
                                                done.setVisibility(View.VISIBLE);
                                                proses.setVisibility(View.GONE);
                                            } else {
                                                gagal.setVisibility(View.VISIBLE);
                                                proses.setVisibility(View.GONE);
                                            }
                                        }
                                    });

                                } else if (!TextUtils.isEmpty(nama_txt[0]) && !TextUtils.isEmpty(status_txt[0])) {
                                    nama_lk.setError("Masukkan Nama Lauk!");
                                } else if (!TextUtils.isEmpty(nama_txt[0])) {
                                    harga_lk.setError("Masukkan Harga Lauk!");
                                } else {
                                    nama_lk.setError("Masukkan Nama Lauk!");
                                    harga_lk.setError("Masukkan Harga Lauk!");
                                }
                            }
                        } else {
                            nama_txt[0] = nama_lk.getText().toString();
                            status_txt[0] = status_lk.getSelectedItem().toString();
                            if (harga_lk.getText().equals("")) {
                                harga_txt[0] = 0;
                            } else {
                                harga_txt[0] = Integer.parseInt(harga_lk.getText().toString());
                            }

                            if (!TextUtils.isEmpty(nama_txt[0]) && !TextUtils.isEmpty(status_txt[0]) && harga_txt[0] != 0) {

                                proses.setVisibility(View.VISIBLE);
                                input.setVisibility(View.GONE);

                                Map<String, Object> profilupdate = new HashMap<>();
                                profilupdate.put("nm_mnu", nama_txt[0]);
                                profilupdate.put("sts_mnu", status_txt[0]);
                                profilupdate.put("hrg_mnu", harga_txt[0]);

                                db.collection("menu").document("minum").collection("list_minum").document(currentItem.getId_mnu_mn()).update(profilupdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            minumActivity.setMenuMinum();
                                            done.setVisibility(View.VISIBLE);
                                            proses.setVisibility(View.GONE);
                                        } else {
                                            gagal.setVisibility(View.VISIBLE);
                                            proses.setVisibility(View.GONE);
                                        }
                                    }
                                });

                            } else if (!TextUtils.isEmpty(nama_txt[0]) && !TextUtils.isEmpty(status_txt[0])) {
                                nama_lk.setError("Masukkan Nama Lauk!");
                            } else if (!TextUtils.isEmpty(nama_txt[0])) {
                                harga_lk.setError("Masukkan Harga Lauk!");
                            } else {
                                nama_lk.setError("Masukkan Nama Lauk!");
                                harga_lk.setError("Masukkan Harga Lauk!");
                            }
                        }
                    }
                });

                pBuilder.setView(pView);
                final AlertDialog dialog = pBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                selesai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                tutup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gagal.setVisibility(View.GONE);
                        input.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return mdataList.size();
    }

    public void filterList(ArrayList<Minum_Class> filteredList) {
        mdataList = filteredList;
        notifyDataSetChanged();
    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        minumActivity.startActivityForResult(intent, minumActivity.PICK_IMAGE_RESULT);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = minumActivity.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadFoto(final String id_mnu){
        final StorageReference storageReference = storageReference1.child(System.currentTimeMillis() + "." + getFileExtension(minumActivity.fotoUri));
        storageTask = storageReference.putFile(minumActivity.fotoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Map<String, Object> fotoupdate = new HashMap<>();
                        fotoupdate.put("img_mnu", uri.toString());
                        db.collection("menu").document("minum").collection("list_minum").document(id_mnu).update(fotoupdate);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(minumActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
            }
        });
    }

}
