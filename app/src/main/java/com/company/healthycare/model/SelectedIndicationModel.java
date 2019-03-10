package com.company.healthycare.model;

public class SelectedIndicationModel {
    String idDisease;
    String idIndication;
    float valueCF;

    public String getIdDisease() {
        return idDisease;
    }

    public void setIdDisease(String idDisease) {
        this.idDisease = idDisease;
    }

    public String getIdIndication() {
        return idIndication;
    }

    public void setIdIndication(String idIndication) {
        this.idIndication = idIndication;
    }

    public float getValueCF() {
        return valueCF;
    }

    public void setValueCF(float valueCF) {
        this.valueCF = valueCF;
    }

    public SelectedIndicationModel(String idDisease, String idIndication) {

        this.idDisease = idDisease;
        this.idIndication = idIndication;
    }

    public SelectedIndicationModel(String idDisease, String idIndication, float valueCF) {

        this.idDisease = idDisease;
        this.idIndication = idIndication;
        this.valueCF = valueCF;
    }

    public SelectedIndicationModel() {

    }
}
