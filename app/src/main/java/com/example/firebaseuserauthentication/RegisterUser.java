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
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private TextView Car2GO, btn_regi;
    private EditText et_name, et_age, et_email2, et_pw;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        Car2GO = (TextView) findViewById(R.id.tv_regHeading);
        Car2GO.setOnClickListener(this);

        btn_regi = (Button) findViewById(R.id.btn_regi);
        btn_regi.setOnClickListener(this);

        et_name = (EditText)findViewById(R.id.et_name);
        et_age = (EditText)findViewById(R.id.et_age);
        et_email2 = (EditText)findViewById(R.id.et_email2);
        et_pw = (EditText)findViewById(R.id.et_pw);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tv_regHeading:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.btn_regi:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String email = et_email2.getText().toString().trim();
        String password = et_pw.getText().toString().trim();
        String name = et_name.getText().toString().trim();
        String age = et_age.getText().toString().trim();

        if(name.isEmpty()){
            et_name.setError("Full name is required");
            et_name.requestFocus();
            return;
        }

        if(age.isEmpty()){
            et_age.setError("Age is required");
            et_age.requestFocus();
            return;
        }

        if(email.isEmpty()){
            et_email2.setError("Email is required");
            et_email2.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email2.setError("Please provide a valid email");
            et_email2.requestFocus();
            return;
        }

        if(password.isEmpty()){
            et_pw.setError("Password is required");
            et_pw.requestFocus();
            return;
        }

        if(password.length() < 6){
            et_pw.setError("Password length should be more than 6 characters");
            et_pw.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(name, age, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterUser.this,"User has been registered successfully!",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);


                                        //redirect to login layout
                                    }else{
                                        Toast.makeText(RegisterUser.this, "Failed to regster! Try again!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(RegisterUser.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }
}