package com.example.task;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Logout extends AppCompatActivity {

    Button logout;
    FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        mauth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mauth.signOut();
                Intent i=new Intent(Logout.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}