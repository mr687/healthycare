package com.company.healthycare.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.company.healthycare.CustomAdapter;
import com.company.healthycare.CustomAdapterHistory;
import com.company.healthycare.R;
import com.company.healthycare.model.DiseasesModel;
import com.company.healthycare.model.HeaderDiagnosisModel;
import com.company.healthycare.model.ListIndicationsModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    List<HeaderDiagnosisModel> datas;
    List<String> dates;
    List<DiseasesModel> diseases;
    CustomAdapterHistory adapter;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mReference;
    ProgressDialog progressDialog;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setTitle("Riwayat Pemeriksaan");

        datas = new ArrayList<>();
        dates = new ArrayList<>();
        diseases = new ArrayList<>();

        listView = findViewById(R.id.list_view);
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Silahkan tunggu...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Proses...");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String date = dates.get(i);
                Intent it = new Intent(HistoryActivity.this,ResultSurveyActivity.class);
                it.putExtra("date",date);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(it);
            }
        });

        retrieveHistory();
    }

    private void retrieveHistory() {
        progressDialog.show();
        mReference.child("HeaderDiagnosis")
                .child(mUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        datas.clear();
                        dates.clear();
                        Iterable<DataSnapshot> child = dataSnapshot.getChildren();
                        for(DataSnapshot ds : child){
                            dates.add(ds.getKey());
                            HeaderDiagnosisModel head = ds.getValue(HeaderDiagnosisModel.class);
                            datas.add(head);
                        }
                        mReference.child("Diseases")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        diseases.clear();
                                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                        for(DataSnapshot ds : children){
                                            DiseasesModel dm = ds.getValue(DiseasesModel.class);
                                            diseases.add(dm);
                                        }
                                        adapter = new CustomAdapterHistory(HistoryActivity.this,datas,dates,diseases);
                                        listView.setAdapter(adapter);
                                        progressDialog.dismiss();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
}
