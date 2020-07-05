package com.campus.management;

public class Fee {
    private String studName, stdClass, stdFName, subAmount, feeType, date, feeDiscount;

    public Fee() {
    }

    public Fee(String studName, String stdClass, String stdFName, String subAmount, String feeType, String date, String feeDiscount) {
        this.studName = studName;
        this.stdClass = stdClass;
        this.stdFName = stdFName;
        this.subAmount = subAmount;
        this.date=date;
        this.feeType = feeType;
        this.feeDiscount=feeDiscount;
    }

    public String getFeeDiscount() {
        return feeDiscount;
    }

    public void setFeeDiscount(String feeDiscount) {
        this.feeDiscount = feeDiscount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getStudName() {
        return studName;
    }

    public void setStudName(String studName) {
        this.studName = studName;
    }

    public String getStdClass() {
        return stdClass;
    }

    public void setStdClass(String stdClass) {
        this.stdClass = stdClass;
    }

    public String getStdFName() {
        return stdFName;
    }

    public void setStdFName(String stdFName) {
        this.stdFName = stdFName;
    }

    public String getSubAmount() {
        return subAmount;
    }

    public void setSubAmount(String subAmount) {
        this.subAmount = subAmount;
    }
}
