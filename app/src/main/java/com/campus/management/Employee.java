package com.campus.management;

import java.io.Serializable;

public class Employee implements Serializable {

    private String empName, empEmail, empPassword, acessRegisteration, accessExpenses, accessFee;

    public Employee() {
    }

    public Employee(String empName, String empEmail, String empPassword, String acessRegisteration, String accessExpenses, String accessFee) {
        this.empName = empName;
        this.empEmail = empEmail;
        this.empPassword = empPassword;
        this.acessRegisteration = acessRegisteration;
        this.accessExpenses = accessExpenses;
        this.accessFee = accessFee;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    public String getEmpPassword() {
        return empPassword;
    }

    public void setEmpPassword(String empPassword) {
        this.empPassword = empPassword;
    }

    public String getAcessRegisteration() {
        return acessRegisteration;
    }

    public void setAcessRegisteration(String acessRegisteration) {
        this.acessRegisteration = acessRegisteration;
    }

    public String getAccessExpenses() {
        return accessExpenses;
    }

    public void setAccessExpenses(String accessExpenses) {
        this.accessExpenses = accessExpenses;
    }

    public String getAccessFee() {
        return accessFee;
    }

    public void setAccessFee(String accessFee) {
        this.accessFee = accessFee;
    }
}
