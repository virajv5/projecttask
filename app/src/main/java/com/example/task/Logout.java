package com.example.task;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
                Intent i=new Intent(Logout.this, Login.class);

               //flags added so that if we click back button on the login page it won't show up the logout page.
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

                Toast.makeText(Logout.this, "LogOut", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}