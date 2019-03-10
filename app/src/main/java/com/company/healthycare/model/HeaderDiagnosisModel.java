package com.company.healthycare.model;

public class HeaderDiagnosisModel {
    public String idDisease;
    public float valueCF;

    public HeaderDiagnosisModel() {
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

    public HeaderDiagnosisModel(String idDisease, float valueCF) {

        this.idDisease = idDisease;
        this.valueCF = valueCF;
    }
}
