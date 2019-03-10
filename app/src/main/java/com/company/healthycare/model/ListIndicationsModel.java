package com.company.healthycare.model;

import java.io.Serializable;

public class ListIndicationsModel implements Serializable {
    boolean isSelected;
    String mIndication;
    String ID;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getmIndication() {
        return mIndication;
    }

    public void setmIndication(String mIndication) {
        this.mIndication = mIndication;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public ListIndicationsModel(boolean isSelected, String mIndication, String ID) {

        this.isSelected = isSelected;
        this.mIndication = mIndication;
        this.ID = ID;
    }

    public ListIndicationsModel() {

    }
}
