package com.example.task;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
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

public class Login extends AppCompatActivity {


    private static final String TAG = "GOOGLEAUTH";

    GoogleSignInClient mgoogleSignInClient;

    private FirebaseAuth mAuth;

    Button signin,logbtn;

    EditText uname,passw;
    TextView forgetpass,regbtn;


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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        //configuring Google Sign in with requested email
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mgoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signin = findViewById(R.id.google);
        uname = findViewById(R.id.username);
        passw = findViewById(R.id.password);
        forgetpass = findViewById(R.id.forget);
        regbtn = findViewById(R.id.regisbtn);
        logbtn = findViewById(R.id.loginbtn);




        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Register.class);
                Toast.makeText(Login.this,"Register Here!",Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


        logbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = uname.getText().toString();
                String pass = passw.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Login.this, "Insert Email!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(pass))
                {
                    Toast.makeText(Login.this, "Enter Password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information

                                    Intent i = new Intent(Login.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                    Toast.makeText(getApplicationContext(),"LoggedIn",Toast.LENGTH_SHORT).show();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
//
//    void signIn() {
//        // initiates google sign in process
//        mgoogleSignInClient.revokeAccess();
//        Intent intent = mgoogleSignInClient.getSignInIntent();
//        startActivityForResult(intent, 1000);
//    }


    void signIn() {
        // Revoke access before initiating google sign in process
        mgoogleSignInClient.revokeAccess().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Now, initiate google sign in process
                Intent intent = mgoogleSignInClient.getSignInIntent();
                startActivityForResult(intent, 1000);
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
                            Intent i = new Intent(Login.this, MainActivity.class);
                            startActivity(i);
                            finish();
                            Toast.makeText(Login.this, "Success", Toast.LENGTH_SHORT).show();

                        } else {

                        }
                    }
                });
    }
}



