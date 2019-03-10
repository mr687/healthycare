package com.company.healthycare.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.company.healthycare.R;
import com.company.healthycare.model.UsersModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity
implements View.OnClickListener {
    FirebaseAuth mAuth;
    EditText txtEmail,txtPassword;
    TextView textClickLogin;
    Button btnSignUp;
    ProgressBar progressBar;
    FirebaseDatabase mDatabase;
    ProgressDialog progressDialog;
    DatabaseReference mReferences;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mDatabase = FirebaseDatabase.getInstance();
        mReferences = mDatabase.getReference("users");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setTitle("Processing...");

        textClickLogin = findViewById(R.id.txt_click_login);
        btnSignUp = findViewById(R.id.btn_sign_up);
        txtEmail = findViewById(R.id.txt_email);
        txtPassword = findViewById(R.id.txt_password);
        progressBar = findViewById(R.id.progress_bar);
        
        btnSignUp.setOnClickListener(this);
        textClickLogin.setOnClickListener(this);
    }

    private void doRegister(){
        final String email = txtEmail.getText().toString().trim();
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

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    mAuth = FirebaseAuth.getInstance();
                    mUser = mAuth.getCurrentUser();
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName("")
                            .build();
                    mUser.updateProfile(profileChangeRequest);
                    UsersModel users = new UsersModel();
                    users.setAge("");
                    users.setEmail(mUser.getEmail());
                    users.setFullName("");
                    users.setGender("");
                    mReferences.child(mUser.getUid())
                            .setValue(users)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"Account registered successfull.",Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                }
                            });
                }else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        txtEmail.setError("Email is already.");
                        txtEmail.requestFocus();
                        return;
                    }else{
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == textClickLogin){
            finish();
        }
        else if(v == btnSignUp){
            doRegister();
        }
    }
}
