package com.example.task;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends AppCompatActivity {


    TextView alredacc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


    alredacc = findViewById(R.id.text);

    alredacc.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(Register.this, Login.class);
            Toast.makeText(Register.this, "Login Here!", Toast.LENGTH_SHORT).show();
            startActivity(i);
        }
    });

    }
}