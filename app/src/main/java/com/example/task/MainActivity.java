package com.example.task;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "GOOGLEAUTH";

    GoogleSignInClient mgoogleSignInClient;

    private FirebaseAuth mAuth;

    Button signin;

    EditText uname,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        //configuring Google Sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mgoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signin = findViewById(R.id.google);
        uname = findViewById(R.id.username);
        pass = findViewById(R.id.password);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


    }

    void signIn() {
        Intent intent = mgoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, 1000);
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
                            Intent i = new Intent(MainActivity.this, Logout.class);
                            startActivity(i);
                            finish();
                            // updateUI(user);
                        } else {
                            //Log.w(TAG,"Signin failed",task.getException());
                            // Snackbar.make(mBinding.mainLayout,"Authentication Failed.",Snackbar.LENGTH_SHORT);
                            //updateUI(null);
                            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}



