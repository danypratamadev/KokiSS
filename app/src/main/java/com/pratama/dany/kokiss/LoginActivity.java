package com.pratama.dany.kokiss;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private EditText inputemail, inputpass;
    private TextView reset, show;
    private Button login;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String id_usr;
    final String[] email_txt = new String[1];
    int value = 0;
    private int operasi = 1;
    private ConstraintLayout input_login, proses_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputemail = (EditText) findViewById(R.id.email);
        inputpass = (EditText) findViewById(R.id.pass);
        login = (Button) findViewById(R.id.login);
        reset = (TextView) findViewById(R.id.reset);
        show = (TextView) findViewById(R.id.show);
        input_login = (ConstraintLayout) findViewById(R.id.input_login);
        proses_login = (ConstraintLayout) findViewById(R.id.proses_login);
        proses_login.setVisibility(View.GONE);

        inputemail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                inputemail.setBackground(getDrawable(R.drawable.edittext_active_style));
                inputpass.setBackground(getDrawable(R.drawable.edittext_style));
            }
        });

        inputpass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                inputemail.setBackground(getDrawable(R.drawable.edittext_style));
                inputpass.setBackground(getDrawable(R.drawable.edittext_active_style));
            }
        });

        login.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                inputemail.setBackground(getDrawable(R.drawable.edittext_style));
                inputpass.setBackground(getDrawable(R.drawable.edittext_style));
            }
        });

        inputemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Is_Valid_Email(s.toString());
            }

            public void Is_Valid_Email(String edt) {
                if (edt == null) {
                    inputemail.setError("Invalid Email Address");
                    email_txt[0] = null;
                } else if (isEmailValid(edt) == false) {
                    inputemail.setError("Invalid Email Address");
                    email_txt[0] = null;
                } else {
                    email_txt[0] = edt;
                }
            }

            boolean isEmailValid(CharSequence email) {
                return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                        .matches();
            }
        });

        inputpass.addTextChangedListener(new TextWatcher() {
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
                    inputpass.setError("Password Minimal 6 Karakter!");
                }
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(operasi == 1){
                    inputpass.setTransformationMethod(null);
                    show.setText("Hide Password");
                    operasi = 0;
                } else {
                    inputpass.setTransformationMethod(new PasswordTransformationMethod());
                    show.setText("Show Password");
                    operasi = 1;
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final String email = email_txt[0];
                final String password = inputpass.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

                    input_login.setVisibility(View.GONE);
                    proses_login.setVisibility(View.VISIBLE);

                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                if (password.length() < 6) {
                                    input_login.setVisibility(View.VISIBLE);
                                    proses_login.setVisibility(View.GONE);
                                    inputpass.setError("Minimal Password 6 Digits");
                                } else {
                                    input_login.setVisibility(View.VISIBLE);
                                    proses_login.setVisibility(View.GONE);
                                    Toast.makeText(LoginActivity.this, "Periksa kembali email dan password anda!", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                db.collection("login").whereEqualTo("email", email).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                                        for(DocumentSnapshot query : snapshots){
                                            if(query.getString("akses").equals("Koki")){
                                                Intent intent = new Intent(LoginActivity.this, KokiActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else if(query.getString("akses").equals("Kasir")){
                                                Intent intent = new Intent(LoginActivity.this, KasirActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    }
                                });
                            }
                        }

                    });

                } else if (!TextUtils.isEmpty(email)) {
                    inputpass.setError("Masukkan Password Anda!");
                } else if (!TextUtils.isEmpty(password)){
                    inputemail.setError("Masukkan Email Anda!");
                } else {
                    inputemail.setError("Masukkan Email Anda!");
                    inputpass.setError("Masukkan Password Anda!");
                }
            }

        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.forget_pass, null);
                final EditText email_forget = (EditText) mView.findViewById(R.id.email_forget);
                final ConstraintLayout input_forget = (ConstraintLayout) mView.findViewById(R.id.input_forget);
                final ConstraintLayout proses_forget = (ConstraintLayout) mView.findViewById(R.id.proses_forget);
                final ConstraintLayout done_forget = (ConstraintLayout) mView.findViewById(R.id.done_forget);
                final ConstraintLayout gagal_forget = (ConstraintLayout) mView.findViewById(R.id.gagal_forget);
                Button send_forget = (Button) mView.findViewById(R.id.send_forget);
                Button selesai_forget = (Button) mView.findViewById(R.id.selesai_forget);
                Button tutup_forget= (Button) mView.findViewById(R.id.tutup_forget);
                final TextView error_txt_forget = (TextView) mView.findViewById(R.id.info_err_forget);

                proses_forget.setVisibility(View.GONE);
                done_forget.setVisibility(View.GONE);
                gagal_forget.setVisibility(View.GONE);

                email_forget.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        email_forget.setBackground(getDrawable(R.drawable.edittext_active_style));
                    }
                });

                send_forget.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        email_forget.setBackground(getDrawable(R.drawable.edittext_style));
                    }
                });

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                send_forget.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email_forget_txt = email_forget.getText().toString();

                        if(!TextUtils.isEmpty(email_forget_txt)){
                            input_forget.setVisibility(View.GONE);
                            proses_forget.setVisibility(View.VISIBLE);

                            auth.sendPasswordResetEmail(email_forget_txt).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        proses_forget.setVisibility(View.GONE);
                                        done_forget.setVisibility(View.VISIBLE);
                                    } else {
                                        FirebaseAuthException error = (FirebaseAuthException )task.getException();
                                        proses_forget.setVisibility(View.GONE);
                                        gagal_forget.setVisibility(View.VISIBLE);
                                        error_txt_forget.setText(String.valueOf(error));
                                    }
                                }
                            });
                        } else {
                            email_forget.setError("Masukkan Email Anda!");
                            email_forget.requestFocus();
                        }

                    }
                });

                selesai_forget.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                tutup_forget.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }
}
