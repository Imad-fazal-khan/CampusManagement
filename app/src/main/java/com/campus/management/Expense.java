package com.campus.management;

public class Expense {

    private String expId, expDescription, expDate, expAmount, madeBy;

    public Expense() {
    }

    public Expense(String expId, String expDescription, String expDate, String expAmount, String madeBy) {
        this.expId = expId;
        this.expDescription = expDescription;
        this.expDate = expDate;
        this.expAmount = expAmount;
        this.madeBy=madeBy;
    }

    public String getMadeBy() {
        return madeBy;
    }

    public void setMadeBy(String madeBy) {
        this.madeBy = madeBy;
    }

    public String getExpId() {
        return expId;
    }

    public void setExpId(String expId) {
        this.expId = expId;
    }

    public String getExpDescription() {
        return expDescription;
    }

    public void setExpDescription(String expDescription) {
        this.expDescription = expDescription;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getExpAmount() {
        return expAmount;
    }

    public void setExpAmount(String expAmount) {
        this.expAmount = expAmount;
    }
}
