package com.example.task;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Register extends AppCompatActivity {

    GoogleSignInClient mgoogleSignInClient;



    EditText username,password;
    TextView alredacc;

    Button regbtn,googlebtn;

    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }
    }


    void signIn() {
        // Revoke access before initiating google sign in process
//        mgoogleSignInClient.revokeAccess().addOnCompleteListener(this, new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                // Now, initiate google sign in process
//                Intent intent = mgoogleSignInClient.getSignInIntent();
//                startActivityForResult(intent, 1000);
//            }
//        });

        // Now, initiate google sign in process
        Intent intent = mgoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, 1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        //configuring Google Sign in with requested email
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mgoogleSignInClient = GoogleSignIn.getClient(this, gso);

    username = findViewById(R.id.username);
    password = findViewById(R.id.password);
    alredacc = findViewById(R.id.text);
    regbtn = findViewById(R.id.Registerbtn);
    mAuth = FirebaseAuth.getInstance();
    googlebtn = findViewById(R.id.google);


    regbtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String email = username.getText().toString();
            String pass = password.getText().toString();

            if(TextUtils.isEmpty(email)){
                Toast.makeText(Register.this, "Insert Email!!", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if(TextUtils.isEmpty(pass))
            {
                Toast.makeText(Register.this, "Enter Password!", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Register.this, "Account Created.",
                                        Toast.LENGTH_SHORT).show();
                                Intent i =new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(i);


                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Register.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    });

    alredacc.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(Register.this, Login.class);
            Toast.makeText(Register.this, "Login Here!", Toast.LENGTH_SHORT).show();
            startActivity(i);
        }
    });


    googlebtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            signIn();
        }
    });

    }








    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthwithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w(TAG, "Google Sign In Failed", e);
            }
        }

        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);


                firebaseAuthwithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w(TAG, "Google Sign In Failed", e);
            }
        }
    }

    private void firebaseAuthwithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "SignInWithCredentials:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            finish();
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();

                        } else {

                        }
                    }
                });
    }
}