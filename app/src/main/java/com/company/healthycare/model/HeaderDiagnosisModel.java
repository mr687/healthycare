package com.company.healthycare.model;

public class HeaderDiagnosisModel {
    int id;
    String idUser;
    String date;
    String idDisease;
    float valueCF;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIdDisease() {
        return idDisease;
    }

    public void setIdDisease(String idDisease) {
        this.idDisease = idDisease;
    }

    public float getValueCF() {
        return valueCF;
    }

    public void setValueCF(float valueCF) {
        this.valueCF = valueCF;
    }

    public HeaderDiagnosisModel(int id, String idUser, String date, String idDisease, float valueCF) {

        this.id = id;
        this.idUser = idUser;
        this.date = date;
        this.idDisease = idDisease;
        this.valueCF = valueCF;
    }

    public HeaderDiagnosisModel() {

    }
}
