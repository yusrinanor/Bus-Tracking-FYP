package com.example.android.fyp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText nEmail, nPassword;
    private Button nLogin, nRegister;

    private FirebaseAuth authentication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        authentication = FirebaseAuth.getInstance();

        nEmail = (EditText) findViewById(R.id.email);
        nPassword = (EditText) findViewById(R.id.password);

        nLogin = (Button) findViewById(R.id.login);


        nLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // giving message instantly after click
                Toast.makeText(MainActivity.this, "Loading...", Toast.LENGTH_SHORT).show();
                if(validateText() == true){
                }
                else {
                    return;
                }
                // getting the text of the textfield
                final String email = nEmail.getText().toString() + "@apu.edu.my";
                final String password = nPassword.getText().toString();

                // sending request to API
                authentication.signInWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "User ID or password incorrect", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            FirebaseUser user = authentication.getCurrentUser();
                            updateUI(user);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currUser = authentication.getCurrentUser();
        updateUI(currUser);
    }

    private void updateUI(FirebaseUser user) {
        if(user != null){
            String userEmail = user.getEmail(); // get dr000001@apu.edu.my from email in firebase auth
            String[] userName = userEmail.split("@"); //spliting email into array seperated by @; output username = ['dr00001','apu.edu.my']
            //userName[0] = dr00001
            //userName[0].substring(0, 2) = dr
            String userType = userName[0].substring(0, 2);
            switch (userType){
                case "tp":
                    Intent userHome = new Intent(MainActivity.this, StudentHome.class);
                    startActivity(userHome);
                    finish();
                    break;
                case "dr":
                    Intent driverHome = new Intent(MainActivity.this, DriverHome.class);
                    startActivity(driverHome);
                    finish();
                    break;
                case "ad":
                    Intent adminHome = new Intent(MainActivity.this, AdminHome.class);
                    startActivity(adminHome);
                    finish();
                    break;
            }

        }
    }

    private boolean validateText() {
        boolean result = true;
        if(TextUtils.isEmpty(nPassword.getText()) && TextUtils.isEmpty(nEmail.getText())){
            result = false;
            Toast.makeText(this, "All fields are required to fill in", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(nPassword.getText()) || TextUtils.isEmpty(nEmail.getText())){
            result = false;
            Toast.makeText(this, "User ID or password needs to be filled in", Toast.LENGTH_SHORT).show();
        }

        return result;
    }

}
