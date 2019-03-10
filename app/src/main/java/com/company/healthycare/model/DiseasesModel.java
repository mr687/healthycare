package com.company.healthycare.model;

public class DiseasesModel {
    public String ID;
    public String Name;
    public String Treatments;

    public DiseasesModel(String ID, String name, String treatments) {
        this.ID = ID;
        Name = name;
        Treatments = treatments;
    }

    public DiseasesModel() {
    }
}
