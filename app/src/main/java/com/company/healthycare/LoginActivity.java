package com.company.healthycare;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity
implements View.OnClickListener {
    TextView textClickCreate;
    Button btnSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        btnSignIn = findViewById(R.id.btn_sign_in);
        textClickCreate = findViewById(R.id.txt_click_create);

//        Click
        textClickCreate.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == textClickCreate){
            startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
        }else if(v == btnSignIn){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }
    }
}
