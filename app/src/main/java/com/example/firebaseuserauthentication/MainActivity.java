package com.example.firebaseuserauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private EditText et_email,et_password;
    private Button btn_login;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.tv_register);
        register.setOnClickListener(this);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterUser.class));
                break;

            case R.id.btn_login:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        if(email.isEmpty()){
            et_email.setError("Email is required");
            et_email.requestFocus();
            return;
        }

        if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.setError("Please enter a valid email");
            et_email.requestFocus();
            return;
        }

        if(password.isEmpty()){
            et_password.setError("Password is required");
            et_password.requestFocus();
            return;
        }

        if(password.length() < 6){
            et_password.setError("Password length should be greater than 6 characters!");
            et_password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
               if(task.isSuccessful()){

               }else{
                   Toast.makeText(MainActivity.this, "Failed to login! Please check your credentials", Toast.LENGTH_SHORT).show();
               }
            }
        });

    }
}