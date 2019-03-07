package com.company.healthycare;


import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SurveyActivity extends AppCompatActivity {
    ListView listView;
    List<IndicationModel> indications;
    Button btnCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        String[] indicationString = {
                "G.1 Sesak napas",
                "G.2 Nyeri dada",
                "G.3 Denyut jantung cepat",
                "G.4 Mudah lelah",
                "G.5 Kaki bengkak",
                "G.6 Nafsu makan menurun",
                "G.7 Keringat dingin",
                "G.8 Mual",
                "G.9 Nyeri punggung",
                "G.10 Batuk",
                "G.11 Demam ringan",
                "G.12 Pingsan",
                "G.13 Pusing",
                "G.14 Bercak merah pada kulit",
                "G.15 Nyeri perut",
                "G.16 Batuk darah",
                "G.17 Perut kembung"
        };

        listView = findViewById(R.id.listview);

        indications = new ArrayList<>();

        for(int i = 0; i<indicationString.length;i++){
            indications.add(new IndicationModel(false,indicationString[i]));
        }

        final CustomAdapter adapter = new CustomAdapter(this,indications);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                IndicationModel indication = indications.get(i);
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
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(SurveyActivity.this);
                progressDialog.setMessage("Silahkan tunggu...");
                progressDialog.setTitle("Proses...");
                progressDialog.show();
            }
        });
    }

}
