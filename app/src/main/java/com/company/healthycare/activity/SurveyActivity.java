package com.company.healthycare.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.company.healthycare.CustomAdapter;
import com.company.healthycare.R;
import com.company.healthycare.helper.DBHelper;
import com.company.healthycare.model.DetailDiagnosaModel;
import com.company.healthycare.model.DiseasesModel;
import com.company.healthycare.model.HeaderDiagnosisModel;
import com.company.healthycare.model.IndicationsModel;
import com.company.healthycare.model.ListIndicationsModel;
import com.company.healthycare.model.RelationsModel;
import com.company.healthycare.model.SelectedIndicationModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SurveyActivity extends AppCompatActivity
implements View.OnClickListener {
    ListView listView;
    List<ListIndicationsModel> indications;
    List<IndicationsModel> mIndications;
    Button btnCheck;
    CustomAdapter adapter;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    ProgressDialog progressDialog;
    List<RelationsModel> mRelations;
    List<DiseasesModel> mDiseases;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        dbHelper = new DBHelper(this);

        listView = findViewById(R.id.listview);
        indications = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        progressDialog = new ProgressDialog(SurveyActivity.this);
        progressDialog.setMessage("Silahkan tunggu...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Proses...");

        mRelations = new ArrayList<>();
        mIndications = new ArrayList<>();
        mDiseases = new ArrayList<>();
        retrieveIndications();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListIndicationsModel indication = indications.get(i);
                if(indication.isSelected()){
                    indication.setSelected(false);
                }else{
                    indication.setSelected(true);
                }
                indications.set(i,indication);
                adapter.updateRecords(indications);
            }
        });

        btnCheck = findViewById(R.id.btn_check);
        btnCheck.setOnClickListener(this);
    }

    private void retrieveIndications(){
        progressDialog.show();
        mReference.child("Indications").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                indications.clear();
                mIndications.clear();
                Iterable<DataSnapshot> child = dataSnapshot.getChildren();
                for(DataSnapshot ds : child){
                    IndicationsModel single = ds.getValue(IndicationsModel.class);
                    indications.add(new ListIndicationsModel(false,single.IDIndications + ". " + single.NameIndications,single.IDIndications));
                    mIndications.add(single);
                }
                adapter = new CustomAdapter(SurveyActivity.this,indications);
                listView.setAdapter(adapter);

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void checkByIndication(){
        progressDialog.show();
        mReference.child("Relations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mRelations.clear();
                Iterable<DataSnapshot> child = dataSnapshot.getChildren();
                for(DataSnapshot ds : child){
                    RelationsModel relationsModel = ds.getValue(RelationsModel.class);
                    mRelations.add(relationsModel);
                }
                mReference.child("Diseases").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mDiseases.clear();
                        Iterable<DataSnapshot> child = dataSnapshot.getChildren();
                        for(DataSnapshot ds : child){
                            DiseasesModel mDisease = ds.getValue(DiseasesModel.class);
                            mDiseases.add(mDisease);
                        }

                        final List<String> valueCF = new ArrayList<>();

                        List<Float> P1 = new ArrayList<>();
                        List<Float> P2 = new ArrayList<>();
                        List<Float> P3 = new ArrayList<>();
                        List<Float> P4 = new ArrayList<>();
                        List<Float> P5 = new ArrayList<>();
                        List<Float> P6 = new ArrayList<>();
                        List<Float> P7 = new ArrayList<>();

                        for(RelationsModel rel : mRelations){
                            for(ListIndicationsModel ls : indications){
                                if(ls.isSelected()){
                                    if(rel.IDIndication.equals(ls.getID())){
                                        if(rel.IDDiesease.equals("P1")) P1.add(rel.ValueCF);
                                        if(rel.IDDiesease.equals("P2")) P2.add(rel.ValueCF);
                                        if(rel.IDDiesease.equals("P3")) P3.add(rel.ValueCF);
                                        if(rel.IDDiesease.equals("P4")) P4.add(rel.ValueCF);
                                        if(rel.IDDiesease.equals("P5")) P5.add(rel.ValueCF);
                                        if(rel.IDDiesease.equals("P6")) P6.add(rel.ValueCF);
                                        if(rel.IDDiesease.equals("P7")) P7.add(rel.ValueCF);
                                    }
                                }
                            }
                        }
                        for(ListIndicationsModel ls : indications){
                            if(ls.isSelected()) valueCF.add(ls.getID());
                        }
                        final List<SelectedIndicationModel> allValueCF = new ArrayList<>();
                        allValueCF.add(new SelectedIndicationModel("P1","",calculateCF(P1)));
                        allValueCF.add(new SelectedIndicationModel("P2","",calculateCF(P2)));
                        allValueCF.add(new SelectedIndicationModel("P3","",calculateCF(P3)));
                        allValueCF.add(new SelectedIndicationModel("P4","",calculateCF(P4)));
                        allValueCF.add(new SelectedIndicationModel("P5","",calculateCF(P5)));
                        allValueCF.add(new SelectedIndicationModel("P6","",calculateCF(P6)));
                        allValueCF.add(new SelectedIndicationModel("P7","",calculateCF(P7)));

                        float max =0;
                        for(SelectedIndicationModel sel : allValueCF){
                            if(sel.getValueCF() > max) max = sel.getValueCF();
                        }
                        for(SelectedIndicationModel sel : allValueCF){
                            if(sel.getValueCF() == max){
                                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                final FirebaseUser mUser = mAuth.getCurrentUser();
                                final String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
                                HeaderDiagnosisModel hm = new HeaderDiagnosisModel();
                                hm.setId(0);
                                hm.setIdUser(mUser.getUid());
                                hm.setDate(date);
                                hm.setValueCF(sel.getValueCF());
                                hm.setIdDisease(sel.getIdDisease());
                                dbHelper.insertToHeader(hm);

                                for(int i =0; i< valueCF.size();i++){
                                    final DetailDiagnosaModel dm = new DetailDiagnosaModel();
                                    dm.setId(0);
                                    dm.setIdUser(mUser.getUid());
                                    dm.setDate(date);
                                    dm.setIdIndication(valueCF.get(i));
                                    dbHelper.insertToDetail(dm);
                                }

                                progressDialog.dismiss();
                                Intent it = new Intent(SurveyActivity.this,ResultSurveyActivity.class);
                                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                it.putExtra("date",date);
                                startActivity(it);
                                finish();
                            }
                        }
                    }

                    private float calculateCF(List<Float> disease) {
                        float preP = 0 ;
                        if(disease.size() >= 2){
                            preP = disease.get(0) + (disease.get(1) * (1 - disease.get(0)));
                            for(int i = 2;i<disease.size();i++){
                                preP = disease.get(i) + (preP * (1 - disease.get(i)));
                            }
                        }else{
                            return 0;
                        }
                        return preP;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View view) {
        if(view == btnCheck){
            checkByIndication();
        }
    }
}
