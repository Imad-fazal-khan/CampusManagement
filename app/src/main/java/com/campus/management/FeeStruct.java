package com.campus.management;

import java.io.Serializable;

public class FeeStruct implements Serializable {
    private String structClass;
    private String admissionCharges;
    private String registrationFee;
    private String securityCharges;
    private String monthlyTutionFee;

    public FeeStruct() {
    }

    public FeeStruct(String structClass, String admissionCharges, String registrationFee, String securityCharges, String monthlyTutionFee) {
        this.structClass = structClass;
        this.admissionCharges = admissionCharges;
        this.registrationFee = registrationFee;
        this.securityCharges = securityCharges;
        this.monthlyTutionFee = monthlyTutionFee;
    }

    public String getStructClass() {
        return structClass;
    }

    public void setStructClass(String structClass) {
        this.structClass = structClass;
    }

    public String getAdmissionCharges() {
        return admissionCharges;
    }

    public void setAdmissionCharges(String admissionCharges) {
        this.admissionCharges = admissionCharges;
    }

    public String getRegistrationFee() {
        return registrationFee;
    }

    public void setRegistrationFee(String registrationFee) {
        this.registrationFee = registrationFee;
    }

    public String getSecurityCharges() {
        return securityCharges;
    }

    public void setSecurityCharges(String securityCharges) {
        this.securityCharges = securityCharges;
    }

    public String getMonthlyTutionFee() {
        return monthlyTutionFee;
    }

    public void setMonthlyTutionFee(String monthlyTutionFee) {
        this.monthlyTutionFee = monthlyTutionFee;
    }
}
