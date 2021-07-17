package com.cvishaldev.timetable_s;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class waitForFirebase extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase instance;
    DatabaseReference yearReference, branchReference;
    Button btnTest;
    String yeaR, brancH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_firebase);

        auth            = FirebaseAuth.getInstance();
        instance        = FirebaseDatabase.getInstance();
        branchReference = instance.getReference().child("users").child(auth.getCurrentUser().getUid().trim()).child("branch");
        yearReference   = instance.getReference().child("users").child(auth.getCurrentUser().getUid().trim()).child("year");
        btnTest         = findViewById(R.id.btnTest);
        readData(new FirebaseCallback() {
            @Override
            public void onCallback(String str) {
                brancH = str;
            }
        });

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(waitForFirebase.this,brancH,Toast.LENGTH_LONG).show();
            }
        });

    }

    private interface FirebaseCallback{
        void onCallback(String str);
    }

    private void readData(FirebaseCallback firebaseCallback){

        ValueEventListener
                valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                brancH = snapshot.getValue(String.class);

                firebaseCallback.onCallback(brancH);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } ;
        branchReference.addListenerForSingleValueEvent(valueEventListener);

    }

}