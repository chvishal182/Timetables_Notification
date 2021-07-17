package com.cvishaldev.timetable_br;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView askLogin,askRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askRegister = findViewById(R.id.askRegister);
        askLogin    = findViewById(R.id.askLogin);

        askLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginPage = new Intent(getApplicationContext(),com.cvishaldev.timetable_br.Login.class);
                startActivity(loginPage);
                finish();
            }
        });

        askRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerPage = new Intent(getApplicationContext(),com.cvishaldev.timetable_br.Register.class);
                startActivity(registerPage);
                finish();
            }
        });
    }
}