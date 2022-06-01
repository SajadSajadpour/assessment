package com.example.assessment.Model;

public class LandingListData {

    private String btnName;
    private int btnId;

    public LandingListData(String btnName, int btnId) {
        this.btnName = btnName;
        this.btnId = btnId;
    }

    public String getBtnName() {
        return btnName;
    }

    public void setBtnName(String btnName) {
        this.btnName = btnName;
    }

    public int getBtnId() {
        return btnId;
    }

    public void setBtnId(int btnId) {
        this.btnId = btnId;
    }
}
