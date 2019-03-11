package com.company.healthycare.model;

public class DetailDiagnosaModel {
    int id;
    String idUser;
    String date;
    String idIndication;

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

    public String getIdIndication() {
        return idIndication;
    }

    public void setIdIndication(String idIndication) {
        this.idIndication = idIndication;
    }

    public DetailDiagnosaModel(int id, String idUser, String date, String idIndication) {

        this.id = id;
        this.idUser = idUser;
        this.date = date;
        this.idIndication = idIndication;
    }

    public DetailDiagnosaModel() {

    }
}
