package com.company.healthycare.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.company.healthycare.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity
implements View.OnClickListener {
    public final int RC_SIGN_IN = 202;
    private FirebaseAuth mAuth;
    EditText txtEmail,txtPassword;
    TextView textClickCreate;
    Button btnSignIn;
    ProgressDialog progressDialog;
    GoogleSignInClient mGoogleSignInClient;
    SignInButton signInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mAuth = FirebaseAuth.getInstance();
        btnSignIn = findViewById(R.id.btn_sign_in);
        textClickCreate = findViewById(R.id.txt_click_create);
        txtPassword = findViewById(R.id.txt_password);
        txtEmail = findViewById(R.id.txt_email);
        signInButton = findViewById(R.id.btn_sign_in_with_google);
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setTitle("Processing...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

//        Click
        textClickCreate.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        signInButton.setOnClickListener(this);
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            successSignIn();
                        } else {
                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
    }

    private void doSIgnIn(){
        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        if(email.isEmpty()){
            txtEmail.setError("Email is required.");
            txtEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtEmail.setError("Email format is incorrect.");
            txtEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            txtPassword.setError("Password is required.");
            txtPassword.requestFocus();
            return;
        }

        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    successSignIn();
                }else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }
    private void successSignIn(){
        finish();
        Intent it = new Intent(LoginActivity.this,MainActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(it);
    }
    @Override
    public void onClick(View v) {
        if(v == textClickCreate){
            startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
        }else if(v == btnSignIn){
            doSIgnIn();
        }else if(v == signInButton){
            signInWithGoogle();
        }
    }
}
