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
import com.company.healthycare.helper.DBHelper;
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
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setTitle("Riwayat Pemeriksaan");

        //membuat object DBHelper
        dbHelper = new DBHelper(this);

        datas = new ArrayList<>();
        dates = new ArrayList<>();
        diseases = new ArrayList<>();

        listView = findViewById(R.id.list_view);
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        //membuat object progress dialog, untuk menampilkan tampilan loading ketika program sedang memproses data
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Silahkan tunggu...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Proses...");

        //membuat fungsi ketika item pada listview di klik akan terjadi beberapa statement
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

        //memanggil fungsi
        retrieveHistory();
    }

    private void retrieveHistory() {
        //menampilkan tampilan loading pada awal fungsi
        progressDialog.show();
        datas.clear();
        dates.clear();

        //melakukan perulangan pada data yang di dapat pada SQlite database sesuai user id yang sedang login
        for(HeaderDiagnosisModel ds : dbHelper.getDataHeaderByUser(mUser.getUid())){
            //per item data di tambahkan ke array
            dates.add(ds.getDate());
            //array di tambahkan ke array lagi
            datas.add(ds);
        }

        //Fungsi dari firebase untuk mengambil data dari realtime database
        mReference.child("Diseases")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        diseases.clear();
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        //melakukan perulangan data yang di ambil dan dimasukkan ke dalam array
                        for(DataSnapshot ds : children){
                            DiseasesModel dm = ds.getValue(DiseasesModel.class);
                            diseases.add(dm);
                        }
                        //kemudian oper array ke CustomAdapterHistory untuk kemudian di proses dan di tampilkan
                        //sesuai perintah yang sudah di atur
                        adapter = new CustomAdapterHistory(HistoryActivity.this,datas,dates,diseases);
                        //Menambah adapter ke listview
                        listView.setAdapter(adapter);
                        //menghilangkan tampilan loading
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
}
