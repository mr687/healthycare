package com.company.healthycare.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.company.healthycare.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
implements View.OnClickListener {
    FirebaseAuth mAuth;
    Button startSurvey,btnHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Healthy Care");
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        startSurvey = findViewById(R.id.btn_start_inspection);
        btnHistory = findViewById(R.id.btn_history_inspection);

        startSurvey.setOnClickListener(this);
        btnHistory.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_logout:
                Dialog.OnClickListener dialogOnclickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case DialogInterface.BUTTON_POSITIVE:
                                FirebaseAuth.getInstance().signOut();
                                finish();
                                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialogInterface.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Apakah anda yakin?").setPositiveButton("Iya",dialogOnclickListener)
                        .setNegativeButton("Tidak",dialogOnclickListener).show();
                break;
            case R.id.nav_profile:
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() == null){
            finish();
            Intent it = new Intent(this,LoginActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);
        }
    }

    @Override
    public void onClick(View view) {
        if(view == startSurvey){
            Intent it = new Intent(MainActivity.this,SurveyActivity.class);
            startActivity(it);
        }else if(view == btnHistory){
            Intent it = new Intent(MainActivity.this,HistoryActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(it);
        }
    }
}
