package com.example.task;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;

    Button button;
    TextView tv1;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        button = findViewById(R.id.btn);
        tv1 = findViewById(R.id.textv);
        user = mAuth.getCurrentUser();

        if(user == null)
        {
            Intent i = new Intent(getApplicationContext(), Login.class);
            startActivity(i);
            finish();
        }
        else{

            tv1.setText(user.getEmail());
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent i=new Intent(getApplicationContext(), Login.class);

                //flags added so that if we click back button on the login page it won't show up the logout page.
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

                Toast.makeText(getApplicationContext(), "LogOut", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}



