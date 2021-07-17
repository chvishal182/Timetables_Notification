package com.cvishaldev.timetable_s;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Edit extends AppCompatActivity {

    Button askEdit;
    Intent toReceiveIn;
    String toSendTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

       toReceiveIn = getIntent();
       toSendTopic = toReceiveIn.getStringExtra("userToken");
        askEdit     = findViewById(R.id.btnAskEdit);

        askEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),toSendTopic,Toast.LENGTH_LONG).show();
                fcmSender sender = new fcmSender(toSendTopic,
                        "SWC_TimeTables",
                        "Change waiting for Approval",
                        Edit.this,
                        Edit.this);

               sender.sendNotifications();
            }

        });
    }
}