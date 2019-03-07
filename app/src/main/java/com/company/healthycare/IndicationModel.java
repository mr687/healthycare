package com.company.healthycare;

public class IndicationModel {
    boolean isSelected;
    String mIndication;

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
    public IndicationModel(boolean isSelected, String mIndication) {

        this.isSelected = isSelected;
        this.mIndication = mIndication;
    }
}
