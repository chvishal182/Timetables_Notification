package com.cvishaldev.timetable_s;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class ViewTT extends AppCompatActivity {

    FirebaseAuth viewAuth;
    FirebaseDatabase viewDBInst;
    DatabaseReference databaseReference;
    String subscribeTopic, toSendTopic, presentBranch,presentYear,aa;
    Button viewTT,toEditTT;
    TextView toShowSubs, toShowSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tt);

        toShowSubs = findViewById(R.id.tVSubs);
        toShowSend = findViewById(R.id.tVToSend);
        viewTT     = findViewById(R.id.btnViewTT);
        toEditTT   = findViewById(R.id.btnChangeTT);
        viewAuth   = FirebaseAuth.getInstance();
        viewDBInst = FirebaseDatabase.getInstance();

        readData(new FirebaseCallback() {
            @Override
            public void onCallback(String str) {
                presentBranch =str;
            }
        },viewDBInst.getReference().child("users").child(viewAuth.getCurrentUser().getUid().trim()).child("branch"));
        readData(new FirebaseCallback() {
            @Override
            public void onCallback(String str) {
                presentYear =str;
            }
        },viewDBInst.getReference().child("users").child(viewAuth.getCurrentUser().getUid().trim()).child("year"));


        viewTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                subscribeTopic = presentBranch+"_"+presentYear;
                toSendTopic    = "/topics/"+subscribeTopic+"_br";
                toShowSend.setText(toSendTopic);
                toShowSubs.setText(subscribeTopic);

                FirebaseMessaging.getInstance().subscribeToTopic(subscribeTopic).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ViewTT.this, "subscribed to"+subscribeTopic, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(ViewTT.this, task.getException().getMessage().trim(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        toEditTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toEditTimeTable = new Intent(getApplicationContext(),com.cvishaldev.timetable_s.Edit.class);
                toEditTimeTable.putExtra("userToken",toSendTopic);
                startActivity(toEditTimeTable);

            }
        });

    }


    private interface FirebaseCallback{
        void onCallback(String str);
    }

    private void readData(ViewTT.FirebaseCallback firebaseCallback,DatabaseReference databaseReference){

        ValueEventListener
                valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                aa = snapshot.getValue(String.class);

                firebaseCallback.onCallback(aa);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } ;
        databaseReference.addListenerForSingleValueEvent(valueEventListener);

    }










}