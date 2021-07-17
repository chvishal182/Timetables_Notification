package com.cvishaldev.timetable_br;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Register extends AppCompatActivity {

    EditText name,mail,passW;
    Spinner spnBranch, spnYear;
    String[] branch,year;
    ArrayList<String> branchList, yearList;
    String userBranch, userYear, userName, userMail,userPass,userToken;
    ArrayAdapter<String> branchAdp,yearAdp;
    Button submit,toLogin;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference users,timetable;
    ProgressBar prgBRegLoad;
    HashMap<String,String> userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initializing our views and variables

        spnBranch        = findViewById(R.id.spnBranch);
        spnYear          = findViewById(R.id.spnYear);
        name             = findViewById(R.id.eTxtName);
        mail             = findViewById(R.id.eTxtLoginMail);
        passW            = findViewById(R.id.eTxtLoginPassW);
        submit           = findViewById(R.id.btnSubmit);
        firebaseAuth     = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        toLogin          = findViewById(R.id.btnLoginPage);
        prgBRegLoad      = findViewById(R.id.prgBReg);
        userDetails      = new HashMap<>();


        // initializing the branch
        branch = new String[]{"Select your Branch","cse","ece","me"};
        // initializing the year
        year   = new String[]{"Select your Year","1st","2nd","3rd","4th"};

        //Converting these array of Strings into ArrayList
        branchList = new ArrayList<>(Arrays.asList(branch));
        yearList   = new ArrayList<>(Arrays.asList(year));


        //functions for filling up the spinners
        fillUpspnBranch();
        fillUpspnYear();

        submit.setOnClickListener(v -> {
            prgBRegLoad.setVisibility(View.VISIBLE);
            if(validateUser())
            {

                createUser();
            }
            else
            {
                Toast.makeText(Register.this, "Please Provide with the correct credentials", Toast.LENGTH_SHORT).show();
                prgBRegLoad.setVisibility(View.INVISIBLE);
            }
        });

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginPage = new Intent(getApplicationContext(),com.cvishaldev.timetable_br.Login.class);
                prgBRegLoad.setVisibility(View.INVISIBLE);
                startActivity(loginPage);
                finish();
            }
        });
    }



    private void fillUpspnYear() {
        yearAdp = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,yearList)
        {
            @Override
            public boolean isEnabled(int position) {
                //Disabling our first item from the list and using it as a hint
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View dropView =  super.getDropDownView(position, convertView, parent);
                TextView hintYear = (TextView) dropView;
                if(position == 0)
                {
                    hintYear.setTextColor(Color.GRAY);
                }
                else
                {
                    hintYear.setTextColor(Color.BLACK);
                }
                return dropView;
            }
        };
        yearAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnYear.setAdapter(yearAdp);

    }

    private void fillUpspnBranch() {

        branchAdp = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,branchList)
        {
            @Override
            public boolean isEnabled(int position) {
                //Disabling our first item from the list and using it as a hint
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View dropView =  super.getDropDownView(position, convertView, parent);
                TextView hintBranch = (TextView) dropView;
                if(position == 0)
                {
                    hintBranch.setTextColor(Color.GRAY);
                }
                else
                {
                    hintBranch.setTextColor(Color.BLACK);
                }
                return dropView;
            }
        };
        branchAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnBranch.setAdapter(branchAdp);
    }

    boolean validateUser()
    {
        boolean stat = true;

        //getting inputs from User

        userName   = name.getText().toString().trim();
        userBranch = spnBranch.getSelectedItem().toString().trim();
        userYear   = spnYear.getSelectedItem().toString().trim();
        userMail   = mail.getText().toString().trim();
        userPass   = passW.getText().toString().trim();

        if(userName.length() == 0)
        {
            name.setError("Please Enter Your Name : (");
            name.requestFocus();
            stat = false;
        }
        if (userBranch.length() > 4 && userYear.length() > 3 )
        {
            Toast.makeText(this, "Please Mention Your Branch and Year", Toast.LENGTH_SHORT).show();
            stat = false;
        }
        else
        {
            if(userBranch.length() > 4)
            {
                Toast.makeText(this, "Please Mention Your Branch", Toast.LENGTH_SHORT).show();stat = false;
            }
            else if (userYear.length() > 3)
            {
                Toast.makeText(this, "Please Mention Your Year", Toast.LENGTH_SHORT).show();stat = false;
            }
        }


        if(userMail.length() == 0&& !Patterns.EMAIL_ADDRESS.matcher(userMail).matches())
        {
            mail.setError("Please provide us with a Mail_ID viz is valid");
            mail.requestFocus();
            stat = false;
        }
        else
        {
            if(userMail.length() == 0)
            {
                mail.setError("Please provide us with a Mail_ID"); stat = false;
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(userMail).matches())
            {
                mail.setError("Provided Mail_ID isn't a valid one"); stat = false;
            }
            mail.requestFocus();
        }

        if(userPass.length() == 0 && userPass.length() < 6)
        {
            passW.setError("Please secure your account with a password of minimum length 6 units");
            passW.requestFocus();
            stat = false;
        }
        else
        {
            if(userPass.length() == 0)
            {
                passW.setError("Secure your account by setting up a password"); stat = false;
            }
            else if(userPass.length() < 6)
            {
                passW.setError("Minimum length of password must be 6 units"); stat = false;
            }
            passW.requestFocus();

        }

        return stat;
    }

    private void createUser() {

        firebaseAuth.createUserWithEmailAndPassword(userMail,userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    users = firebaseDatabase.getReference("BR").child(firebaseAuth.getCurrentUser().getUid().trim());
                    saveToDatabase();

                }
                else
                {
                    Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }prgBRegLoad.setVisibility(View.INVISIBLE);
            }
        });



    }

    private void saveToDatabase() {



        userDetails.put("name",userName);
        userDetails.put("branch",userBranch);
        userDetails.put("year",userYear);
        userDetails.put("mail-ID",userMail);
        userDetails.put("password",userPass);
        users.setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    FirebaseMessaging.getInstance().subscribeToTopic(userBranch+"_"+userYear+"_br").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                Toast.makeText(Register.this, "successfully subscribed to "+userBranch+"_"+userYear+"_br", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(Register.this, task.getException().getMessage().trim(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Intent registerPage = new Intent(getApplicationContext(),com.cvishaldev.timetable_br.Login.class);
                    startActivity(registerPage);prgBRegLoad.setVisibility(View.INVISIBLE);

                    finish();
                }
                else
                {
                    prgBRegLoad.setVisibility(View.INVISIBLE);
                    Toast.makeText(Register.this, task.getException().getMessage().trim(), Toast.LENGTH_SHORT).show();prgBRegLoad.setVisibility(View.INVISIBLE);
                }
            }
        });


    }
}