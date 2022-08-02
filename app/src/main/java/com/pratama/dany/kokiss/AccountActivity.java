package com.pratama.dany.kokiss;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabWidget;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AccountActivity extends AppCompatActivity {

    private Button logout, delete;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user_auth = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView nm_display, aks_display, email_usr, pass_usr, nm_usr, gen_usr, tlp_usr;
    private ImageButton edit_email, edit_profil;
    private ImageView img_display;
    private String id_usr, foto, email, akses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        akses = intent.getStringExtra("akses");

        img_display = (ImageView) findViewById(R.id.img_display2);
        nm_display = (TextView) findViewById(R.id.nm_display);
        aks_display = (TextView) findViewById(R.id.aks_display);
        email_usr = (TextView) findViewById(R.id.email_usr);
        pass_usr = (TextView) findViewById(R.id.pass_usr);
        nm_usr = (TextView) findViewById(R.id.nm_usr);
        gen_usr = (TextView) findViewById(R.id.gen_usr);
        tlp_usr = (TextView) findViewById(R.id.tlp_usr);
        edit_email = (ImageButton) findViewById(R.id.edit_email);
        edit_profil = (ImageButton) findViewById(R.id.edit_profil);
        logout = (Button) findViewById(R.id.logout);
        delete = (Button) findViewById(R.id.delete);

        if(akses.equals("User")){
            edit_email.setVisibility(View.GONE);
            edit_profil.setVisibility(View.GONE);
            logout.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            delete.setText("Delete This Account");
        } else if(akses.equals("Admin")){
            logout.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
        }

        edit_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] old_txt = new String[1];
                final String[] pass_txt = new String[1];
                final String[] pass_confir_txt = new String[1];
                final String[] old_pstxt = new String[1];
                final int[] operasi = {1};

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(AccountActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.edit_email, null);
                final EditText old_pass = (EditText) mView.findViewById(R.id.old_pass);
                final EditText new_pass = (EditText) mView.findViewById(R.id.new_pass);
                final EditText new_pass_confir = (EditText) mView.findViewById(R.id.new_pass_confir);
                final ConstraintLayout input = (ConstraintLayout) mView.findViewById(R.id.input);
                final ConstraintLayout proses = (ConstraintLayout) mView.findViewById(R.id.proses);
                final ConstraintLayout done = (ConstraintLayout) mView.findViewById(R.id.done);
                final ConstraintLayout gagal = (ConstraintLayout) mView.findViewById(R.id.gagal);
                Button save_email = (Button) mView.findViewById(R.id.save_email);
                Button selesai = (Button) mView.findViewById(R.id.selesai);
                Button tutup = (Button) mView.findViewById(R.id.tutup);
                final TextView show = (TextView) mView.findViewById(R.id.show);
                final TextView error_txt = (TextView) mView.findViewById(R.id.info_err);

                proses.setVisibility(View.GONE);
                done.setVisibility(View.GONE);
                gagal.setVisibility(View.GONE);

                old_pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        old_pass.setBackground(getDrawable(R.drawable.edittext_active_style));
                        new_pass.setBackground(getDrawable(R.drawable.edittext_style));
                        new_pass_confir.setBackground(getDrawable(R.drawable.edittext_style));
                    }
                });
                new_pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        old_pass.setBackground(getDrawable(R.drawable.edittext_style));
                        new_pass.setBackground(getDrawable(R.drawable.edittext_active_style));
                        new_pass_confir.setBackground(getDrawable(R.drawable.edittext_style));
                    }
                });

                new_pass_confir.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        new_pass.setBackground(getDrawable(R.drawable.edittext_style));
                        old_pass.setBackground(getDrawable(R.drawable.edittext_style));
                        new_pass_confir.setBackground(getDrawable(R.drawable.edittext_active_style));
                    }
                });

                save_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        old_pass.setBackground(getDrawable(R.drawable.edittext_style));
                        new_pass.setBackground(getDrawable(R.drawable.edittext_style));
                        new_pass_confir.setBackground(getDrawable(R.drawable.edittext_style));
                    }
                });

                old_pass.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        chekJmlPass(s.length());
                        samePass(s.toString());
                    }

                    public void chekJmlPass(int sum){
                        if(sum < 6){
                            old_pass.setError("Password Minimal 6 Karakter!");
                        }
                    }

                    public void samePass(String pass){
                        if(pass.equals(pass_usr.getText().toString())){

                        } else {
                            old_pass.setError("Password Lama Anda Salah!");
                        }
                    }

                });

                new_pass.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        chekJmlPass(s.length());
                    }

                    public void chekJmlPass(int sum){
                        if(sum < 6){
                            new_pass.setError("Password Minimal 6 Karakter!");
                        }
                    }

                });

                new_pass_confir.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        chekJmlPass(s.length());
                        samePass(s.toString());
                    }

                    public void chekJmlPass(int sum){
                        if(sum < 6){
                            new_pass_confir.setError("Password Minimal 6 Karakter!");
                        }
                    }

                    public void samePass(String pass){
                        if(pass.equals(new_pass.getText().toString())){

                        } else {
                            new_pass_confir.setError("Konfirmasi Password Anda Salah!");
                        }
                    }
                });

                save_email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        old_txt[0] = old_pass.getText().toString();
                        pass_txt[0] = new_pass.getText().toString();
                        pass_confir_txt[0] = new_pass_confir.getText().toString();
                        old_pstxt[0] = pass_usr.getText().toString();

                        new_pass.setBackground(getDrawable(R.drawable.edittext_style));
                        old_pass.setBackground(getDrawable(R.drawable.edittext_style));
                        new_pass_confir.setBackground(getDrawable(R.drawable.edittext_style));

                        if(!TextUtils.isEmpty(old_txt[0]) && !TextUtils.isEmpty(pass_txt[0]) && !TextUtils.isEmpty(pass_confir_txt[0])){

                            if(old_txt[0].equals(old_pstxt[0]) && pass_confir_txt[0].equals(pass_txt[0])){
                                proses.setVisibility(View.VISIBLE);
                                input.setVisibility(View.GONE);

                                AuthCredential credential;

                                if(email.equals("")){
                                    credential = EmailAuthProvider.getCredential(user_auth.getEmail(), pass_usr.getText().toString());
                                } else {
                                    credential = EmailAuthProvider.getCredential(email, pass_usr.getText().toString());
                                }

                                user_auth.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        user_auth.updatePassword(pass_txt[0]).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(!task.isSuccessful()){
                                                    FirebaseAuthException error = (FirebaseAuthException )task.getException();
                                                    proses.setVisibility(View.GONE);
                                                    gagal.setVisibility(View.VISIBLE);
                                                    error_txt.setText(String.valueOf(error));
                                                }else {
                                                    done.setVisibility(View.VISIBLE);
                                                    proses.setVisibility(View.GONE);

                                                    Map<String, Object> emailupdate = new HashMap<>();
                                                    emailupdate.put("password", pass_txt[0]);

                                                    db.collection("login").document(id_usr).update(emailupdate);

                                                }
                                            }
                                        });
                                    }
                                });
                            } else if(!pass_confir_txt[0].equals(pass_txt[0]) && !old_txt[0].equals(old_pstxt[0])){
                                new_pass_confir.setError("Konfirmasi password salah!");
                                old_pass.setError("Password lama salah!");
                            } else if(!pass_confir_txt[0].equals(pass_txt[0])){
                                new_pass_confir.setError("Konfirmasi password salah!");
                            } else {
                                old_pass.setError("Password lama salah!");
                            }

                        } else if(!TextUtils.isEmpty(old_txt[0]) && !TextUtils.isEmpty(pass_txt[0])) {
                            new_pass_confir.setError("Masukkan Konfirmasi Password!");
                        } else if(!TextUtils.isEmpty(old_txt[0])){
                            new_pass.setError("Masukkan password baru!");
                            new_pass_confir.setError("Masukkan Konfirmasi Password!");
                        } else {
                            old_pass.setError("Masukkan password lama!");
                            new_pass.setError("Masukkan password baru!");
                            new_pass_confir.setError("Masukkan Konfirmasi Password!");
                        }
                    }
                });


                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
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
                        dialog.dismiss();
                    }
                });

                show.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(operasi[0] == 1){
                            old_pass.setTransformationMethod(null);
                            new_pass.setTransformationMethod(null);
                            new_pass_confir.setTransformationMethod(null);
                            show.setText("Hide Password");
                            operasi[0] = 0;
                        } else {
                            old_pass.setTransformationMethod(new PasswordTransformationMethod());
                            new_pass.setTransformationMethod(new PasswordTransformationMethod());
                            new_pass_confir.setTransformationMethod(new PasswordTransformationMethod());
                            show.setText("Show Password");
                            operasi[0] = 1;
                        }

                    }
                });
            }
        });

        edit_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] nama_txt = new String[1];
                final String[] gen_txt = new String[1];
                final String[] phone_txt = new String[1];

                final AlertDialog.Builder pBuilder = new AlertDialog.Builder(AccountActivity.this);
                final View pView = getLayoutInflater().inflate(R.layout.edit_profil, null);
                final EditText new_name = (EditText) pView.findViewById(R.id.new_name);
                final EditText new_gender = (EditText) pView.findViewById(R.id.new_gender);
                final EditText new_phone = (EditText) pView.findViewById(R.id.new_phone);
                final ConstraintLayout input_profil = (ConstraintLayout) pView.findViewById(R.id.input_profil);
                final ConstraintLayout proses_profil = (ConstraintLayout) pView.findViewById(R.id.proses_profil);
                final ConstraintLayout done_profil = (ConstraintLayout) pView.findViewById(R.id.done_profil);
                final ConstraintLayout gagal_profil = (ConstraintLayout) pView.findViewById(R.id.gagal_profil);
                Button save_profil = (Button) pView.findViewById(R.id.save_profil);
                Button selesai_profil = (Button) pView.findViewById(R.id.selesai_profil);
                Button tutup_profil = (Button) pView.findViewById(R.id.tutup_profil);

                new_name.setHint(nm_usr.getText().toString());
                new_gender.setHint(gen_usr.getText().toString());
                new_phone.setHint(tlp_usr.getText().toString());
                proses_profil.setVisibility(View.GONE);
                done_profil.setVisibility(View.GONE);
                gagal_profil.setVisibility(View.GONE);

                save_profil.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nama_txt[0] = new_name.getText().toString();
                        gen_txt[0] = new_gender.getText().toString();
                        phone_txt[0] = new_phone.getText().toString();

                        if(!TextUtils.isEmpty(nama_txt[0]) && !TextUtils.isEmpty(gen_txt[0]) && !TextUtils.isEmpty(phone_txt[0])){

                            proses_profil.setVisibility(View.VISIBLE);
                            input_profil.setVisibility(View.GONE);

                            Map<String, Object> profilupdate = new HashMap<>();
                            profilupdate.put("nama", nama_txt[0]);
                            profilupdate.put("gender", gen_txt[0]);
                            profilupdate.put("phone", phone_txt[0]);

                            db.collection("login").document(id_usr).update(profilupdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    done_profil.setVisibility(View.VISIBLE);
                                    proses_profil.setVisibility(View.GONE);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    gagal_profil.setVisibility(View.VISIBLE);
                                    proses_profil.setVisibility(View.GONE);
                                }
                            });

                        } else if(!TextUtils.isEmpty(nama_txt[0]) && !TextUtils.isEmpty(gen_txt[0])){
                            new_phone.setError("Masukkan Telepon!");
                        } else if(!TextUtils.isEmpty(nama_txt[0])){
                            new_gender.setError("Masukkan Gender!");
                            new_phone.setError("Masukkan Telepon!");
                        } else {
                            new_name.setError("Masukkan Nama!");
                            new_gender.setError("Masukkan Gender!");
                            new_phone.setError("Masukkan Telepon!");
                        }
                    }
                });

                pBuilder.setView(pView);
                final AlertDialog dialog = pBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                selesai_profil.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                tutup_profil.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder oBuilder = new AlertDialog.Builder(AccountActivity.this);
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
                                startActivity(new Intent(AccountActivity.this, LoginActivity.class));
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

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder oBuilder = new AlertDialog.Builder(AccountActivity.this);
                final View oView = getLayoutInflater().inflate(R.layout.delete, null);
                final TextView email_del = (TextView) oView.findViewById(R.id.email_del);
                final TextView info_del = (TextView) oView.findViewById(R.id.info_del);
                final ConstraintLayout input_del = (ConstraintLayout) oView.findViewById(R.id.input_del);
                final ConstraintLayout proses_del = (ConstraintLayout) oView.findViewById(R.id.proses_del);
                Button delete_del = (Button) oView.findViewById(R.id.delete_del);
                Button cancel_del = (Button) oView.findViewById(R.id.cancel_del);

                email_del.setText(user_auth.getEmail().toString());
                proses_del.setVisibility(View.GONE);

                delete_del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        input_del.setVisibility(View.GONE);
                        proses_del.setVisibility(View.VISIBLE);
                        info_del.setText("Menghapus Akun Anda...");

                        AuthCredential credential = EmailAuthProvider.getCredential(user_auth.getEmail(), pass_usr.getText().toString());

                        user_auth.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                user_auth.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            info_del.setText("Mengeluarkan anda dari aplikasi...");
                                            db.collection("login").document(id_usr).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    final Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            auth.signOut();
                                                            startActivity(new Intent(AccountActivity.this, LoginActivity.class));
                                                            finish();
                                                        }
                                                    }, 3000);
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        });
                    }
                });

                oBuilder.setView(oView);
                final AlertDialog dialog = oBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                cancel_del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(email.equals("")) {
            db.collection("login").whereEqualTo("email", user_auth.getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                    for (DocumentSnapshot query : snapshots) {
                        id_usr = query.getId();
                        foto = query.getString("foto");
                        nm_display.setText(query.getString("nama"));
                        aks_display.setText(query.getString("akses"));
                        email_usr.setText(query.getString("email"));
                        pass_usr.setText(query.getString("password"));
                        nm_usr.setText(query.getString("nama"));
                        gen_usr.setText(query.getString("gender"));
                        tlp_usr.setText(query.getString("phone"));
                    }
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageReference = storage.getReferenceFromUrl(foto);

                    try {
                        final File localFile = File.createTempFile("images", "png,jpg");
                        storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                img_display.setImageBitmap(bitmap);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        });
                    } catch (IOException err) {
                    }
                }
            });
        } else {
            db.collection("login").whereEqualTo("email", email).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                    for (DocumentSnapshot query : snapshots) {
                        id_usr = query.getId();
                        foto = query.getString("foto");
                        nm_display.setText(query.getString("nama"));
                        aks_display.setText(query.getString("akses"));
                        email_usr.setText(query.getString("email"));
                        pass_usr.setText(query.getString("password"));
                        nm_usr.setText(query.getString("nama"));
                        gen_usr.setText(query.getString("gender"));
                        tlp_usr.setText(query.getString("phone"));
                    }
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageReference = storage.getReferenceFromUrl(foto);

                    try {
                        final File localFile = File.createTempFile("images", "png,jpg");
                        storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                img_display.setImageBitmap(bitmap);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        });
                    } catch (IOException err) {
                    }
                }
            });
        }
    }
}
