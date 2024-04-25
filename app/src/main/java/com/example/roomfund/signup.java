package com.example.roomfund;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roomfund.fragment.myProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signup extends AppCompatActivity {

    private Button btnLogin,btnRegister;
    private EditText user_name, password,password2;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initUi();
        initListener();

    }

    private void initListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(signup.this, login.class);
                startActivity(i);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_name.getText().toString().trim().equals("")) {
                    Toast.makeText(signup.this,"Yêu cầu nhập tài khoản",Toast.LENGTH_LONG).show();
                }else if (password.getText().toString().trim().equals(password2.getText().toString().trim())) {
                    onClickSignup();
                }else {
                    Toast.makeText(signup.this,"Mật khẩu không khớp",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void onClickSignup() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String strEmail=user_name.getText().toString().trim();
        String strPassword=password.getText().toString().trim();
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("please wait...");
        progressDialog.show();
        auth.createUserWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent=new Intent(signup.this, myProfile.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(signup.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void initUi() {
        btnLogin=(Button)findViewById(R.id.btn_login2);
        user_name=findViewById(R.id.user_name);
        password=(EditText)findViewById(R.id.password);
        password2=(EditText)findViewById(R.id.password3);
        btnRegister=(Button)findViewById(R.id.btn_signup2);
        progressDialog = new ProgressDialog(this);
    }
}