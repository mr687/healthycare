package com.company.healthycare;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity
implements View.OnClickListener {
    TextView textClickLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        textClickLogin = findViewById(R.id.txt_click_login);

        textClickLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == textClickLogin){
            finish();
        }
    }
}
