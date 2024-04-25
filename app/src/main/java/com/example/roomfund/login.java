package com.example.roomfund;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class login extends AppCompatActivity {

    private Button btnSignUp, btnLogin;
    private EditText user_name, password;
    private ProgressDialog progressDialog;
    private TextView txtForgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUi();
        initListener();

    }

    private void initUi() {
        txtForgetPassword = findViewById(R.id.txt_forget_password2);
        btnSignUp = (Button) findViewById(R.id.btn_signup3);
        btnLogin = findViewById(R.id.btn_login3);
        user_name = findViewById(R.id.user_name2);
        password = findViewById(R.id.password2);
        progressDialog = new ProgressDialog(this);
    }

    private void initListener() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, signup.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_name.getText().toString().trim().equals("") && password.getText().toString().trim().equals("")) {
                    Toast.makeText(login.this, "Nhập email và mật khẩu của bạn", Toast.LENGTH_SHORT).show();
                } else if (password.getText().toString().trim().equals("")) {
                    Toast.makeText(login.this, "Nhâp mật khẩu của bạn", Toast.LENGTH_SHORT).show();
                } else if (user_name.getText().toString().trim().equals("")) {
                    Toast.makeText(login.this, "Nhập email của bạn", Toast.LENGTH_SHORT).show();
                } else {
                    onclickLogin();
                }
            }
        });

        txtForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_name.getText().toString().trim().equals("")) {
                    Toast.makeText(login.this, "Nhập email để nhận mail xác nhận", Toast.LENGTH_SHORT).show();
                } else {
                    onclickforgetPassword();
                }
            }
        });
    }

    private void onclickforgetPassword() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = user_name.getText().toString().trim();
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(login.this, "Đã gửi đến email xác nhận", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void onclickLogin() {
        FirebaseAuth auth= FirebaseAuth.getInstance();
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("please wait...");
        progressDialog.show();
        String strEmail=user_name.getText().toString().trim();
        String strPassword=password.getText().toString().trim();
        auth.signInWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent=new Intent(login.this,MainActivity2.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e("TAG", "onComplete: fail" );
                        }
                    }
                });
    }


}