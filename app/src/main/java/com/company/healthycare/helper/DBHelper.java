package com.company.healthycare.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.company.healthycare.model.DetailDiagnosaModel;
import com.company.healthycare.model.HeaderDiagnosisModel;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "healthycare.db",null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE HeaderDiagnosis (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "idUser text NULL, date text NULL, idDisease text NULL, valueCF FLOAT NULL)");
        db.execSQL("CREATE TABLE DetailDiagnosis (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "idUser TEXT NOT NULL, date TEXT NOT NULL, idIndication TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS HeaderDiagnosis");
        db.execSQL("DROP TABLE IF EXISTS DetailDiagnosis");
        onCreate(db);
    }

    public HeaderDiagnosisModel getDataHeader(String idUser,String date) {
        HeaderDiagnosisModel header;
        header = new HeaderDiagnosisModel();
        String selectQuery = "SELECT * FROM HeaderDiagnosis WHERE idUser='" + idUser + "' AND date='" + date + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                header.setId(cursor.getInt(0));
                header.setIdUser(cursor.getString(1));
                header.setDate(cursor.getString(2));
                header.setIdDisease(cursor.getString(3));
                header.setValueCF(cursor.getFloat(4));
            } while (cursor.moveToNext());
        }

        database.close();
        return header;
    }
    public ArrayList<HeaderDiagnosisModel> getDataHeaderByUser(String idUser) {
        ArrayList<HeaderDiagnosisModel> header;
        header = new ArrayList<>();
        String selectQuery = "SELECT * FROM HeaderDiagnosis WHERE idUser='" + idUser + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HeaderDiagnosisModel map = new HeaderDiagnosisModel();
                map.setId(cursor.getInt(0));
                map.setIdUser(cursor.getString(1));
                map.setDate(cursor.getString(2));
                map.setIdDisease(cursor.getString(3));
                map.setValueCF(cursor.getFloat(4));
                header.add(map);
            } while (cursor.moveToNext());
        }

        database.close();
        return header;
    }
    public ArrayList<DetailDiagnosaModel> getDataDetail(String idUser,String date) {
        ArrayList<DetailDiagnosaModel> detail;
        detail = new ArrayList<>();
        String selectQuery = "SELECT * FROM DetailDiagnosis WHERE idUser='" + idUser + "' AND date='" + date + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DetailDiagnosaModel map = new DetailDiagnosaModel();
                map.setId(cursor.getInt(0));
                map.setIdUser(cursor.getString(1));
                map.setDate(cursor.getString(2));
                map.setIdIndication(cursor.getString(3));
                detail.add(map);
            } while (cursor.moveToNext());
        }

        database.close();
        return detail;
    }

    public void insertToHeader(HeaderDiagnosisModel model) {
        SQLiteDatabase database = this.getWritableDatabase();
        String queryValues = "INSERT INTO HeaderDiagnosis (id,idUser,date,idDisease,valueCF) VALUES (null, " +
                "'" + model.getIdUser() + "', '" + model.getDate() + "', '" + model.getIdDisease() + "', " +
                "'" + model.getValueCF() + "')";
        database.execSQL(queryValues);
        database.close();
    }

    public void insertToDetail(DetailDiagnosaModel model) {
        SQLiteDatabase database = this.getWritableDatabase();
        String queryValues = "INSERT INTO DetailDiagnosis (id,idUser,date,idIndication) VALUES (null, " +
                "'" + model.getIdUser() + "', '" + model.getDate() + "', '" + model.getIdIndication() + "')";
        database.execSQL(queryValues);
        database.close();
    }
}
