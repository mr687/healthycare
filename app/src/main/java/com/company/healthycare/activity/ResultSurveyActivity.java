package com.company.healthycare.activity;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.company.healthycare.R;
import com.company.healthycare.model.DetailDiagnosaModel;
import com.company.healthycare.model.DiseasesModel;
import com.company.healthycare.model.HeaderDiagnosisModel;
import com.company.healthycare.model.IndicationsModel;
import com.company.healthycare.model.UsersModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ResultSurveyActivity extends AppCompatActivity {
    TextView txtFullName,txtAge,txtEmail,txtGender;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    ProgressDialog progressDialog;
    ListView listView;
    List<IndicationsModel> listIndication;
    List<DiseasesModel> listDisease;
    List<HeaderDiagnosisModel> diagnosis;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_survey);

        listView = findViewById(R.id.list_view);
        title = findViewById(R.id.title);

        listDisease = new ArrayList<>();
        listIndication = new ArrayList<>();
        diagnosis = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        progressDialog = new ProgressDialog(ResultSurveyActivity.this);
        progressDialog.setMessage("Silahkan tunggu...");
        progressDialog.setTitle("Proses...");

        txtFullName = findViewById(R.id.txt_full_name);
        txtAge = findViewById(R.id.txt_age);
        txtEmail = findViewById(R.id.txt_email);
        txtGender = findViewById(R.id.txt_gender);
        
        loadProfileInformation();
    }

    private void loadResultDiagnosis() {
        mReference.child("Diseases")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listDisease.clear();
                        final Iterable<DataSnapshot> child = dataSnapshot.getChildren();
                        for(DataSnapshot ds : child){
                            DiseasesModel dm = ds.getValue(DiseasesModel.class);
                            listDisease.add(dm);
                        }
                        mReference.child("HeaderDiagnosis")
                                .child(mUser.getUid())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        HeaderDiagnosisModel header = dataSnapshot.getValue(HeaderDiagnosisModel.class);
                                        for(DiseasesModel disease : listDisease){
                                            if(disease.ID.equals(header.idDisease)){

                                            }
                                        }
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

    private void loadSelectedIndications() {
        mReference.child("Indications")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listIndication.clear();
                        Iterable<DataSnapshot> child = dataSnapshot.getChildren();
                        for(DataSnapshot ds : child){
                            IndicationsModel im = ds.getValue(IndicationsModel.class);
                            listIndication.add(im);
                        }
                        mReference.child("DetailDiagnosis")
                                .child(mUser.getUid())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                        List<DetailDiagnosaModel> listSelected = new ArrayList<>();
                                        ArrayList<String> list = new ArrayList<>();
                                        for(DataSnapshot data : children){
                                            DetailDiagnosaModel selectedIndication = data.getValue(DetailDiagnosaModel.class);
                                            listSelected.add(selectedIndication);
                                        }
                                        for(DetailDiagnosaModel dd : listSelected){
                                            for(IndicationsModel ind : listIndication){
                                                if(dd.getIdIndication().equals(ind.IDIndications)){
                                                    list.add(ind.IDIndications + ". Anda mengalami " + ind.NameIndications);
                                                }
                                            }
                                        }
                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(),R.layout.mytextview,list);
                                        listView.setAdapter(adapter);
                                        ListAdapter listAdapter = listView.getAdapter();
                                        if(listAdapter != null){
                                            int totalheight = 0;
                                            for(int i = 0; i < listAdapter.getCount();i++){
                                                View listItem  = listAdapter.getView(i, null, listView);
                                                listItem.measure(0,0);
                                                totalheight += listItem.getMeasuredHeight();
                                            }
                                            ViewGroup.LayoutParams params = listView.getLayoutParams();
                                            params.height = totalheight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
                                            listView.setLayoutParams(params);
                                            listView.requestLayout();
                                        }
                                        loadResultDiagnosis();
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

    private void loadProfileInformation() {
        progressDialog.show();
        mReference.child("users").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    UsersModel users = dataSnapshot.getValue(UsersModel.class);
                    txtAge.setText(": " + users.getAge());
                    if(users.getGender() != null && users.getGender().equals("Male")){
                        txtGender.setText(": " + "Laki-Laki");
                    }else{
                        txtGender.setText(": " + "Perempuan");
                    }
                }
                txtFullName.setText(": " + mUser.getDisplayName());
                txtEmail.setText(": " + mUser.getEmail());
                loadSelectedIndications();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
