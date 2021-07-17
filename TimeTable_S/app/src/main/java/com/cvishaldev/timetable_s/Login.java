package com.cvishaldev.timetable_s;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    FirebaseAuth logAuth;
    String userLogMail, userLogPass;
    EditText logMail, logPass;
    Button redirectSignUp,logInto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logAuth  = FirebaseAuth.getInstance();
        logMail  = findViewById(R.id.eTxtLoginMail);
        logPass  = findViewById(R.id.eTxtLoginPassW);


        redirectSignUp = findViewById(R.id.reSignUp);
        redirectSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerPage = new Intent(getApplicationContext(),com.cvishaldev.timetable_s.Register.class);
                startActivity(registerPage);
                finish();
            }
        });

        logInto = findViewById(R.id.btnLoginUser);
        logInto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userLogMail = logMail.getText().toString().trim();
                userLogPass = logPass.getText().toString().trim();
                if(validateCredentials()){
                loginUser();}
            }
        });


    }

    private boolean validateCredentials() {
        boolean stats = true;

        if(userLogPass.length() == 0)
        {
            logPass.setError("Please enter your password");
            logPass.requestFocus();stats = false;
        }
        if(userLogMail.length() == 0)
        {
            logMail.setError("Please enter your mailID");
            logMail.requestFocus();stats = false;
        }

        return stats;
    }

    private void loginUser() {

        logAuth.signInWithEmailAndPassword(userLogMail,userLogPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Intent toViewTimeTable = new Intent(getApplicationContext(), com.cvishaldev.timetable_s.ViewTT.class);
                        startActivity(toViewTimeTable);
                        finish();
                    }
                    else
                        {
                            Toast.makeText(Login.this, task.getException().getMessage().trim(), Toast.LENGTH_SHORT).show();

                        }
            }
        });

    }
}