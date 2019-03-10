package com.company.healthycare.model;

public class RelationsModel {
    public String IDIndication;
    public String IDDiesease;
    public float ValueCF;

    public RelationsModel(String IDIndication, String IDDiesease, float valueCF) {
        this.IDIndication = IDIndication;
        this.IDDiesease = IDDiesease;
        ValueCF = valueCF;
    }

    public RelationsModel() {

    }
}
