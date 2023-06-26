package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email, password, password2;
    private Button btnRegister;
    private TextView textLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        // initialising all views through id defined above
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        password2 = findViewById(R.id.confirm_password);
        btnRegister = findViewById(R.id.register);
        textLogin= findViewById(R.id.text_login);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Register();
            }
        });

        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
    private void Register(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String user, pass, pass2;
        user = email.getText().toString().trim();
        pass = password.getText().toString().trim();
        pass2 = password2.getText().toString().trim();
        if(user.isEmpty()) {
            email.setError("Email cannot be empty");
        }
        if(pass.isEmpty()) {
            password.setError("Password cannot be empty");
        }
        else
        {
            if( !pass.equals(pass2)){
                password2.setError("Password would not be matched");
            }
            else{


                mAuth.createUserWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "User registered Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });}
        }


    }


}
// hogy karu run ?? hnn ismein hi karti hoo
//pc par hi karu run?
